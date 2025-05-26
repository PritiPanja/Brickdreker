
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;

// ------------------- Ball Class -------------------
class Ball {

    private int x, y;
    private final int diameter = 20;
    private int xSpeed = 2, ySpeed = -2;

    public Ball(int startX, int startY) {
        x = startX;
        y = startY;
    }

    public void move() {
        x += xSpeed;
        y += ySpeed;

        // Bounce off left and right walls
        if (x <= 0 || x >= 800 - diameter) {
            reverseXSpeed();
        }

        // Bounce off top
        if (y <= 0) {
            reverseYSpeed();
        }

        // Game over condition (optional): reset if it hits bottom
        if (y >= 600) {
            x = 400;
            y = 300;
            xSpeed = 2;
            ySpeed = -2;
        }
    }

    public void draw(Graphics g) {
        g.setColor(Color.RED);
        g.fillOval(x, y, diameter, diameter);
    }

    public void reverseYSpeed() {
        ySpeed = -ySpeed;
    }

    public void reverseXSpeed() {
        xSpeed = -xSpeed;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, diameter, diameter);
    }
}

// ------------------- Paddle Class -------------------
class Paddle {

    private int x, y;
    private final int width = 100, height = 15;

    public Paddle(int startX, int startY) {
        x = startX;
        y = startY;
    }

    public void draw(Graphics g) {
        g.setColor(Color.BLUE);
        g.fillRect(x, y, width, height);
    }

    public void move(int direction) {
        x += direction;
        if (x < 0) {
            x = 0;
        }
        if (x > 800 - width) {
            x = 800 - width;
        }
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }
}

// ------------------- Brick Class -------------------
class Brick {

    private final int x, y;
    private final int width = 60, height = 20;
    private boolean visible = true;

    public Brick(int startX, int startY) {
        x = startX;
        y = startY;
    }

    public void draw(Graphics g) {
        if (visible) {
            g.setColor(Color.GREEN);
            g.fillRect(x, y, width, height);
            g.setColor(Color.BLACK);
            g.drawRect(x, y, width, height);
        }
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean v) {
        visible = v;
    }
}

// ------------------- Game Panel Class -------------------
class GamePanel extends JPanel implements ActionListener {

    private final Timer timer;
    private final Ball ball;
    private final Paddle paddle;
    private final ArrayList<Brick> bricks;

    public GamePanel() {
        setFocusable(true);
        setBackground(Color.WHITE);
        ball = new Ball(400, 300);
        paddle = new Paddle(350, 550);
        bricks = new ArrayList<>();

        // Create bricks in 5 rows and 10 columns
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 10; j++) {
                bricks.add(new Brick(80 + j * 70, 50 + i * 30));
            }
        }

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    paddle.move(-15);
                } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    paddle.move(15);
                }
            }
        });

        timer = new Timer(10, this);
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        ball.draw(g);
        paddle.draw(g);
        for (Brick brick : bricks) {
            brick.draw(g);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        ball.move();
        checkCollisions();
        repaint();
    }

    private void checkCollisions() {
        // Ball and paddle
        if (ball.getBounds().intersects(paddle.getBounds())) {
            ball.reverseYSpeed();
        }

        // Ball and bricks
        for (Brick brick : bricks) {
            if (brick.isVisible() && brick.getBounds().intersects(ball.getBounds())) {
                brick.setVisible(false);
                ball.reverseYSpeed();
                break;
            }
        }
    }
}

// ------------------- Main Class (JFrame) -------------------
public class BrickBreaker extends JFrame {

    public BrickBreaker() {
        setTitle("Brick Breaker");
        setSize(800, 600);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        add(new GamePanel());
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void main(String[] args) {
        new BrickBreaker();
    }
}
