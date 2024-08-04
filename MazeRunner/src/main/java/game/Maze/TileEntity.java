package game.Maze;

public class TileEntity {
	
	private Boolean colliderOn;
	private Boolean needPathBkgrd;
	
	public TileEntity(Boolean bColliderOn, Boolean needPathBkgrd)
	{	
		// default collider Off
		this.colliderOn = bColliderOn;
		this.needPathBkgrd = needPathBkgrd;
	}
	
	public Boolean isColliderOn()
	{
		return this.colliderOn;
	}
	public void setColliderOnOff(Boolean bOn)
	{
		this.colliderOn = bOn;
	}
	
	public Boolean isNeedPathBkgrd()
	{
		return this.needPathBkgrd;
	}
}
