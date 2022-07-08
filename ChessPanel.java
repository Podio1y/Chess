import java.awt.event.*;
import javax.swing.*;
import java.awt.*;
import java.util.Random;
import java.lang.Math;

class ChessPanel extends JPanel implements ActionListener{
    int screenHeight = 576;
    int screenWidth = 576;
    int boardWidth = screenWidth - 8*20;
    int boardHeight = screenHeight - 8*20;
    Color bgColor = new Color(100,100,100);
    Color piece1Color = new Color (220, 200, 200);
    Color piece2Color = new Color (150, 150, 210);
    Random r;
    Timer t;
    int delay = 10;
    int motionX, motionY = 0;
    int [][] shapeArray = new int[10][2];
    int shapeIndex = 0;
    boolean click = false;
    int [][] piecesCoords = new int[32][2];
    boolean [] piecesMovement = new boolean[32];
    boolean movementActive = false;
    char [][] board = new char[8][8];
    int [] piecesCoordsArrayX = new int[32];
    int [] piecesCoordsArrayY = new int[32];

    int [][] piecesLogX = new int[2][100];
    int [][] piecesLogY = new int[2][100];
    char [][] piecesLogChar = new char[2][100];

    char selectedPiece = '.';
    int xSelectCoord = 0;
    int ySelectCoord = 0;
    

    public ChessPanel(){
        r = new Random();

        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
		this.setBackground(bgColor);
		this.setFocusable(true);
        this.setVisible(true);
		this.addKeyListener(new MyKeyAdapter());
        this.addMouseListener(new mouse());
        this.addMouseMotionListener(new mouseMotion());

        t = new Timer(delay, this);
        t.start();
        setPieces();

        for (int i = 0 ; i < 8 ; i++){
            for (int j = 0 ; j < 8 ; j++){
                board[i][j] = '.';
            }
        }
        for (int i = 0 ; i < 100 ; i++){
            piecesLogX[0][i] = -1;
            piecesLogX[1][i] = -1;
            piecesLogY[0][i] = -1;
            piecesLogY[1][i] = -1;
            piecesLogChar[0][i] = '0';
            piecesLogChar[1][i] = '0';
        }
        setBoardArray();
    }

    public void setBoardArray(){
        board[0][0] = 'R';
        piecesCoordsArrayX[0] = 0;
        piecesCoordsArrayY[0] = 0;

        board[0][7] = 'R';
        piecesCoordsArrayX[7] = 0;
        piecesCoordsArrayY[7] = 7;

        board[0][1] = 'N';
        piecesCoordsArrayX[1] = 0;
        piecesCoordsArrayY[1] = 1;

        board[0][6] = 'N';
        piecesCoordsArrayX[6] = 0;
        piecesCoordsArrayY[6] = 6;

        board[0][2] = 'B';
        piecesCoordsArrayX[2] = 0;
        piecesCoordsArrayY[2] = 2;

        board[0][5] = 'B';
        piecesCoordsArrayX[5] = 0;
        piecesCoordsArrayY[5] = 5;

        board[0][3] = 'Q';
        piecesCoordsArrayX[3] = 0;
        piecesCoordsArrayY[3] = 3;

        board[0][4] = 'K';
        piecesCoordsArrayX[4] = 0;
        piecesCoordsArrayY[4] = 4;

        for (int i = 0 ; i < 8 ; i++){
            board[1][i] = 'P';
            piecesCoordsArrayX[8 + i] = 1;
            piecesCoordsArrayY[8 + i] = i;
        }

        board[7][0] = 'r';
        board[7][7] = 'r';

        board[7][1] = 'n';
        board[7][6] = 'n';

        board[7][2] = 'b';
        board[7][5] = 'b';

        board[7][3] = 'q';
        board[7][4] = 'k';

        piecesCoordsArrayX[0 + 24] = 7;
        piecesCoordsArrayY[0 + 24] = 0;

        piecesCoordsArrayX[7 + 24] = 7;
        piecesCoordsArrayY[7 + 24] = 7;

        piecesCoordsArrayX[1 + 24] = 7;
        piecesCoordsArrayY[1 + 24] = 1;

        piecesCoordsArrayX[6 + 24] = 7;
        piecesCoordsArrayY[6 + 24] = 6;

        piecesCoordsArrayX[2 + 24] = 7;
        piecesCoordsArrayY[2 + 24] = 2;

        piecesCoordsArrayX[5 + 24] = 7;
        piecesCoordsArrayY[5 + 24] = 5;

        piecesCoordsArrayX[3 + 24] = 7;
        piecesCoordsArrayY[3 + 24] = 3;

        piecesCoordsArrayX[4 + 24] = 7;
        piecesCoordsArrayY[4 + 24] = 4;

        for (int i = 0 ; i < 8 ; i++){
            board[6][i] = 'p';
            piecesCoordsArrayX[16 + i] = 6;
            piecesCoordsArrayY[16 + i] = i;
        }
    }

    public void setPieces(){
        int pieceIndex = 0;
        for (int i = 0 ; i < 8 ; i++){

            for (int j = 0 ; j < 8 ; j++){
                piecesCoords[pieceIndex][0] = 10 + j*(boardWidth/8);
                piecesCoords[pieceIndex][1] = 25 + i*(boardHeight/8);
                pieceIndex++;
            }
            if (i == 1){
                i = 5;
            }
        }
    }

    public int pieceInMovement(){
        for (int i = 0 ; i < 32 ; i++){
            if (piecesMovement[i]){
                return i;
            }
        }
        return -1;
    }

    public int coordsToIndex(int unitSize, int coord){
        return (coord - (coord % (unitSize/8)))/(unitSize/8);
    }

    public void checkClick(int x, int y){

        //System.out.println(x % (boardWidth/8));
        int xIndex = (x - (x % (boardWidth/8)))/(boardWidth/8);
        int yIndex = (y - (y % (boardHeight/8)))/(boardHeight/8);
        // System.out.println(x + "," + xIndex + "," + (x%(boardWidth/8)) + "   " + y + "," + yIndex + "," + (y%(boardHeight/8)));

        // If the tile is taken, and you ARE NOT moving something currently
        // Else if the tile is open, and you ARE moving something currently

        // System.out.println(openTile(snapCoordsX(x), snapCoordsY(y), pieceInMovement()));
        // System.out.println(movementActive);
        if (!openTile(snapCoordsX(x), snapCoordsY(y), pieceInMovement()) && !movementActive){
            // Select the piece and move it
           
            for (int i = 0 ; i < 32 ; i++){

                if ( (snapCoordsX(piecesCoords[i][0]) == snapCoordsX(x)) && (snapCoordsY(piecesCoords[i][1]) == snapCoordsY(y)) ){
                    movementActive = true;
                    piecesMovement[i] = true;

                    selectedPiece = pieceFromIndex(pieceInMovement());
                    xSelectCoord = coordsToIndex(boardWidth, piecesCoords[i][1]);
                    ySelectCoord = coordsToIndex(boardWidth, piecesCoords[i][0]);

                    //logSelection(coordsToIndex(boardWidth, piecesCoords[i][1]), coordsToIndex(boardWidth, piecesCoords[i][0]), board[coordsToIndex(boardWidth, piecesCoords[i][1])][coordsToIndex(boardWidth, piecesCoords[i][0])]);
                    board[xSelectCoord][ySelectCoord] = '.';
                }
            }
        }
        else if (openTile(snapCoordsX(x), snapCoordsY(y), pieceInMovement()) && movementActive){ 
            // Place the tile
            for (int i = 0 ; i < 32 ; i++){

                if ( (snapCoordsX(piecesCoords[i][0]) == snapCoordsX(x)) && (snapCoordsY(piecesCoords[i][1]) == snapCoordsY(y)) ){

                    System.out.println(board[6][2] + " " + coordsToIndex(boardWidth, piecesCoords[i][1]) + " " + coordsToIndex(boardWidth, piecesCoords[i][0]));
                    
                    if (!validMove(xSelectCoord, ySelectCoord, coordsToIndex(boardWidth, piecesCoords[i][1]), coordsToIndex(boardWidth, piecesCoords[i][0]), selectedPiece)){
                        break;
                    }

                    piecesMovement[i] = false;
                    movementActive = false;
                    piecesCoords[i][0] = snapCoordsX(piecesCoords[i][0]);
                    piecesCoords[i][1] = snapCoordsY(piecesCoords[i][1]);

                    board[coordsToIndex(boardWidth, piecesCoords[i][1])][coordsToIndex(boardWidth, piecesCoords[i][0])] = pieceFromIndex(i);
                    //logMove();
                }
            }
        }
        repaint();
        //drawBoard();
        drawTextBoard();
    }

    // public void logSelection(int xIndex, int yIndex, char piece){

    //     if (Character.isUpperCase(piece)){

    //         for (int i = 0 ; i < 100 ; i++){

    //             if (piecesLogX[1][i] == -1){
    //                 piecesLogX[1][i] = xIndex;
    //                 piecesLogY[1][i] = yIndex;
    //                 piecesLogChar[1][i] = piece;
    //                 break;
    //             }
    //         }
    //     }
    //     else{

    //         for (int i = 0 ; i < 100 ; i++){

    //             if (piecesLogX[0][i] == -1){
    //                 piecesLogX[0][i] = xIndex;
    //                 piecesLogY[0][i] = yIndex;
    //                 piecesLogChar[0][i] = piece;
    //                 break;
    //             }
    //         }
    //     }
    //     printLogSelection(piecesLogX, piecesLogY, piecesLogChar);
    //     //print2DCharArray(piecesLogChar);
    // }

    // public void printLogSelection(int[][] piecesLogX, int [][] piecesLogY, char [][] piecesLogChar){
    //     for (int i = 0 ; i < 100 ; i++){
    //         if (piecesLogX[0][i] == -1){
    //             break;
    //         }
    //         System.out.println("selection 1 white: " + piecesLogX[0][i] + " " + piecesLogY[0][i]);
            
    //         if (piecesLogY[1][i] == -1){
    //             break;
    //         }
    //         System.out.println("selection 1 black: " + piecesLogX[1][i] + " " + piecesLogY[1][i]);
    //     }   
    // }

    boolean validMove(int xStartIndex, int yStartIndex, int xMoveIndex, int yMoveIndex, char pieceType){
        int xDisplacement = xStartIndex - xMoveIndex;
        int yDisplacement = yStartIndex - yMoveIndex;
        System.out.println(xStartIndex + " " + xMoveIndex + " " + yStartIndex + " " + yMoveIndex + " " + pieceType);

        // Boundary checks
        if ((xStartIndex < 0 || xMoveIndex > 7) || (yStartIndex < 0 || yMoveIndex > 7)){
            return false;
        }

        if (pieceType == 'p' || pieceType == 'P'){
            return pawnCheck(xDisplacement, yDisplacement, xMoveIndex, yMoveIndex, pieceType);
        }

        return true;
    }

    // Verifies if a pawn move is valid, does not support en passant as of yet, maybe will implement once done
    public boolean pawnCheck(int xDisplacement, int yDisplacement, int xMoveIndex, int yMoveIndex, char pieceType){
        // Piece checks
        if (pieceType == 'p'){
            if (xMoveIndex + xDisplacement == 6){
                if (xDisplacement <= 2 && xDisplacement >= 1 && yDisplacement == 0){

                    if (board[xMoveIndex][yMoveIndex] == '.'){
                        System.out.println("TRUE");
                        return true;
                    }
                    else{
                        System.out.println("FALSE");
                        return false;
                    }
                }
                else {
                    return false;
                }
            }
            else if (xDisplacement == 1 && yDisplacement == 0){

                if (board[xMoveIndex][yMoveIndex] == '.'){
                    System.out.println("TRUE");
                    return true;
                }
                else{
                    System.out.println("FALSE");
                    return false;
                }
            }
            else {
                return false;
            }
        }
        else if (pieceType == 'P'){
            if (xMoveIndex + xDisplacement == 1){
                if (xDisplacement >= -2 && xDisplacement <= -1 && yDisplacement == 0){

                    if (board[xMoveIndex][yMoveIndex] == '.'){
                        System.out.println("TRUE");
                        return true;
                    }
                    else{
                        System.out.println("FALSE");
                        return false;
                    }
                }
                else {
                    return false;
                }
            }
            else if (xDisplacement == -1 && yDisplacement == 0){

                if (board[xMoveIndex][yMoveIndex] == '.'){
                    System.out.println("TRUE");
                    return true;
                }
                else{
                    System.out.println("FALSE");
                    return false;
                }
            }
            else {
                return false;
            }
        }
        return false;
    }

    public void drawTextBoard(){
        for (int i = 0 ; i < 8 ; i++){

            for (int j = 0 ; j < 8 ; j++){

                if (j == 7){
                    System.out.println(board[i][j]);
                    //System.out.println("  i" + i + " j" + j);
                }
                else{
                    System.out.print(board[i][j] + " ");
                    //System.out.print("  i" + i + " j" + j);
                }
            }
        }
        System.out.println("-------------------------------");
    }

    char pieceFromIndex(int index){
        if (index == 0 || index == 7){
            return 'R';
        }
        else if (index == 1 || index == 6){
            return 'N';
        }
        else if (index == 2 || index == 5){
            return 'B';
        }
        else if (index == 4){
            return 'K';
        }
        else if (index == 3){
            return 'Q';
        }
        else if (index >= 8 && index < 16){
            return 'P';
        }

        if (index == 0 + 24 || index == 7 + 24){
            return 'r';
        }
        else if (index == 1 + 24 || index == 6 + 24){
            return 'n';
        }
        else if (index == 2 + 24 || index == 5 + 24){
            return 'b';
        }
        else if (index == 4 + 24){
            return 'k';
        }
        else if (index == 3 + 24){
            return 'q';
        }
        else if (index >= 16 && index < 24){
            return 'p';
        }
        return '.';
    }

    boolean isPresentBool(int size, boolean [] array, boolean value){
        for (int i = 0 ; i < size ; i++){
            if (value == array[i]){
                return true;
            }
        }
        return false;
    }

    public void testingBoardOverlay(Graphics g){
        
        for (int i = 0 ; i < 8 ; i++){

            for (int j = 0 ; j < 8 ; j++){
                Color c = new Color(200, 200, i*j*4);
                g.setColor(c);
                g.drawRect(5 + j*boardWidth/8, 5 + i*boardHeight/8, boardWidth/8-1, boardHeight/8-1);
            }
        }
    }

    boolean openTile(int x, int y, int avoid){

        for (int i = 0 ; i < 32 ; i++){
            if (i != avoid){

                if ( (snapCoordsX(piecesCoords[i][0]) == x) && (snapCoordsY(piecesCoords[i][1]) == y) ){
                    return false;
                }
            }
        }
        return true;
    }

    public int snapCoordsX(int x){
        for (int i = 0 ; i < boardWidth ; i += boardWidth/8){
            if (i <= x && x <= i + boardWidth/8){
                return i + 10;
            }
        }
        return 0;
    }

    public int snapCoordsY(int y){
        for (int i = 0 ; i < boardHeight ; i += boardHeight/8){
            if (i <= y && y <= i + boardHeight/8){
                return i + 10 + 15; // +15 because y-offsett for text
            }
        }
        return 0;
    }

    public void paintComponent(Graphics g) {
		super.paintComponent(g);
		draw(g);
	}
	
	public void draw (Graphics g) {

        drawPieceCoords(g);

        g.fillRect(motionX - 1, motionY - 1, 3, 3);
        drawBoard(g);
        testingBoardOverlay(g);

        for (int i = 0 ; i < 32 ; i++){
            if (piecesMovement[i]){
                // g.setColor(Color.GREEN);
                // g.fillRect(motionX-10, motionY-10, 20, 20);
                piecesCoords[i][0] = motionX;
                piecesCoords[i][1] = motionY;
            }
        }

        drawPieces(g);
        
    }

    public void drawPieceCoords(Graphics g){
        g.setColor(Color.RED);
        g.drawString("(" + motionX + ", " + motionY + ")", 510, 10);
        for (int i = 0 ; i < 32 ; i++){
            g.drawString("(" + piecesCoords[i][0] + ", " + piecesCoords[i][1] + ")", 510, 30 + i*15);
        }
    }

    public void drawPieces(Graphics g){

        for (int i = 8 ; i < 16 ; i++){
            drawPawn(g, piecesCoords[i][0], piecesCoords[i][1], piece1Color);
        }
        
        drawRook(g, piecesCoords[0][0], piecesCoords[0][1], piece1Color);
        drawKnight(g, piecesCoords[1][0], piecesCoords[1][1], piece1Color);
        drawBishop(g, piecesCoords[2][0], piecesCoords[2][1], piece1Color);
        drawQueen(g, piecesCoords[3][0], piecesCoords[3][1], piece1Color);
        drawKing(g, piecesCoords[4][0], piecesCoords[4][1], piece1Color);
        drawBishop(g, piecesCoords[5][0], piecesCoords[5][1], piece1Color);
        drawKnight(g, piecesCoords[6][0], piecesCoords[6][1], piece1Color);
        drawRook(g, piecesCoords[7][0], piecesCoords[7][1], piece1Color);

        for (int i = 16 ; i < 24 ; i++){
            drawPawn(g, piecesCoords[i][0], piecesCoords[i][1], piece2Color);
        }

        drawRook(g, piecesCoords[24][0], piecesCoords[24][1], piece2Color);
        drawKnight(g, piecesCoords[25][0], piecesCoords[25][1], piece2Color);
        drawBishop(g, piecesCoords[26][0], piecesCoords[26][1], piece2Color);
        drawQueen(g, piecesCoords[27][0], piecesCoords[27][1], piece2Color);
        drawKing(g, piecesCoords[28][0], piecesCoords[28][1], piece2Color);
        drawBishop(g, piecesCoords[29][0], piecesCoords[29][1], piece2Color);
        drawKnight(g, piecesCoords[30][0], piecesCoords[30][1], piece2Color);
        drawRook(g, piecesCoords[31][0], piecesCoords[31][1], piece2Color);

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
		    }
	    }
    }

    public class mouse extends MouseAdapter{
        public void mousePressed(MouseEvent e){
            
        }
        public void mouseClicked(MouseEvent e){
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