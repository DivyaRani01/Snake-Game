import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.LinkedList;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class SnakeGame extends JPanel implements KeyListener {

    // Define constants for the game window size, cell size, and grid size
    private static final int WINDOW_WIDTH = 500;
    private static final int WINDOW_HEIGHT = 500;
    private static final int CELL_SIZE = 20;
    private static final int GRID_WIDTH = WINDOW_WIDTH / CELL_SIZE;
    private static final int GRID_HEIGHT = WINDOW_HEIGHT / CELL_SIZE;

    // Define an enum to represent the different directions the snake can move
    private enum Direction {
        UP, DOWN, LEFT, RIGHT
    }

    // Initialize instance variables for the snake's current direction, head, body, food, and score
    private Direction currentDirection = Direction.RIGHT;
    private Point head;
    private LinkedList<Point> body;
    private Point food;
    private Random random = new Random();
    private int score = 0;

    // Constructor for the SnakeGame class
    public SnakeGame() {
        // Set the preferred size of the game panel to match the window size
        setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
        // Initialize the snake's starting position and body
        head = new Point(GRID_WIDTH / 2, GRID_HEIGHT / 2);
        body = new LinkedList<Point>();
        body.add(new Point(head.x - 1, head.y));
        body.add(new Point(head.x - 2, head.y));
        // Generate the initial food position
        generateFood();
        // Add the key listener and set the panel to be focusable
        addKeyListener(this);
        setFocusable(true);
    }

    // Override the paintComponent method to draw the game graphics
    public void paintComponent(Graphics g) {
        // Call the parent paintComponent method to clear the panel
        super.paintComponent(g);
        // Draw the grid lines
        g.setColor(Color.LIGHT_GRAY);
        for (int x = 0; x <= WINDOW_WIDTH; x += CELL_SIZE) {
            g.drawLine(x, 0, x, WINDOW_HEIGHT);
        }
        for (int y = 0; y <= WINDOW_HEIGHT; y += CELL_SIZE) {
            g.drawLine(0, y, WINDOW_WIDTH, y);
        }
        // Draw the food and the snake
        g.setColor(Color.RED);
        g.fillOval(food.x * CELL_SIZE, food.y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
        g.setColor(Color.GREEN);
        g.fillRect(head.x * CELL_SIZE, head.y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
        for (Point p : body) {
            g.fillRect(p.x * CELL_SIZE, p.y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
        }
        // Draw the score
        g.setColor(Color.BLACK);
        g.drawString("Score: " + score, 10, 20);
    }

    // Move the snake one step
    public void move() {
        // Add the current head position to the front of the body and remove the last tail position
        body.addFirst(new Point(head.x, head.y));
        body.removeLast();
        // Move the head in the current direction
        switch (currentDirection) {
            case UP:
                head.y--;
                break;
            case DOWN:
                head.y++;
                break;
            case LEFT:
                head.x--;
                break;
            case RIGHT:
                head.x++;
        }
        
        // Check if the head has collided with the wall or the body
        if (head.x < 0 || head.x >= GRID_WIDTH || head.y < 0 || head.y >= GRID_HEIGHT || body.contains(head)) {
            // Game over
            System.out.println("Game over! Your score is " + score);
            // Exit the program
            System.exit(0);
        }
        // Check if the head has collided with the food
        if (head.equals(food)) {
            // Increase the score and generate a new food position
            score++;
            generateFood();
            // Add a new tail segment to the body
            body.addLast(new Point(body.getLast().x, body.getLast().y));
        }
        // Repaint the game panel
        repaint();
    }

    // Generate a new random position for the food
    public void generateFood() {
        // Generate a random position that is not already occupied by the snake
        do {
            food = new Point(random.nextInt(GRID_WIDTH), random.nextInt(GRID_HEIGHT));
        } while (head.equals(food) || body.contains(food));
    }

    // Handle key press events
    public void keyPressed(KeyEvent e) {
        // Change the current direction based on the arrow key pressed
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:
                if (currentDirection != Direction.DOWN) {
                    currentDirection = Direction.UP;
                }
                break;
            case KeyEvent.VK_DOWN:
                if (currentDirection != Direction.UP) {
                    currentDirection = Direction.DOWN;
                }
                break;
            case KeyEvent.VK_LEFT:
                if (currentDirection != Direction.RIGHT) {
                    currentDirection = Direction.LEFT;
                }
                break;
            case KeyEvent.VK_RIGHT:
                if (currentDirection != Direction.LEFT) {
                    currentDirection = Direction.RIGHT;
                }
                break;
        }
    }

    // Unused KeyListener methods
    public void keyReleased(KeyEvent e) {}
    public void keyTyped(KeyEvent e) {}

    // Main method to start the game
    public static void main(String[] args) {
        // Create a new JFrame and add the SnakeGame panel to it
        JFrame frame = new JFrame("Snake Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new SnakeGame());
        // Set the window size and show the frame
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        // Create a new Timer to move the snake every 100 milliseconds
        javax.swing.Timer timer = new javax.swing.Timer(100, e -> ((SnakeGame)frame.getContentPane().getComponent(0)).move());
        timer.start();
    }
}

