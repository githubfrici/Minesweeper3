import java.awt.Color;

import javax.swing.*;


public class Mine extends JButton{
   
	private static final long serialVersionUID =1L;
	
	public int xPos=0;
	public int yPos=0;
	public boolean isBomb=false;
    public int nrOfBombs=0;
    public boolean markAsBomb=false;
    
    private boolean isDiscovered=false;
    private Minesweeper theGame;
       
    
    public Mine(Minesweeper theGame, int xPos, int yPos){
    	this.xPos = xPos;
    	this.yPos = yPos;
    	this.theGame = theGame;
    }
    
     
    public void discover(){
    	if(!(this.getDiscovered())){
    		this.setDiscovered(true);
    	   	//this.setBorderPainted(false);
    		this.setContentAreaFilled(false);
    		if(this.nrOfBombs==0){
    			if (!(this.isBomb)){
    				this.setText("");
    				//this.setEnabled(false);
    				this.removeMouseListener(theGame.listenerForMouse);
    				this.discoverArea(this.xPos,this.yPos);
    			}else {
    				System.out.println("Itt a vége!!!");
    				
    				this.theGame.timeIsRunning=false;
    				this.theGame.showTheTable();
    				
    				JOptionPane.showMessageDialog(this.theGame, "ez most nem sikerült", "Vége a dalnak", JOptionPane.INFORMATION_MESSAGE);
    			}
    			
    		}else{
    			switch (this.nrOfBombs) {
				case 1:
					this.setForeground(Color.BLUE);
					break;
				case 2:
					this.setForeground(Color.MAGENTA);
					break;
				case 3:
					this.setForeground(Color.RED);
					break;
				case 4:
					this.setForeground(Color.GREEN);
					break;
				case 5:
					this.setForeground(Color.ORANGE);
					break;
				case 6:
					this.setForeground(Color.CYAN);
					break;
				case 7:
					this.setForeground(Color.DARK_GRAY);
					break;

				default:
					this.setForeground(Color.BLACK);
					break;
				}
    			this.setText(Integer.toString(this.nrOfBombs));
    		}
    	}
    	if(theGame.timeIsRunning){theGame.thisIsTheEnd();}
    }	
    
    public void markedBomb(){
    	
    	if(this.markAsBomb){
    		this.setText("");
    		this.setIcon(null);
        	this.markAsBomb=false;
        	this.theGame.markedMines--;
    	}else{
    		this.setIcon(new ImageIcon(this.theGame.newimgFlag));
    		
        	this.markAsBomb=true;
        	this.theGame.markedMines++;
    	}
    	this.theGame.lblBomb.setText("Megvan: "+this.theGame.markedMines);

    }
    
    public void discoverArea(int x, int y){
    	for (int i=-1;i<2;i++){
    		for (int j=-1;j<2;j++){
    			if(this.xPos+i>=0 && this.xPos+i<=9 && this.yPos+j>=0 && this.yPos+j<=9 && !(theGame.mineArray[this.xPos+i][this.yPos+j].getDiscovered())) {
    				theGame.mineArray[this.xPos+i][this.yPos+j].discover();
    	    	}
    		}
    	}
    }    
    public void discoverUnmarked(int x, int y){
    	for (int i=-1;i<2;i++){
    		for (int j=-1;j<2;j++){
    			if(this.xPos+i>=0 && this.xPos+i<=9 && this.yPos+j>=0 && this.yPos+j<=9 && !(theGame.mineArray[this.xPos+i][this.yPos+j].markAsBomb)) {
    				theGame.mineArray[this.xPos+i][this.yPos+j].discover();
    	    	}
    		}
    	}
    }    
    
    public void neighborNotification(){
    	for (int i=-1;i<2;i++){
    		for (int j=-1;j<2;j++){
    			if((this.xPos+i>=0 && this.xPos+i<=9 && this.yPos+j>=0 && this.yPos+j<=9) && !(theGame.mineArray[this.xPos+i][this.yPos+j].isBomb)) {
    				theGame.mineArray[this.xPos+i][this.yPos+j].nrOfBombs+=1;
    				//theGame.mineArray[this.xPos+i][this.yPos+j].setToolTipText(Integer.toString(theGame.mineArray[this.xPos+i][this.yPos+j].nrOfBombs));
    	    	}
    		}
    	}
    }
	
	public boolean getDiscovered() {
		return isDiscovered;
	}

	public void setDiscovered(boolean isDiscovered) {
		this.isDiscovered = isDiscovered;
	}


}