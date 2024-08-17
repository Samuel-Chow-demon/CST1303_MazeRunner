package game.Player;

import static game.UtilityAndConstant.ConstantAndDefine.INIT_X_SHIFT;
import static game.UtilityAndConstant.ConstantAndDefine.INIT_Y_SHIFT;
import static game.UtilityAndConstant.ConstantAndDefine.PLAYER_DEFAULT_SPEED;
import static game.UtilityAndConstant.ConstantAndDefine.PLAYER_SIZE_XY;
import static game.UtilityAndConstant.ConstantAndDefine.UI_HEIGHT;
import static game.UtilityAndConstant.ConstantAndDefine.UI_WIDTH;
import static game.UtilityAndConstant.ConstantAndDefine.eCONTROL_KEY.eDOWN;
import static game.UtilityAndConstant.ConstantAndDefine.eCONTROL_KEY.eLEFT;
import static game.UtilityAndConstant.ConstantAndDefine.eCONTROL_KEY.eRIGHT;
import static game.UtilityAndConstant.ConstantAndDefine.eCONTROL_KEY.eUP;

import java.awt.Point;
import java.awt.Rectangle;

import game.UtilityAndConstant.ConstantAndDefine.eCONTROL_KEY;
import game.UtilityAndConstant.ConstantAndDefine.ePLAYER;

public class Player {
	
	private String specificName;
	private Point position;
	private Point startPosition;
	private Point finishPosition;
	private Integer speed;
	private ePLAYER id;
	private Integer isWin;
	private Integer isDie;
	private eCONTROL_KEY curControlDir;
	private Rectangle collider;
	
	public Player(ePLAYER id)
	{		
		startPosition = new Point();
		finishPosition = new Point();
		
		// Player boundary
		position = new Point();
		switch (id)
		{
			case eP4:
				this.position.setLocation(INIT_X_SHIFT, UI_HEIGHT - INIT_Y_SHIFT);
				break;
			case eP3:
				this.position.setLocation(UI_WIDTH - INIT_X_SHIFT, UI_HEIGHT - INIT_Y_SHIFT);
				break;
			case eP2:
				this.position.setLocation(UI_WIDTH - INIT_X_SHIFT, INIT_Y_SHIFT);
				break;
			default:
			case eP1:
				this.position.setLocation(INIT_X_SHIFT, INIT_Y_SHIFT);	// pos (50, 50)
				break;
		}
		
		speed = PLAYER_DEFAULT_SPEED;
		this.id = id;
		this.specificName = "";
		
		this.isWin = 0;
		this.isDie = 0;
		this.curControlDir = eDOWN; // default is down
		
		// setup collider size, the whole tile for player is 48 x 48
		// tile edge to left leg or tile edge to right leg around 8 pixel
		// choose from the character left leg to right leg around 32 pixel
		// the foot to neck around 32 pixel from bottom
		collider = new Rectangle();
		collider.x = PLAYER_SIZE_XY / 6;									// coordinate reference from player tile, 48 -> 8 pixel
		collider.y = PLAYER_SIZE_XY / 3;									// 48 - 32;
		collider.width = PLAYER_SIZE_XY - ((PLAYER_SIZE_XY / 6) * 2);		// 32
		collider.height = PLAYER_SIZE_XY - (PLAYER_SIZE_XY / 2);			// 32
		
	}
	public Player(ePLAYER id, String specName, Point initPos, Point finishPos,
					int speed, int isWin, int isDie, int curDir)
	{
		this.startPosition = new Point(initPos.x, initPos.y);
		this.position = new Point(initPos.x, initPos.y);
		
		this.finishPosition = new Point(finishPos.x, finishPos.y);
		
		speed = PLAYER_DEFAULT_SPEED;
		this.id = id;
		this.specificName = specName;
		
		this.speed = speed;
		
		this.isWin = isWin;
		this.isDie = isDie;
		this.curControlDir = eCONTROL_KEY.fromValue(curDir);
	}
	// Copy constructor, deep copy
	public Player(Player other)
	{
		this.id = ePLAYER.fromValue(other.id.ordinal());
		this.specificName = other.specificName;
		this.speed = other.speed;
		this.startPosition = new Point(other.startPosition.x, other.startPosition.y);
		this.position = new Point(other.position.x, other.position.y);
		this.finishPosition = new Point(other.finishPosition.x, other.finishPosition.y);
		this.isWin = other.isWin;
		this.isDie = other.isDie;
		this.curControlDir = eCONTROL_KEY.fromValue(other.curControlDir.ordinal());
	}
	
	public void resetAttributes()
	{
		this.position.setLocation(this.startPosition);
		this.speed = PLAYER_DEFAULT_SPEED;
		this.isWin = 0;
		this.isDie = 0;
		this.curControlDir = eDOWN;
	}
	
	public String getName()
	{
		return this.specificName.isEmpty() ? this.id.toString() : this.specificName;
	}
	
	public ePLAYER getID()
	{
		return this.id;
	}
	
	public int getSpeed()
	{
		return this.speed;
	}
	public void setSpeed(int speed)
	{
		this.speed = speed;
	}
	
	public void setWin()
	{
		this.isWin = 1;
	}
	public int getIsWin()
	{
		return this.isWin;
	}
	
	public void setDie()
	{
		this.isDie = 1;
	}
	public int getIsDie()
	{
		return this.isDie;
	}
	public Rectangle getCollider()
	{
		return this.collider;
	}
	
	public Boolean moveUp()
	{
		// Boundary Condition
		if (this.position.getLocation().y - speed >= 0)
		{
			this.position.setLocation(this.position.getLocation().x,
								  		this.position.getLocation().y - speed);
			return false;
		}
		return true;
	}
	public Boolean moveDown()
	{
		// Boundary Condition
		if (this.position.getLocation().y + speed <= (UI_HEIGHT - PLAYER_SIZE_XY))
		{
			this.position.setLocation(this.position.getLocation().x,
									  this.position.getLocation().y + speed);
			return false;
		}
		return true;
	}
	public Boolean moveLeft()
	{
		// Boundary Condition
		if (this.position.getLocation().x - speed >= 0)
		{
			this.position.setLocation(this.position.getLocation().x - speed,
									  this.position.getLocation().y);
			return false;
		}
		return true;
	}
	public Boolean moveRight()
	{
		// Boundary Condition
		if (this.position.getLocation().x + speed <= (UI_WIDTH - PLAYER_SIZE_XY))
		{
			this.position.setLocation(this.position.getLocation().x + speed,
									  this.position.getLocation().y);
			return false;
		}
		return true;
	}
	
	public void setStartPos(Point pos)
	{
		this.startPosition = new Point(pos.x, pos.y);
		this.position = pos;
	}
	
	public void setX(int x)
	{
		this.position.setLocation(x, this.position.getY());
	}
	
	public void setY(int x)
	{
		this.position.setLocation(x, this.position.getY());
	}
	
	public int getX()
	{
		return this.position.getLocation().x;
	}
	public int getY()
	{
		return this.position.getLocation().y;
	}
	
	public Point getPos()
	{
		return this.position.getLocation();
	}
	
	public void setFinishPos(Point pt)
	{
		this.finishPosition = new Point(pt.x, pt.y);
	}
	public Point getFinishPos()
	{
		return this.finishPosition;
	}
	
	public void setDirection(eCONTROL_KEY eDir)
	{
		this.curControlDir = eDir;
	}
	public eCONTROL_KEY getCurDir()
	{
		return this.curControlDir;
	}
}
