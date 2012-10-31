package jervis.AI.Debug;


import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.swing.*;

import jervis.AI.WaterManager;


@SuppressWarnings("serial")
public class WaterDisplay extends JFrame{
	/**
	 * The default constructor creates the frame with all visual objects.
	 */
	
	public WaterDisplay(){
		// Init frame 
		super("DebugFrame");
		setSize(800,800);
		setVisible(true);
		
		this.getContentPane().add(new MyCanvas());
	}

	WaterManager state = null;
	public void draw(WaterManager waterManager){
		this.state = waterManager;
		this.repaint();
	}
	
	
	
	class MyCanvas extends JComponent {
		@Override
		public void paint(Graphics g) {
			
			if(state == null) return;
			
			float[][] a = new float[60][60];
			
			
			for (int x = 0; x < 60 ; x++) {
				for (int y = 0; y < 60 ; y++) {	
					float tmp = (float)state.getWaterProbability(x,y);
					a[x][y] = tmp;
				}
			}
			
			
			
			float max = 0;
			for (int x = 0; x < 60 ; x++) {
				for (int y = 0; y < 60 ; y++) {
					if(a[x][y] > max)
						max = a[x][y];
				}
			}
			
			if(max==0) max=1;
			
			
			
			/*float sum = 0;
			for (int x = 0; x < 60 ; x++) {
				for (int y = 0; y < 60 ; y++) {	
					a[x][y] = a[x][y]/max;
					sum += a[x][y];
				}
			}*/
			
			for (int x = 0; x < 60 ; x++) {
				for (int y = 0; y < 60 ; y++) {	
					a[x][y] = a[x][y]/max;
				}
			}		
			
			Graphics2D g2 = (Graphics2D) g;
			BufferedImage bi = (BufferedImage) createImage(775,715);
			Graphics2D big = bi.createGraphics();
			for (int x = 0; x < 60 ; x++) {
				for (int y = 0; y < 60 ; y++) {
					float c = a[x][y];
					if(c>1 || Float.isNaN(c))
						big.setColor(Color.red);
					else
						big.setColor(new Color(c,c,c));
					big.fillRect(
							(int)(775.0/60*x),
							(int)(715.0/60*y),
							(int)(775.0/60),
							(int)(715.0/60)
					); 
				}
			}		
			g2.drawImage(bi, 0, 0, this);
			
		}
	}
}
 