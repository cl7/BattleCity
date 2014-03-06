import java.awt.*;
import java.util.Random;

public class GetBlood {
	
	public static final int width = 34;
	public static final int length = 30;

	private int x, y;
	TankClient tc;
	private static Random r = new Random();

	int step = 0; 
	private boolean live = false;

	private static Toolkit tk = Toolkit.getDefaultToolkit();
	private static Image[] bloodImags = null;
	static {
		bloodImags = new Image[] { tk.getImage(CommonWall.class
				.getResource("Images/hp.png")), };
	}

	private int[][] poition = { { 700, 196 }, { 500, 58 }, { 80, 300 },
			{600,321}, { 345, 456 }, { 123, 321 }, { 258, 413 } };

	public void draw(Graphics g) {
		if (r.nextInt(100) > 98) {
			this.live = true;
			move();
		}
		if (!live)
			return;
		g.drawImage(bloodImags[0], x, y, null);

	}

	private void move() {
		step++;
		if (step == poition.length) {
			step = 0;
		}
		x = poition[step][0];
		y = poition[step][1];
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

}
