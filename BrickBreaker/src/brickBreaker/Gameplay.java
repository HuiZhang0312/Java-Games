package brickBreaker;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.Timer;

import javax.swing.JPanel;

public class Gameplay extends JPanel implements KeyListener, ActionListener{

	private static final long serialVersionUID = -82308012598605919L;
	private boolean play = false;
	private int score = 0;
	
	private int totalBricks = 28;
	
	private Timer timer;
	private int delay = 8;
	
	private int playerX = 300;
	private int ballposX = 120;
	private int ballposY = 350;
	private int ballXdir = -1;
	private int ballYdir = -2;
	
	private MapGenerator map;
	
	public Gameplay(){
		addKeyListener(this);
		setFocusable(true);
		setFocusTraversalKeysEnabled(false);
		timer = new Timer(delay, this);
		timer.start();
		map = new MapGenerator(4,7);
	}
	
	public void paint(Graphics g){
		// Background
		g.setColor(Color.black);
		g.fillRect(1, 1, 692, 592);
		
		// Map
		map.draw((Graphics2D) g);
		
		// Score
		g.setColor(Color.white);
		g.setFont(new Font("serif",Font.BOLD,25));
		g.drawString("Score:"+score, 580, 30);
		
		// Borders
		g.setColor(Color.yellow);
		g.fillRect(0, 0, 3, 592);
		g.fillRect(0, 0, 692, 3);
		g.fillRect(691, 0, 3, 592);
		
		// Paddle
		g.setColor(Color.green);
		g.fillRect(playerX, 550, 100, 8);
		
		// Ball
		g.setColor(Color.yellow);
		g.fillOval(ballposX, ballposY, 20, 20);
		
		if(totalBricks<=0){
			play = false;
			ballXdir = 0;
			ballYdir = 0;
			g.setColor(Color.red);
			g.setFont(new Font("serif",Font.BOLD,30));
			g.drawString("You Won!", 280, 300);
			
			g.setFont(new Font("serif",Font.BOLD,20));
			g.drawString("Press Enter to Restart", 260, 350);
		}
		
		if(ballposY > 570){
			play = false;
			ballXdir = 0;
			ballYdir = 0;
			g.setColor(Color.red);
			g.setFont(new Font("serif",Font.BOLD,30));
			g.drawString("Game Over", 280, 300);
			
			g.setFont(new Font("serif",Font.BOLD,20));
			g.drawString("Press Enter to Restart", 260, 350);
		}
		
		g.dispose();
		
	}
	
	

	@Override
	public void actionPerformed(ActionEvent arg0) {
		timer.start();
		
		if(play){
			if(new Rectangle(ballposX,ballposY,20,20).intersects(new Rectangle(playerX,550,100,8))){
				ballYdir = -ballYdir;
			}
			
			for(int i=0;i<map.map.length;i++){
				for(int j=0;j<map.map[0].length;j++){
					if(map.map[i][j]>0){
						int brickX = j * map.brickWidth + 80;
						int brickY = i * map.brickHeight + 50;
						int brickWidth = map.brickWidth;
						int brickHeight = map.brickHeight;
						
						Rectangle rec = new Rectangle(brickX,brickY,brickWidth,brickHeight);
						Rectangle ballRec = new Rectangle(ballposX,ballposY,20,20);
						Rectangle brickRec = rec;
						
						if(ballRec.intersects(brickRec)){
							map.setBrickValue(0, i, j);
							totalBricks--;
							score += 5;
							
							if(ballposX + 19 <= brickRec.x || ballposX+1 >= brickRec.x + brickRec.width ){
								ballXdir = -ballXdir;
							}else{
								ballYdir = -ballYdir;
							}
							break;
						}
					}
				}
			}
			
			ballposX += ballXdir;
			ballposY += ballYdir;
			if(ballposX < 0){
				ballXdir = -ballXdir;
			}
			if(ballposY < 0){
				ballYdir = -ballYdir;
			}
			if(ballposX > 670){
				ballXdir = -ballXdir;
			}
		}
		
		repaint();
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_RIGHT){
			if(playerX>=590){
				playerX = 590;
			}else{
				moveRight();
			} 
		}
		if(e.getKeyCode() == KeyEvent.VK_LEFT){
			if(playerX<=10){
				playerX = 10;
			}else{
				moveLeft();
			} 
		}
		if(e.getKeyCode() == KeyEvent.VK_ENTER){
			if(!play){
				play = true;
				playerX = 300;
				score = 0;
				totalBricks = 28;
				ballposX = 120;
				ballposY = 350;
				ballXdir = -2;
				ballYdir = -4;
				map = new MapGenerator(4,7);
				repaint();
			}
		}
	}
	
	public void moveRight(){
		play = true;
		playerX += 20;
	}
	
	public void moveLeft(){
		play = true;
		playerX -= 20;
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

}
