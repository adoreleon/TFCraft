package com.bioxx.tfc.WorldGen.Generators.Trees;

import java.util.HashMap;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

import com.bioxx.tfc.TFCBlocks;
import com.bioxx.tfc.TFCItems;
import com.bioxx.tfc.Core.TFC_Core;
import com.bioxx.tfc.api.TreeManager;
import com.bioxx.tfc.api.Util.intCoord;

public class WorldGenDouglasFir extends WorldGenerator
{
	private boolean Tall = false;
	private int metaID;
	private HashMap<intCoord, Integer> treeBlocks;

	public WorldGenDouglasFir(boolean notify, int m, boolean t)
	{
		super(notify);
		metaID = m;
		Tall = t;
	}

	@Override
	public boolean generate(World world, Random rand, int x, int y, int z)
	{
		treeBlocks = new HashMap<intCoord, Integer>();
		
		int i = rand.nextInt(10) + 10;
		if(rand.nextInt(20) == 0)
			Tall=true;
		if(Tall)
			i += rand.nextInt(10);
		boolean flag = true;

		if (y < 1 || y + i + 1 > 256)
			return false;

		for (int j = y; j <= y + 1 + i; j++)
		{
			byte byte0 = 1;

			if (j == y)
				byte0 = 0;

			if (j >= (y + 1 + i) - 2)
				byte0 = 2;

			for (int l = x - byte0; l <= x + byte0 && flag; l++)
			{
				for (int j1 = z - byte0; j1 <= z + byte0 && flag; j1++)
				{
					if (j >= 0 && j < 256)
					{
						Block j2 = world.getBlock(l, j, j1);
						if (j2 != Blocks.air && (j2 != TFCBlocks.Leaves || j2 != TFCBlocks.Leaves2) &&
								(j2 != TFCBlocks.Grass || j2 != TFCBlocks.Grass2) &&
								(j2 != TFCBlocks.Dirt || j2 != TFCBlocks.Dirt2) &&
								(j2 != TFCBlocks.LogNatural || j2 != TFCBlocks.LogNatural2) &&
								(j2 != TFCBlocks.Sapling || j2 != TFCBlocks.Sapling2))
						{
							flag = false;
						}
					}
					else
					{
						flag = false;
					}
				}
			}
		}

		if (!flag)
			return false;

		Block b = world.getBlock(x, y - 1, z);
		if (!TFC_Core.isSoil(b) || y >= 256 - i - 1)
			return false;

		byte byte1 = 3;
		int i1 = 0;

		for (int k1 = y + (i / 3) - 1; k1 <= y + i - 1; k1++)
		{
			int k2 = k1 - (y + i);
			int j3 = 1 - k2 / 2;
			int zz = i;
			if (i > 20)
				zz = 20;
			int xx = zz / 10 + 1;
			if (k1 - y > i / 2 || k1 - y - (i / 3) + 2 < 3)
				xx--;
			if(y + i - k1 < 4)
				xx=1;

			for (int l3 = x - xx; l3 <= x + xx; l3++)
			{
				int j4 = l3 - x;
				for (int l4 = z- xx; l4 <= z + xx; l4++)
				{
					int i5 = l4 - z;
					b = world.getBlock(l3, k1, l4);
					if ((Math.abs(j4) != 0 || Math.abs(i5) != 0 && k2 != 0) && 
							(Math.abs(j4) + Math.abs(i5) != xx * 2 || (k1 - y > i / 2 && k1 - y < (4 * i / 5)) || 
							k1 - y - (i / 3) + 2 == 2) && (rand.nextInt(12) != 0) && b == Blocks.air)
					{
						setBlockAndNotifyAdequately(world, l3, k1, l4, TFCBlocks.Leaves, metaID);
						treeBlocks.put(new intCoord(l3, k1, l4), -1);
					}
				}
			}
		}
		setBlockAndNotifyAdequately(world, x, y + i, z, TFCBlocks.Leaves, metaID);
		treeBlocks.put(new intCoord(x, y + i, z), -1);
		for (int l1 = 0; l1 < i - 1; l1++)
		{
			Block l2 = world.getBlock(x, y + l1, z);
			if (l2 != Blocks.air && (l2 != TFCBlocks.Leaves || l2 != TFCBlocks.Leaves2))
				continue;
			setBlockAndNotifyAdequately(world, x, y + l1, z, TFCBlocks.LogNatural, metaID);
			treeBlocks.put(new intCoord(x, y + l1, z), metaID);
		}
		TreeManager.instance.addTree(new intCoord(x, y, z), treeBlocks);
		return true;
	}

}
