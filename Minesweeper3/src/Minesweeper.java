import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;


public class Minesweeper extends JFrame{
	private static final long serialVersionUID = 1L;
	public Mine[][] mineArray =  new Mine[10][10];
	public int markedMines =0;
	public JPanel basePanel, controlPanel, gamePanel;
	public JLabel lblBomb;
	public JTextField tfTime;
	public JButton newGame;
	public JFrame theGame;
	public int timeElapsed;
	ImageIcon newGameIcon;
	public volatile boolean timeIsRunning=false;
	
	private ImageIcon pngFlag = new ImageIcon("img/flag.png"); 
    Image img1 = pngFlag.getImage();
    public Image newimgFlag = img1.getScaledInstance( 30, 30,  java.awt.Image.SCALE_SMOOTH ) ;
 
    private ImageIcon pngBomb = new ImageIcon("img/bomb.png"); 
    Image img2 = pngBomb.getImage();
    public Image newimgBomb = img2.getScaledInstance( 30, 30,  java.awt.Image.SCALE_SMOOTH ) ;

    private ImageIcon pngNew = new ImageIcon("img/new.jpg"); 
    Image img3 = pngNew.getImage();
    public Image newimgNew = img3.getScaledInstance( 30, 30,  java.awt.Image.SCALE_SMOOTH ) ;
   
    public ListenForMouse listenerForMouse = new ListenForMouse();
    public Thread tm; 
    
	public static void main(String[] args) {
		
		new Minesweeper(450, 500);
	
	}//main vége
	
	public void showTheTable(){
    	for(int i=0;i<10;i++){
        	for(int j=0;j<10;j++){
        		if(mineArray[i][j].isBomb) {
        			mineArray[i][j].setIcon(new ImageIcon(newimgBomb));
        			} else {
        					mineArray[i][j].setText("");
        					}
        		mineArray[i][j].removeMouseListener(listenerForMouse);
        	}
        }
    }	
	
	
	
	public void newTable(){
		
			markedMines=0;
			lblBomb.setText("Megvan: "+markedMines);
	    	for(int i=0;i<10;i++){
	        	for(int j=0;j<10;j++){
	        		mineArray[i][j].setDiscovered(false);
	        		mineArray[i][j].isBomb=false;
	        		mineArray[i][j].nrOfBombs=0;
	        		mineArray[i][j].setContentAreaFilled(true);
	        		mineArray[i][j].setEnabled(true);
	        		mineArray[i][j].setIcon(null);
	        		mineArray[i][j].setText("");
	        		for (MouseListener oneListener:mineArray[i][j].getMouseListeners()){
	        			mineArray[i][j].removeMouseListener(oneListener);
	        		}
	        		mineArray[i][j].addMouseListener(listenerForMouse);
	        	}
	        }
	        
	    	int dbBomba=0;
	        do{
	        	int xRan = (int)(Math.random()*9);
	        	int yRan = (int)(Math.random()*9);
	        	if (!(mineArray[xRan][yRan].isBomb)){
	        		mineArray[xRan][yRan].isBomb=true;
	        		mineArray[xRan][yRan].nrOfBombs=0;
	        		//mineArray[xRan][yRan].setToolTipText("Ez BOMBA!!!");
	        		dbBomba++;
	        		mineArray[xRan][yRan].neighborNotification();
	        	}
	        }while(dbBomba<10);
	        
	        timeElapsed=0;
	        timeIsRunning=true;
	        System.out.println(timeElapsed);
	        System.out.println(timeIsRunning);
	    }
	   
	public void thisIsTheEnd(){
		int discoveredFiels=0;

		for(int i=0;i<10;i++){ 
			for(int j=0;j<10;j++){
				if(mineArray[i][j].getDiscovered()){
					discoveredFiels++;
				}
			}
		}

		if(discoveredFiels==90){
			timeIsRunning=false;
			JOptionPane.showMessageDialog(theGame, "Ügyes vagy!", "Nyertél", JOptionPane.INFORMATION_MESSAGE);
		}	
	}
	
	
	public Minesweeper(int w, int h) {{
		   	theGame = new JFrame();
	        theGame.setSize(w,h);
	        theGame.setLocationRelativeTo(null);
	        theGame.setResizable(false);
	        theGame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        theGame.setTitle("Aknakereso");
	        
		   
	        basePanel = new JPanel();
	        basePanel.setLayout(new BorderLayout());
	           
	        controlPanel = new JPanel();
	        basePanel.add(controlPanel,BorderLayout.PAGE_START);
	        //kontrolPanel.setLayout(new BoxLayout(kontrolPanel, BoxLayout.Y_AXIS));
	        controlPanel.setSize(new Dimension(50,50));
	        
	        tfTime = new JTextField(5);
	        tfTime.setEditable(false);
	        controlPanel.add(tfTime);
	        
	        newGame =new JButton();
	        newGame.setPreferredSize(new Dimension(34,34));
	        newGame.addMouseListener(listenerForMouse);
	        newGame.setIcon(new ImageIcon(newimgNew));
	        controlPanel.add(newGame);
	                
	        lblBomb = new JLabel("Megvan: 0"); 
	        controlPanel.add(lblBomb);
	        
	        gamePanel = new JPanel();
	        gamePanel.setLayout(new GridLayout(10,10,0,0));
	        basePanel.add(gamePanel,BorderLayout.CENTER);
	        

	        for(int i=0;i<10;i++){
	        	for(int j=0;j<10;j++){
	        		mineArray[i][j] = new Mine(this, i, j);
	        		gamePanel.add(mineArray[i][j]);
	        		
	        	}
	        }
	        newTable();
	        tm = new timeMeasuring();
	        tm.start();
	        theGame.add(basePanel);
	        theGame.setVisible(true);

	   }
	}
	


	private class ListenForMouse implements MouseListener{
		public void mouseClicked(MouseEvent e) {
//			System.out.println("Klikk!");
			if(e.getSource().getClass().toString().equals("class Mine")){
				if(e.getButton()==3 && !((Mine)e.getSource()).getDiscovered()){
 
					((Mine)e.getSource()).markedBomb();

				} else{
				
					if(e.getClickCount()==2){
						((Mine)e.getSource()).discoverUnmarked(((Mine)e.getSource()).xPos, ((Mine)e.getSource()).yPos);
						((Mine)e.getSource()).removeMouseListener(listenerForMouse);
					} else{
						((Mine)e.getSource()).discover();
					}
					//System.out.println(e.getSource().getClass().toString());
					
				}
			} else if (e.getSource()== newGame) {
				newTable();
			}	
		}
		public void mouseEntered(MouseEvent arg0) {}
		public void mouseExited(MouseEvent arg0) {}
		public void mousePressed(MouseEvent arg0) {}
		public void mouseReleased(MouseEvent arg0) {}

	}//listenForMouse	

	private class timeMeasuring extends Thread{
		
		
		public void run() {
			while(true){
				if(timeIsRunning){
					timeElapsed++;


					tfTime.setText(Integer.toString(timeElapsed));
					//tfTime.pack();
					//tfTime.repaint();
					controlPanel.validate();
					controlPanel.repaint();

					//lblTime.p
					System.out.println(timeElapsed);
				}
				
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {}
				
			}
		}
	}
}//end CLASS
