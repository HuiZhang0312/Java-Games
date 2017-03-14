package flappyBird;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.Timer;

public class Gameplay implements ActionListener, MouseListener, KeyListener{
	
	public static void main(String[] args){
		g = new Gameplay("Flappy Bird", 800, 800);
	}
	
	public static Gameplay g;
	
	private final int width = 800, height = 800;
	private Renderer r;
	private Timer timer;
	
	private Rectangle bird;
	private ArrayList<Rectangle> columns;
	private Random rand;
	private int ticks, yMotion, score = 0, speed = 10;
	private boolean gameOver, started;
	
	public Gameplay(String title, int width, int height){
		JFrame obj = new JFrame();
		obj.setSize(width, height);
		obj.setTitle(title);
		obj.setVisible(true);
		obj.setResizable(false);
		obj.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		obj.addMouseListener(this);
		obj.addKeyListener(this);
		
		r = new Renderer();
		obj.add(r);
		
		rand = new Random();
		bird = new Rectangle(width/2-20,height/2-20,20,20);
		columns = new ArrayList<Rectangle>();
		addColumn(true);
		addColumn(true);
		addColumn(true);
		addColumn(true);
		
		timer = new Timer(20, this);
		timer.start();
	}
	
	public void repaint(Graphics g) {
		//background
		g.setColor(Color.cyan);
		g.fillRect(0, 0, width, height);
		
		//bird
		g.setColor(Color.red);
		g.fillRect(bird.x, bird.y, bird.width, bird.height);
		
		//ground
		g.setColor(Color.orange);
		g.fillRect(0, height-120, width, 120);
		
		//grass
		g.setColor(Color.green);
		g.fillRect(0, height-120, width, 20);	
		
		// columns
		for(Rectangle column:columns){
			paintColumn(g, column);
		}
		
		g.setColor(Color.white);
		g.setFont(new Font("Arial",Font.BOLD,80));
		// game start
		if(!started){
			g.drawString("Click to Start!", 180, height/2-50);
		}
		
		// game over
		if(gameOver){
			g.drawString("Game Over!", 180, height/2-50);
		}
		
		// score
		g.setColor(Color.white);
		g.setFont(new Font("Arial",Font.BOLD,25));
		g.drawString("Score:"+score, 650, 30);
	}

	public void addColumn(boolean start){
		int space = 300;
		int colWidth = 100;
		int colHeight = 50 + rand.nextInt(300);
		
		if(start){
			columns.add(new Rectangle(width+colWidth+columns.size()*300, height-colHeight-120, colWidth,colHeight));
			columns.add(new Rectangle(width+colWidth+(columns.size()-1)*300, 0, colWidth, height-colHeight-space));
		}else{
			columns.add(new Rectangle(columns.get(columns.size()-1).x + 600, height-colHeight-120, colWidth,colHeight));
			columns.add(new Rectangle(columns.get(columns.size()-1).x, 0, colWidth, height-colHeight-space));
		}
		
		
	}
	
	public void paintColumn(Graphics g, Rectangle col){
		g.setColor(Color.green.darker());
		g.fillRect(col.x, col.y, col.width, col.height);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		ticks++;
		
		//int speed = 10;
		
		if(started){
			for(int i = 0; i < columns.size(); i++){
				Rectangle col = columns.get(i);
				col.x -= speed;
			}
			
			if(ticks%2 == 0 && yMotion < 15){
				yMotion += 2;
			}
			
			for(int i = 0; i < columns.size(); i++){
				Rectangle col = columns.get(i);
				if(col.x+col.width < 0){
					columns.remove(col);
					if(col.y == 0){
						addColumn(false);
					}
				}
			}
			
			for(Rectangle col : columns){
				// Actually, score will go up by 2 cause there are 2 columns each time.
				// So need add condition col.y==0 or divide score by 2.
				if(col.y == 0 && bird.x + bird.width/2 == col.x + col.width/2){
					score++;	
				}
				
				if(col.intersects(bird)){
					gameOver = true;
					speed = 0;
					
					bird.x = col.x - bird.width;
				
				}
			}
			
			if(bird.y > height-120 || bird.y < 0){
				gameOver = true;
			}
			
			bird.y += yMotion;
			
			if(bird.y + yMotion >= height-120){
				bird.y = height-bird.height-120;
			}
		}
		
		
		
		r.repaint();
		
	}

	public void jump(){
		if(gameOver){
			bird = new Rectangle(width/2-20,height/2-20,20,20);
			columns.clear();
			yMotion = 0;
			score = 0;
			speed = 10;
			
			addColumn(true);
			addColumn(true);
			addColumn(true);
			addColumn(true);
			
			gameOver = false;
		}
		
		if(!started){
			started = true;
		}else if(!gameOver){
			if(yMotion > 0){
				yMotion = 0;
			}
			yMotion -=10; 
		}
	}
	
	@Override
	public void mouseClicked(MouseEvent arg0) {
		jump();
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_SPACE){
			jump();
		}
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		
	}
}
