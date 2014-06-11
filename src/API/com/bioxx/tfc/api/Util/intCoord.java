package com.bioxx.tfc.api.Util;

public class intCoord
{
	public int x, y, z;

	public intCoord(int x, int y, int z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}

	@Override
	public boolean equals(Object o)
	{
		if(o instanceof intCoord &&
				((intCoord)o).x == x &&
				((intCoord)o).y == y &&
				((intCoord)o).z == z)
		{
			return true;
		}
		return false;
	}
}
