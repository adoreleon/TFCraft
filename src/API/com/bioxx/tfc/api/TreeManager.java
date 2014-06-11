package com.bioxx.tfc.api;

import java.util.HashMap;

import com.bioxx.tfc.api.Util.intCoord;

public class TreeManager
{
	private HashMap<intCoord, HashMap<intCoord, Integer>> trees = new HashMap<intCoord, HashMap<intCoord, Integer>>();

	public static TreeManager instance = new TreeManager();
	public TreeManager() {}

	public void addTree(intCoord stump, HashMap treeBlocks)
	{
		//TODO treeBlocks needs to be sorted by intCoord.y DESC,
		//that way the blocks get processed from top down,
		//leaving no holes when the tools breaks before getting to the stump.
		//TODO We also need a good way to save and restore data, all this is only in memory at the moment.

		Boolean notFound = true;
		for(intCoord key : trees.keySet())
		{
			if(key.equals(stump))
			{
				trees.remove(key);
				trees.put(stump, treeBlocks);
				notFound = false;
				break;
			}
		}

		if(notFound)
			trees.put(stump, treeBlocks);
	}

	public void delTree(intCoord stump)
	{
		for(intCoord key : trees.keySet())
		{
			if(key.equals(stump))
			{
				trees.remove(key);
				break;
			}
		}
	}

	public HashMap getTreeBlocks(intCoord bCoord)
	{
		for(HashMap treeBlocks : trees.values())
		{
			for(Object key : treeBlocks.keySet())
			{
				if(bCoord.equals(key))
					return treeBlocks;
			}
		}
		return null;
	}

	public void updateTreeBlocks(intCoord stump, HashMap treeBlocks)
	{
		delTree(stump);
		trees.put(stump, treeBlocks);
	}
}
