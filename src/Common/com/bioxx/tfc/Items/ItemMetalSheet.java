package com.bioxx.tfc.Items;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import com.bioxx.tfc.TFCBlocks;
import com.bioxx.tfc.Core.TFCTabs;
import com.bioxx.tfc.TileEntities.TEMetalSheet;
import com.bioxx.tfc.api.HeatIndex;
import com.bioxx.tfc.api.HeatRegistry;
import com.bioxx.tfc.api.TFC_ItemHeat;
import com.bioxx.tfc.api.Enums.EnumSize;
import com.bioxx.tfc.api.Enums.EnumWeight;

public class ItemMetalSheet extends ItemTerra
{
	protected int[][] sidesMap = new int[][]{{0,-1,0},{0,1,0},{0,0,-1},{0,0,+1},{-1,0,0},{1,0,0}};
	public int metalID;

	public ItemMetalSheet(int mID)
	{
		super();
		setMaxDamage(0);
		this.setCreativeTab(TFCTabs.TFCMaterials);
		setFolder("ingots/");
		this.setWeight(EnumWeight.MEDIUM);
		this.setSize(EnumSize.MEDIUM);
		metalID = mID;
	}

	@Override
	public boolean onItemUse(ItemStack itemstack, EntityPlayer entityplayer, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ)
	{
		boolean isSuccessful = false;
		if(!world.isRemote)
		{
			TEMetalSheet te = null;
			int[] sides = sidesMap[side];
			if(world.getBlock(x, y, z) == TFCBlocks.MetalSheet)
			{
				te = (TEMetalSheet)world.getTileEntity(x, y, z);
				switch(side)
				{
				case 0:
					if(!te.BottomExists())
					{
						te.toggleBottom(true);
						isSuccessful = true;
						break;
					}
				case 1:
					if(!te.TopExists())
					{
						te.toggleTop(true);
						isSuccessful = true;
						break;
					}
				case 2:
					if(!te.NorthExists())
					{
						te.toggleNorth(true);
						isSuccessful = true;
						break;
					}
				case 3:
					if(!te.SouthExists())
					{
						te.toggleSouth(true);
						isSuccessful = true;
						break;
					}
				case 4:
					if(!te.EastExists())
					{
						te.toggleEast(true);
						isSuccessful = true;
						break;
					}
				case 5:
					if(!te.WestExists())
					{
						te.toggleWest(true);
						isSuccessful = true;
						break;
					}
				}
			}
			else if(isValid(world, sides[0] + x, sides[1] + y, sides[2] + z))
			{
				if(world.getBlock(x, y, z) != TFCBlocks.MetalSheet)
				{
					world.setBlock( sides[0] + x, sides[1] + y, sides[2] + z, TFCBlocks.MetalSheet);
					te = (TEMetalSheet)world.getTileEntity( sides[0] + x, sides[1] + y, sides[2] + z);
					te.metalID = this.metalID;
					te.sheetStack = itemstack.copy();
					te.sheetStack.stackSize = 1;
					te.toggleBySide(flipSide(side), true);
					isSuccessful = true;
				}
			}
			else
			{
				isSuccessful = false;
			}

			if(isSuccessful)
			{
				itemstack.stackSize--;
			}

		}
		return isSuccessful;
	}
	public int flipSide(int side)
	{
		switch(side)
		{
		case 0: return 1;
		case 1: return 0;
		case 2: return 3;
		case 3: return 2;
		case 4: return 5;
		case 5: return 4;
		}
		return 0;
	}

	public boolean isValid(World world, int i, int j, int k)
	{
		Block bid = world.getBlock(i, j, k);
		if(bid == Blocks.air)
			return true;
		if(bid == TFCBlocks.MetalSheet)
		{
			TEMetalSheet te = (TEMetalSheet)world.getTileEntity(i, j, k);
			if(te.metalID == this.metalID)
				return true;
		}
		return false;
	}
	
	@Override
	public void addItemInformation(ItemStack is, EntityPlayer player, List arraylist)
	{
		if(TFC_ItemHeat.HasTemp(is))
		{
			String s = "";
			if(isTemperatureDanger(is))
			{
				s += EnumChatFormatting.WHITE + StatCollector.translateToLocal("gui.ingot.danger") + " | ";
			}

			if(isTemperatureWeldable(is))
			{
				s += EnumChatFormatting.WHITE + StatCollector.translateToLocal("gui.ingot.weldable") + " | ";
			}

			if(isTemperatureWorkable(is))
			{
				s += EnumChatFormatting.WHITE + StatCollector.translateToLocal("gui.ingot.workable");
			}

			if(!s.equals(""))
				arraylist.add(s);
		}
	}

	public Boolean isTemperatureWeldable(ItemStack is)
	{
		HeatRegistry manager = HeatRegistry.getInstance();
		if(TFC_ItemHeat.HasTemp(is))
		{
			HeatIndex index = manager.findMatchingIndex(is);
			if(index != null)
			{
				float temp = TFC_ItemHeat.GetTemp(is);
				return temp < index.meltTemp && temp > index.meltTemp *0.8;
			}
		}
		return false;
	}

	public Boolean isTemperatureWorkable(ItemStack is)
	{
		HeatRegistry manager = HeatRegistry.getInstance();
		if(TFC_ItemHeat.HasTemp(is))
		{
			HeatIndex index = manager.findMatchingIndex(is);
			if(index != null)
			{
				float temp = TFC_ItemHeat.GetTemp(is);
				return temp < index.meltTemp && temp > index.meltTemp * 0.60;
			}
		}
		return false;
	}

	public Boolean isTemperatureDanger(ItemStack is)
	{
		HeatRegistry manager = HeatRegistry.getInstance();
		if(TFC_ItemHeat.HasTemp(is))
		{
			HeatIndex index = manager.findMatchingIndex(is);
			if(index != null)
			{
				float temp = TFC_ItemHeat.GetTemp(is);
				return temp < index.meltTemp && temp > index.meltTemp * 0.90;
			}
		}
		return false;
	}

}
