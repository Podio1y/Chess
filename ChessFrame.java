import javax.swing.JFrame;

class ChessFrame extends JFrame{

    public ChessFrame(){
        ChessPanel game = new ChessPanel();
		this.add(game);
		this.setTitle("My Chess Game");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		this.pack();
		this.setResizable(false);
		this.setLocationRelativeTo(null);
    }

    public static void main (String[] args){
        ChessFrame chess = new ChessFrame();
    }

};