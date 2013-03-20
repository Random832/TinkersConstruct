package mods.tinker.tconstruct;

import java.io.File;

/**
 * Common proxy class for InfiTools
 */

public class TProxyCommon
{
	/* Registers any rendering code. Does nothing server-side */
	public void registerRenderer() {}
	
	/* Ties an internal name to a visible one. Does nothing server-side */
	public void addNames() {}
	
	public void readManuals() {}
	
	public void registerKeys() {}
	
	public File getLocation()
	{
		return new File(".");
	}

	public void spawnParticle (String slimeParticle, double xPos, double yPos, double zPos, double velX, double velY, double velZ) {}
}
