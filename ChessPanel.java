import java.awt.event.*;
import javax.swing.*;
import java.awt.*;
import java.util.Random;

class ChessPanel extends JPanel implements ActionListener{
    int screenHeight = 576;
    int screenWidth = 576;
    int boardWidth = screenWidth - 8*20;
    int boardHeight = screenHeight - 8*20;
    Color bgColor = new Color(100,100,100);
    Random r;
    Timer t;
    int delay = 10;
    int motionX, motionY = 0;
    int [][] shapeArray = new int[10][2];
    int shapeIndex = 0;
    boolean click = false;
    int [][] piecesCoords = new int[32][2];

    public ChessPanel(){
        r = new Random();
        //t = new Timer(delay, this);
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
		this.setBackground(bgColor);
		this.setFocusable(true);
        this.setVisible(true);
		this.addKeyListener(new MyKeyAdapter());
        this.addMouseListener(new mouse());
        this.addMouseMotionListener(new mouseMotion());

        t = new Timer(delay, this);
        t.start();
        piecesCoords[0][0] = 20;
        piecesCoords[0][1] = 100;
    }

    public void checkClick(int x, int y){
        if ((piecesCoords[0][0] <= x && x <= piecesCoords[0][0]+boardWidth/8) && (piecesCoords[0][1]-10 <= y && y <= piecesCoords[0][1]+boardHeight/8)){
            click = !click;
            repaint();
        }
    }

    public void paintComponent(Graphics g) {
		super.paintComponent(g);
		draw(g);
	}
	
	public void draw (Graphics g) {
        g.setColor(Color.RED);
        g.drawString("(" + motionX + ", " + motionY + ")", 510, 10);
        g.drawString("(" + piecesCoords[0][0] + ", " + piecesCoords[0][1] + ")", 510, 30);

        g.fillRect(motionX - 1, motionY - 1, 3, 3);
        drawBoard(g);

        if (click){
            // g.setColor(Color.GREEN);
            // g.fillRect(motionX-10, motionY-10, 20, 20);
            piecesCoords[0][0] = motionX;
            piecesCoords[0][1] = motionY;
        }
        drawPawn(g, piecesCoords[0][0], piecesCoords[0][1], Color.GREEN);
    }

    public void drawPawn(Graphics g, int x, int y, Color pieceColor){
        g.setColor(pieceColor);
        g.drawString("Pawn", x, y);
    }

    public void drawKnight(Graphics g, int x, int y, Color pieceColor){
        g.setColor(pieceColor);
        g.drawString("Knight", x, y);
    }

    public void drawBishop(Graphics g, int x, int y, Color pieceColor){
        g.setColor(pieceColor);
        g.drawString("Bishop", x, y);
    }

    public void drawRook(Graphics g, int x, int y, Color pieceColor){
        g.setColor(pieceColor);
        g.drawString("Rook", x, y);
    }

    public void drawQueen(Graphics g, int x, int y, Color pieceColor){
        g.setColor(pieceColor);
        g.drawString("Queen", x, y);
    }

    public void drawKing(Graphics g, int x, int y, Color pieceColor){
        g.setColor(pieceColor);
        g.drawString("King", x, y);
    }

    public void setPieces(Graphics g){
        for (int j = 0 ; j < boardHeight/4 ; j += boardHeight/8){

            for (int i = 0; i < boardWidth ; i += 2*boardWidth/8){
                
                
            }
        }
    }

    public void drawBoard (Graphics g){

        Color boardColor1 = new Color(10, 10, 10);
        Color boardColor2 = new Color(180, 90, 90);

        g.setColor(Color.WHITE);
        for (int i = 5; i <= boardWidth + boardWidth/8; i += boardWidth/8){
            g.drawLine(i, 5, i, boardHeight + 5);
            g.drawLine(5,i, boardWidth + 5, i);
        }

        // checkerboard
        for (int j = 0 ; j < boardHeight ; j += boardHeight/8){

            for (int i = 0; i < boardWidth ; i += 2*boardWidth/8){
                
                if (j % (boardHeight/4) == (boardHeight/8)){
                    g.setColor(boardColor1);
                    g.fillRect(i + 5 + 1, 5 + j + 1, boardWidth/8 - 1, boardHeight/8 - 1);
                    g.setColor(boardColor2);
                    g.fillRect(i + boardWidth/8 + 5 + 1, 5 + j + 1, boardWidth/8 - 1, boardHeight/8 - 1);
                }
                else{
                    g.setColor(boardColor2);
                    g.fillRect(i + 5 + 1, 5 + j + 1, boardWidth/8 - 1, boardHeight/8 - 1);
                    g.setColor(boardColor1);
                    g.fillRect(i + boardWidth/8 + 5 + 1, 5 + j + 1, boardWidth/8 - 1, boardHeight/8 - 1);
                }
            }
        }
    }

    public void actionPerformed(ActionEvent e) {
		
	}
	
	public class MyKeyAdapter extends KeyAdapter{
		@Override
		public void keyPressed(KeyEvent k) {
			
			// WASD keys for direction
			switch(k.getKeyCode()) {
                case KeyEvent.VK_A:
                    System.out.println("left");
                    break;
                case KeyEvent.VK_D:
                    System.out.println("right");
                    break;
                case KeyEvent.VK_W:
                    System.out.println("up");
                    break;
                case KeyEvent.VK_S:
                    System.out.println("down");
                    break;
                case KeyEvent.VK_J:
                    System.out.println("delay: ");
                    break;
		    }
	    }
    }

    public class mouse extends MouseAdapter{
        public void mousePressed(MouseEvent e){
            
        }
        public void mouseClicked(MouseEvent e){
            //click = !click;
            checkClick(e.getX(), e.getY());
        }
        public void mouseEntered(MouseEvent e){
            
        }
        public void mouseExited(MouseEvent e){
            
        }
        public void mouseReleased(MouseEvent e){

        }
    }

    public class mouseMotion extends MouseMotionAdapter{
        public void mouseMoved(MouseEvent e){
            motionX = e.getX();
            motionY = e.getY();
            repaint();
        }
    }
}