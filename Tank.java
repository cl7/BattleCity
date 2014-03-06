import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class Tank {
	public static  int speedX = 6, speedY =6; 
	public static int count = 0;
	public static final int width = 35, length = 35;
	private Direction direction = Direction.STOP;
	private Direction Kdirection = Direction.U; 
	TankClient tc;
	private int player=0;
	private boolean good;
	private int x, y;
	private int oldX, oldY;
	private boolean live = true;
	private int life = 200;
	private int rate=1;
	private static Random r = new Random();
	private int step = r.nextInt(10)+5 ; 

	private boolean bL = false, bU = false, bR = false, bD = false;
	

	private static Toolkit tk = Toolkit.getDefaultToolkit();
	private static Image[] tankImags = null; 
	static {
		tankImags = new Image[] {
				tk.getImage(BombTank.class.getResource("Images/tankD.gif")),
				tk.getImage(BombTank.class.getResource("Images/tankU.gif")),
				tk.getImage(BombTank.class.getResource("Images/tankL.gif")),
				tk.getImage(BombTank.class.getResource("Images/tankR.gif")), 
				tk.getImage(BombTank.class.getResource("Images/HtankD.gif")), 
				tk.getImage(BombTank.class.getResource("Images/HtankU.gif")), 
				tk.getImage(BombTank.class.getResource("Images/HtankL.gif")),
				tk.getImage(BombTank.class.getResource("Images/HtankR.gif")), 
				tk.getImage(BombTank.class.getResource("Images/HtankD2.gif")),
				tk.getImage(BombTank.class.getResource("Images/HtankU2.gif")),
				tk.getImage(BombTank.class.getResource("Images/HtankL2.gif")),
				tk.getImage(BombTank.class.getResource("Images/HtankR2.gif")),
				};

	}

	public Tank(int x, int y, boolean good) {
		this.x = x;
		this.y = y;
		this.oldX = x;
		this.oldY = y;
		this.good = good;
	}

	public Tank(int x, int y, boolean good, Direction dir, TankClient tc, int player) {
		this(x, y, good);
		this.direction = dir;
		this.tc = tc;
		this.player=player;
	}

	public void draw(Graphics g) {
		if (!live) {
			if (!good) {
				tc.tanks.remove(this);
			}
			return;
		}
		//if (good)
			//new DrawBloodbBar().draw(g); 
		switch (Kdirection) {
							
		case D:
			if(player==1){	g.drawImage(tankImags[4], x, y, null);
			}
			else if(tc.Player2&&player==2){
				g.drawImage(tankImags[8], x, y, null);
			}else{
			g.drawImage(tankImags[0], x, y, null);}
			break;

		case U:
			if(player==1){	g.drawImage(tankImags[5], x, y, null);
			}else if(tc.Player2&&player==2){
				g.drawImage(tankImags[9], x, y, null);
			}else{
			g.drawImage(tankImags[1], x, y, null);}
			break;
		case L:if(player==1){	g.drawImage(tankImags[6], x, y, null);
		}else if(tc.Player2&&player==2){
			g.drawImage(tankImags[10], x, y, null);
		}else{
			g.drawImage(tankImags[2], x, y, null);}
			break;

		case R:if(player==1){	g.drawImage(tankImags[7], x, y, null);
		}else if(tc.Player2&&player==2){
			g.drawImage(tankImags[11], x, y, null);
		}else{
			g.drawImage(tankImags[3], x, y, null);}
			break;

		}

		move();   
	}

	void move() {

		this.oldX = x;
		this.oldY = y;

		switch (direction) {  
		case L:
			x -= speedX;
			break;
		case U:
			y -= speedY;
			break;
		case R:
			x += speedX;
			break;
		case D:
			y += speedY;
			break;
		case STOP:
			break;
		}

		if (this.direction != Direction.STOP) {
			this.Kdirection = this.direction;
		}

		if (x < 0)
			x = 0;
		if (y < 40)     
			y = 40;
		if (x + Tank.width > TankClient.Fram_width)  
			x = TankClient.Fram_width - Tank.width;
		if (y + Tank.length > TankClient.Fram_length)
			y = TankClient.Fram_length - Tank.length;

		if (!good) {
			Direction[] directons = Direction.values();
			if (step == 0) {                  
				step = r.nextInt(12) + 3;  
				int mod=r.nextInt(9);
				if (playertankaround()){
					if(x==tc.homeTank.x){ if(y>tc.homeTank.y) direction=directons[1];
					else if (y<tc.homeTank.y) direction=directons[3];
					}else if(y==tc.homeTank.y){ if(x>tc.homeTank.x) direction=directons[0];
					else if (x<tc.homeTank.x) direction=directons[2];
					}
					else{
						int rn = r.nextInt(directons.length);
						direction = directons[rn]; 
					}
					rate=2;
				}else if (mod==1){
					rate=1;
				}else if(1<mod&&mod<=3){
					rate=1;
				}else{
				int rn = r.nextInt(directons.length);
				direction = directons[rn]; 
				rate=1;}    
			}
			step--;
			if(rate==2){
				if (r.nextInt(40) > 35)
					this.fire();
			}else if (r.nextInt(40) > 38)
				this.fire();
		}
	}
	public boolean playertankaround(){
		int rx=x-15,ry=y-15;
		if((x-15)<0) rx=0;
		if((y-15)<0)ry=0;
		Rectangle a=new Rectangle(rx, ry,60,60);
		if (this.live && a.intersects(tc.homeTank.getRect())) {
		return true;	
		}
		return false;	
	}
	public int getzone(int x,int y){
		int tempx=x;
		int tempy=y;
		if (tempx<85&&tempy<300) return 11;
		else if(tempx>85&&tempx<140&&tempy>0&&tempy<100) return 9;
		else if(tempx>85&&tempx<140&&tempy>254&&tempy<300) return 10;
		else if(tempx>0&&tempx<200&&tempy>300&&tempy<715) return 12;
		else if(tempx>140&&tempx<400&&tempy>0&&tempy<150) return 7;
		else if(tempx>140&&tempx<400&&tempy>210&&tempy<300) return 8;
		else if(tempx>400&&tempx<500&&tempy>0&&tempy<300) return 6;
		else if(tempx>500&&tempy>0&&tempy<180) return 5;
		else if(tempx>500&&tempy>180&&tempy<300) return 4;
		else if(tempx>520&&tempx<600&&tempy>3000&&tempy<715) return 2;
		else if(tempx>600&&tempy>300&&tempy<715) return 3;
	return 1;
	}
	public int getdirect(int a,int b){
		if(b==13){
			
		}
		return 4;
	}
	private void changToOldDir() {  
		x = oldX;
		y = oldY;
	}

	public void keyPressed(KeyEvent e) {  
		int key = e.getKeyCode();
		if (player==1){
		switch (key) {
		case KeyEvent.VK_R:  
			tc.tanks.clear(); 
			tc.bullets.clear();
			tc.trees.clear();
			tc.otherWall.clear();
			tc.homeWall.clear();
			tc.metalWall.clear();
			tc.homeTank.setLive(false);
			if (tc.tanks.size() == 0) {        
				for (int i = 0; i < 20; i++) {
					if (i < 9)                             
						tc.tanks.add(new Tank(150 + 70 * i, 40, false,
								Direction.R, tc,0));
					else if (i < 15)
						tc.tanks.add(new Tank(700, 140 + 50 * (i -6), false,
								Direction.D, tc,0));
					else
						tc.tanks.add(new Tank(10,  50 * (i - 12), false,
								Direction.L, tc,0));
				}
			}
			
			tc.homeTank = new Tank(300, 560, true, Direction.STOP, tc,0);
			if (!tc.home.isLive()) 
				tc.home.setLive(true);
			TankClient abc=new TankClient();
			if (tc.Player2) abc.Player2=true;
			break;
		case KeyEvent.VK_D:
			bR = true;
			break;
			
		case KeyEvent.VK_A:
			bL = true;
			break;
		
		case KeyEvent.VK_W:  
			bU = true;
			break;
		
		case KeyEvent.VK_S:
			bD = true;
			break;
		}}
		if (player==2){
		switch(key){
		case KeyEvent.VK_RIGHT:
			bR = true;
			break;
			
		case KeyEvent.VK_LEFT:
			bL = true;
			break;
		
		case KeyEvent.VK_UP: 
			bU = true;
			break;
		
		case KeyEvent.VK_DOWN:
			bD = true;
			break;
		}
		}
		decideDirection();
	}

	void decideDirection() {
		if (!bL && !bU && bR && !bD) 
			direction = Direction.R;

		else if (bL && !bU && !bR && !bD) 
			direction = Direction.L;

		else if (!bL && bU && !bR && !bD) 
			direction = Direction.U;

		else if (!bL && !bU && !bR && bD) 
			direction = Direction.D;

		else if (!bL && !bU && !bR && !bD)
			direction = Direction.STOP; 
	}

	public void keyReleased(KeyEvent e) {  
		int key = e.getKeyCode();
		if (player==1){
		switch (key) {
		
		case KeyEvent.VK_F:
			fire();
			break;
			
		case KeyEvent.VK_D:
			bR = false;
			break;
		
		case KeyEvent.VK_A:
			bL = false;
			break;
		
		case KeyEvent.VK_W:
			bU = false;
			break;
		
		case KeyEvent.VK_S:
			bD = false;
			break;
			

		}}
		if (player==2){
			switch (key) {
			
			case KeyEvent.VK_SLASH:
				fire();
				break;
				
			case KeyEvent.VK_RIGHT:
				bR = false;
				break;
			
			case KeyEvent.VK_LEFT:
				bL = false;
				break;
			
			case KeyEvent.VK_UP:
				bU = false;
				break;
			
			case KeyEvent.VK_DOWN:
				bD = false;
				break;
				

			}}
		decideDirection(); 
	}

	public Bullets fire() { 
		if (!live)
			return null;
		int x = this.x + Tank.width / 2 - Bullets.width / 2; 
		int y = this.y + Tank.length / 2 - Bullets.length / 2;
		Bullets m = new Bullets(x, y + 2, good, Kdirection, this.tc); 
		tc.bullets.add(m);                                                
		return m;
	}


	public Rectangle getRect() {
		return new Rectangle(x, y, width, length);
	}

	public boolean isLive() {
		return live;
	}

	public void setLive(boolean live) {
		this.live = live;
	}

	public boolean isGood() {
		return good;
	}

	public boolean collideWithWall(CommonWall w) {  
		if (this.live && this.getRect().intersects(w.getRect())) {
			 this.changToOldDir();    
			return true;
		}
		return false;
	}

	public boolean collideWithWall(MetalWall w) { 
		if (this.live && this.getRect().intersects(w.getRect())) {
			this.changToOldDir();     
			return true;
		}
		return false;
	}

	public boolean collideRiver(River r) {    
		if (this.live && this.getRect().intersects(r.getRect())) {
			this.changToOldDir();
			return true;
		}
		return false;
	}

	public boolean collideHome(Home h) {  
		if (this.live && this.getRect().intersects(h.getRect())) {
			this.changToOldDir();
			return true;
		}
		return false;
	}

	public boolean collideWithTanks(java.util.List<Tank> tanks) {
		for (int i = 0; i < tanks.size(); i++) {
			Tank t = tanks.get(i);
			if (this != t) {
				if (this.live && t.isLive()
						&& this.getRect().intersects(t.getRect())) {
					this.changToOldDir();
					t.changToOldDir();
					return true;
				}
			}
		}
		return false;
	}


	public int getLife() {
		return life;
	}

	public void setLife(int life) {
		this.life = life;
	}

	private class DrawBloodbBar {
		public void draw(Graphics g) {
			Color c = g.getColor();
			g.setColor(Color.RED);
			g.drawRect(375, 585, width, 10);
			int w = width * life / 200;
			g.fillRect(375, 585, w, 10);
			g.setColor(c);
		}
	}

	public boolean eat(GetBlood b) {
		if (this.live && b.isLive() && this.getRect().intersects(b.getRect())) {
			if(this.life<=100)
			this.life = this.life+100;      
			else
				this.life = 200;
			b.setLive(false);
			return true;
		}
		return false;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
}