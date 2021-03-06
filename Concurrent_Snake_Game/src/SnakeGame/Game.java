package SnakeGame;

/**
 * Original Created by https://code.google.com/p/java-snake/source/browse/trunk/java-snake/src/snake/Main.java
 */

import java.util.concurrent.ThreadLocalRandom;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import SnakeGame.Entity.Type;

/**
 *
 * @author Group
 */
public class Game implements KeyListener, WindowListener {
	
	public final static int UP = 0;
	public final static int DOWN = 1;
	public final static int LEFT = 2;
	public final static int RIGHT = 3;

	private int P1_direction = -1;
	private int P1_next_direction = -1;

	private int P2_direction = -1;
	private int P2_next_direction = -1;

	private int P3_direction = -1;
	private int P3_next_direction = -1;

	private int P4_direction = -1;
	private int P4_next_direction = -1;


	private ArrayList<Snake> playerList = new ArrayList<>();
	
	//Array of threads
	//private ArrayList<Thread> playerTList = new ArrayList<>();
	
	private int playerCount = 0;

	private Entity[][] gameBoard = null;
	private int height = 1000;
	private int width = 1000;
	private int gameSize = 80;
	private int totalFood = 5;

	private long speed = 80;
	private Frame frame = null;
	private Canvas canvas = null;
	private Graphics graph = null;
	private BufferStrategy strategy = null;
	private boolean game_over = false;
	private boolean paused = false;

	private long cycleTime = 0;
	private long sleepTime = 0;
	private int bonusTime = 0;
	private boolean running = true;
	
	
	/*
	public static void main(String[] args) {
		Game game = new Game();
		game.init();
		
		//mainloop is now a thread, implemented in run()
		//game.mainLoop();
		Thread theGame = new Thread(game);
		theGame.start();
	}*/
	
	public void createSnake(){
		//playerList holds current players for the gameBoard to see
		playerCount++;
		Snake player = new Snake(this,"Player"+playerCount);
		
		playerList.add(player);
		
		System.out.println("Created new Snake"+player.toString());
	}
	
	/*
	@Override
	public void run() {
		try{
			while (running) {
				cycleTime = System.currentTimeMillis();
				if(!paused && !game_over)
				{
					int count = 0;
					for(Snake i: playerList){
						if(count ==0){
							P1_direction = P1_next_direction;
							moveSnakeNEW(i,P1_direction,P1_next_direction);
						} else if(count == 1){
							P2_direction = P2_next_direction;
							moveSnakeNEW(i,P2_direction,P2_next_direction);
						} else if(count == 2){
							P3_direction = P3_next_direction;
							moveSnakeNEW(i,P3_direction,P3_next_direction);
						} else if(count == 3){
							P4_direction = P4_next_direction;
							moveSnakeNEW(i,P4_direction,P4_next_direction);
						} else {
							//NON CONTROLLABLE PLAYERS
							//TODO: make randomMovement more natural
							
							//randomMovement(i);
						}
						count++;  	
					}
				}
				//System.out.println("Render game is looped");
				renderGameNEW();
				cycleTime = System.currentTimeMillis() - cycleTime;
				sleepTime = speed - cycleTime;
				if (sleepTime < 0)
					sleepTime = 0;
				try {
					Thread.sleep(sleepTime);
				} catch (InterruptedException ex) {
					Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null,
							ex);
				}
			}
		}
		catch(Exception e){
			
		}
		
	}
	*/

	public Entity[][] getGameBoard() {
		return gameBoard;
	}

	public void placeEntity(Entity en,int x,int y){
		gameBoard[x][y] = en;
	}

	public int getGameSize() {
		return gameSize;
	}

	

	public Game() {
		super();
		frame = new Frame();
		canvas = new Canvas();
		
		//Creating new gameboard of entities (Snake, Food, Empty)
		gameBoard = new Entity[gameSize][gameSize]; 

	}

	public void init() {

		frame.setSize(width + 7, height + 27);
		frame.setResizable(false);
		frame.setLocationByPlatform(true);
		canvas.setSize(width + 7, height + 27);
		frame.add(canvas);
		canvas.addKeyListener(this);
		frame.addWindowListener(this);
		frame.dispose();
		frame.validate();
		frame.setTitle("Snake");
		frame.setVisible(true);

		canvas.setIgnoreRepaint(true);
		canvas.setBackground(Color.WHITE);

		canvas.createBufferStrategy(2);

		strategy = canvas.getBufferStrategy();
		graph = strategy.getDrawGraphics();

		initGameNEW();

		renderGameNEW();
	}


	public void mainLoop() {
		while (running) {
			cycleTime = System.currentTimeMillis();
			if(!paused && !game_over)
			{
				int count = 0;
				//First 4 snakes in playerList can be controlled individually
				for(Snake i: playerList){
					if(count ==0){
						//Directions for arrow keys
						P1_direction = P1_next_direction;
						moveSnakeNEW(i,P1_direction,P1_next_direction);
					} else if(count == 1){
						//Directions for WASD
						P2_direction = P2_next_direction;
						moveSnakeNEW(i,P2_direction,P2_next_direction);
					} else if(count == 2){
						//Directions for NUMPAD
						P3_direction = P3_next_direction;
						moveSnakeNEW(i,P3_direction,P3_next_direction);
					} else if(count == 3){
						//Directions for IJKL
						P4_direction = P4_next_direction;
						moveSnakeNEW(i,P4_direction,P4_next_direction);
					} else {
						//NON CONTROLLABLE PLAYERS
						//TODO: make randomMovement more natural
						
						//randomMovement(i);
					}
					count++;  	
				}
			}
			//System.out.println("Render game is looped");
			renderGameNEW();
			cycleTime = System.currentTimeMillis() - cycleTime;
			sleepTime = speed - cycleTime;
			if (sleepTime < 0)
				sleepTime = 0;
			try {
				Thread.sleep(sleepTime);
			} catch (InterruptedException ex) {
				Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null,
						ex);
			}
		}
		
	}

	private void initGameNEW(){
		// Create an empty board
		for(int i = 0; i < gameSize; i++){
			for (int j = 0; j < gameSize; j++) {
				//Filling the gameBoard with entities (Empty spaces)
				gameBoard[i][j] = new Entity(this);
			}
		}
		//createSnake();
		for(int i = 0; i< totalFood;i++){
			//Creating food
			createFood();
		}

	}



	private void renderGameNEW() {
		int gridUnit = height / gameSize;
		canvas.paint(graph);

		do {
			do {
				graph = strategy.getDrawGraphics();
				((Graphics2D)graph).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

				// Draw Background
				graph.setColor(Color.WHITE);
				graph.fillRect(0, 0, width, height);

				// Draw snake, bonus ...

				Entity gridCase = new Entity();
				for (int i = 0; i < gameSize; i++) {
					for (int j = 0; j < gameSize; j++) {
						gridCase = gameBoard[i][j];



						switch (gridCase.getType()){
						case SNAKE :

							graph.setColor(gridCase.getColour());
							graph.fillRect(i * gridUnit, j * gridUnit,
									gridUnit, gridUnit);
							break;

						case FOOD :
							//Implement later
							graph.setColor(Color.CYAN);
							graph.fillOval(i * gridUnit, j * gridUnit,
									gridUnit, gridUnit);
							break;
						default:
							break;
						}
					}
				}
				graph.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 80));

				if (game_over) {
					graph.setColor(Color.RED);
					graph.drawString("GAME OVER", 280, height / 2);
				}
				else if (paused) {
					graph.setColor(Color.BLACK);
					graph.drawString("PAUSED", 350, height / 2);
				}

				//graph.setColor(Color.BLACK);
				//graph.drawString("SCORE = " + getScore(), 10, 20);

				graph.dispose();

			} while (strategy.contentsRestored());
			// Draw image from buffer
			strategy.show();
			Toolkit.getDefaultToolkit().sync();
		} while (strategy.contentsLost());


	}

	
	//TODO: create a scoring system for multiple snakes
	/*
	private int getScore() {
		int score = 0;
		for (int i = 0; i < gameSize * gameSize; i++) {
			if ((snake[i][0] < 0) || (snake[i][1] < 0)) {
				break;
			}
			score++;
		}
		return score;
	}
	*/

	/**this method simulates random movement for the uncontrollable snakes
	 * 
	 * 
	 */
	public void randomMovement(Snake snake){
		
		//TODO: Make movement more natural
		
		int ymove =0;
		int xmove =0;
		int randomNum = ThreadLocalRandom.current().nextInt(0, 3 + 1);
		switch (randomNum) {
		case 0:
			xmove = 0;
			ymove = -1;
			break;
		case 1:
			xmove = 0;
			ymove = 1;
			break;
		case 2:
			xmove = 1;
			ymove = 0;
			break;
		case 3:
			xmove = -1;
			ymove = 0;
			break;
		}

		int tempx = snake.getPosition()[0][0];
		int tempy = snake.getPosition()[0][1];


		int fut_x = snake.getPosition()[0][0] + xmove;
		int fut_y = snake.getPosition()[0][1] + ymove;


		if(fut_x < 0)
			fut_x = gameSize - 1;
		if(fut_y < 0)
			fut_y = gameSize - 1;
		if(fut_x >= gameSize)
			fut_x = 0;
		if(fut_y >= gameSize)
			fut_y = 0;

		//Food pickup
		if(gameBoard[fut_x][fut_y].getType()==Type.FOOD){
			snake.setGrow(snake.getGrow()+1);
			createFood();
		}


		snake.setPositionX(0, fut_x);
		snake.setPositionY(0, fut_y);
		
		//run into self
		/*
		if ((gameBoard[snake.getPosition()[0][0]][snake.getPosition()[0][1]].getType() == Type.SNAKE)) {
			gameOver();
			if(snake.getID().equals(gameBoard[snake.getPosition()[0][0]][snake.getPosition()[0][1]].getID())){
				//ran into itself
				System.out.println("Snake Collision! ("+snake.toString()+") ran into itself!");
			} else {
				//ran into another snake
				System.out.println("Snake Collision! ("+snake.toString()+") ran into ("+ gameBoard[snake.getPosition()[0][0]][snake.getPosition()[0][1]].toString()+")!");
			}
			return;
		}
		*/
		
		gameBoard[tempx][tempy] = new Entity(this);

		int snakex, snakey, i;

		for (i = 1; i < gameSize * gameSize; i++) {
			if ((snake.getPosition()[i][0] < 0) || (snake.getPosition()[i][1] < 0)) {
				break;
			}
			gameBoard[snake.getPosition()[i][0]][snake.getPosition()[i][1]] = new Entity(this);
			snakex = snake.getPosition()[i][0];
			snakey = snake.getPosition()[i][1];
			snake.setPositionX(i, tempx);
			snake.setPositionY(i, tempy);
			tempx = snakex;
			tempy = snakey;
		}

		gameBoard[snake.getPosition()[0][0]][snake.getPosition()[0][1]] = snake;
		for (i = 1; i < gameSize * gameSize; i++) {
			if ((snake.getPosition()[i][0] < 0) || (snake.getPosition()[i][1] < 0)) {
				break;
			}
			gameBoard[snake.getPosition()[i][0]][snake.getPosition()[i][1]] = snake;
		}



		if (snake.getGrow() > 0) {
			snake.incrementSize();
			snake.setPositionX(i, tempx);
			snake.setPositionY(i, tempy);
			gameBoard[snake.getPosition()[i][0]][snake.getPosition()[i][1]] = snake;
			snake.setGrow(snake.getGrow()-1);
		}



	}




	/**
	 * 
	 * @param snake: the snake to move
	 * @param direction current moveing direction
	 * @param next_direction next moving direction
	 */
	private void moveSnakeNEW(Snake snake, int direction, int next_direction) {

		//Implement for 4 players

		//direction = next_direction;

		if(direction < 0) {
			return;
		}

		int ymove =0;
		int xmove =0;
		switch (direction) {
		case UP:
			xmove = 0;
			ymove = -1;
			break;
		case DOWN:
			xmove = 0;
			ymove = 1;
			break;
		case RIGHT:
			xmove = 1;
			ymove = 0;
			break;
		case LEFT:
			xmove = -1;
			ymove = 0;
			break;
		default:
			xmove = 0;
			ymove = 0;
			break;
		}

		//set current head pos of snake
		int tempx = snake.getPosition()[0][0];
		int tempy = snake.getPosition()[0][1];

		//determine future head pos of snake
		int fut_x = snake.getPosition()[0][0] + xmove;
		int fut_y = snake.getPosition()[0][1] + ymove;

		//wrap around
		if(fut_x < 0)
			fut_x = gameSize - 1;
		if(fut_y < 0)
			fut_y = gameSize - 1;
		if(fut_x >= gameSize)
			fut_x = 0;
		if(fut_y >= gameSize)
			fut_y = 0;


		//Food pickup
		if(gameBoard[fut_x][fut_y].getType()==Type.FOOD){
			snake.setGrow(snake.getGrow()+1);
			createFood();
		}


		snake.setPositionX(0, fut_x);
		snake.setPositionY(0, fut_y);
		
		//run into self
		if ((gameBoard[snake.getPosition()[0][0]][snake.getPosition()[0][1]].getType() == Type.SNAKE)) {
			gameOver();
			if(snake.getID().equals(gameBoard[snake.getPosition()[0][0]][snake.getPosition()[0][1]].getID())){
				//ran into itself
				System.out.println("Snake Collision! ("+snake.toString()+") ran into itself!");
			} else {
				//ran into another snake
				System.out.println("Snake Collision! ("+snake.toString()+") ran into ("+ gameBoard[snake.getPosition()[0][0]][snake.getPosition()[0][1]].toString()+")!");
			}
			
			
			return;
		}

		gameBoard[tempx][tempy] = new Entity(this);

		int snakex, snakey, i;

		for (i = 1; i < gameSize * gameSize; i++) {
			if ((snake.getPosition()[i][0] < 0) || (snake.getPosition()[i][1] < 0)) {
				break;
			}
			gameBoard[snake.getPosition()[i][0]][snake.getPosition()[i][1]] = new Entity(this);
			snakex = snake.getPosition()[i][0];
			snakey = snake.getPosition()[i][1];
			snake.setPositionX(i, tempx);
			snake.setPositionY(i, tempy);
			tempx = snakex;
			tempy = snakey;
		}

		gameBoard[snake.getPosition()[0][0]][snake.getPosition()[0][1]] = snake;
		for (i = 1; i < gameSize * gameSize; i++) {
			if ((snake.getPosition()[i][0] < 0) || (snake.getPosition()[i][1] < 0)) {
				break;
			}
			gameBoard[snake.getPosition()[i][0]][snake.getPosition()[i][1]] = snake;
		}



		if (snake.getGrow() > 0) {
			snake.incrementSize();
			snake.setPositionX(i, tempx);
			snake.setPositionY(i, tempy);
			gameBoard[snake.getPosition()[i][0]][snake.getPosition()[i][1]] = snake;
			snake.setGrow(snake.getGrow()-1);
		}
	}


	private void createFood() {
		//Find random empty space on gameBoard and put food there
		int x = (int) (Math.random() * 1000) % gameSize;
		int y = (int) (Math.random() * 1000) % gameSize;
		if (gameBoard[x][y].getType() == Type.EMPTY) {
			gameBoard[x][y] = new Food();
		} else {
			createFood();
		}
	}


	private void gameOver() {
		game_over = true;
	}


	// / IMPLEMENTED FUNCTIONS
	public void keyPressed(KeyEvent ke) {
		int code = ke.getKeyCode();
		Dimension dim;
		//System.out.println("Key pressed" + ke.toString());

		//Unpauses game if game is paused
		paused = false;

		//System.out.println("P1 Direction: "+P1_direction);

		switch (code) {
		//p1 movement
		case KeyEvent.VK_UP:
			if (P1_direction != DOWN) {
				P1_next_direction = UP;
			}
			break;
		case KeyEvent.VK_DOWN:
			if (P1_direction != UP) {
				P1_next_direction = DOWN;
			}
			break;
		case KeyEvent.VK_LEFT:
			if (P1_direction != RIGHT) {
				P1_next_direction = LEFT;
			}
			break;
		case KeyEvent.VK_RIGHT:
			if (P1_direction != LEFT) {
				P1_next_direction = RIGHT;
			}
			break;

			//p2 movement
		case KeyEvent.VK_W:
			if (P2_direction != DOWN) {
				P2_next_direction = UP;
			}break;
		case KeyEvent.VK_S:
			if (P2_direction != UP) {
				P2_next_direction = DOWN;
			}break;
		case KeyEvent.VK_A:
			if (P2_direction != RIGHT) {
				P2_next_direction = LEFT;
			}break;
		case KeyEvent.VK_D:
			if (P2_direction != LEFT) {
				P2_next_direction = RIGHT;
			}break;  

			//p3 movement
		case KeyEvent.VK_NUMPAD8:
			if (P3_direction != DOWN) {
				P3_next_direction = UP;
			}break;
		case KeyEvent.VK_NUMPAD2:
			if (P3_direction != UP) {
				P3_next_direction = DOWN;
			}break;
		case KeyEvent.VK_NUMPAD4:
			if (P3_direction != RIGHT) {
				P3_next_direction = LEFT;
			}break;
		case KeyEvent.VK_NUMPAD6:
			if (P3_direction != LEFT) {
				P3_next_direction = RIGHT;
			}break;	

			//p4 movement
		case KeyEvent.VK_I:
			if (P4_direction != DOWN) {
				P4_next_direction = UP;
			}break;
		case KeyEvent.VK_K:
			if (P4_direction != UP) {
				P4_next_direction = DOWN;
			}break;
		case KeyEvent.VK_J:
			if (P4_direction != RIGHT) {
				P4_next_direction = LEFT;
			}break;
		case KeyEvent.VK_L:
			if (P4_direction != LEFT) {
				P4_next_direction = RIGHT;
			}break;		



		case KeyEvent.VK_ESCAPE:
			running = false;
			System.exit(0);
			break;

		case KeyEvent.VK_SPACE:
			if(!game_over)
				paused = true;
			break;

		case KeyEvent.VK_INSERT:
			if(!game_over){}
			System.out.println("Game-Debug: creating non-cliented snake");
			//Adds a snake to the board at a random position
			//Snake snake = new Snake();
			createSnake();
			break;
		default:
			// Unsupported key
			break;
		}
	}

	public void windowClosing(WindowEvent we) {
		running = false;
		System.exit(0);
	}




	// UNNUSED IMPLEMENTED FUNCTIONS
	public void keyTyped(KeyEvent ke) {}
	public void keyReleased(KeyEvent ke) {}
	public void windowOpened(WindowEvent we) {}
	public void windowClosed(WindowEvent we) {}
	public void windowIconified(WindowEvent we) {}
	public void windowDeiconified(WindowEvent we) {}
	public void windowActivated(WindowEvent we) {}
	public void windowDeactivated(WindowEvent we) {}


}
