/*The Image class
 * --> Read/Save/display Images
 * --> Extract pixel arrays, etc. 
 * --> Based on BufferedArray 
 ----------------------------
 Nischal Regmi
 Associate Professor, EEC
 */


import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;


public class Image {
	BufferedImage image;
	
	//*********************************************************************
	//************** Constructors *****************************************
	public Image(String filename) {
		 try{
			 this.image = ImageIO.read(new File(filename));
		    }catch(IOException e){
		      System.out.println("Error: "+e);
		  }
	}
	
	public Image(BufferedImage img) {
		this.image = img;
	}
	
	public Image(int[][] A) {//gray-scale image from array
		image = new BufferedImage(A.length,A[0].length,
								BufferedImage.TYPE_BYTE_GRAY);
		for(int x=0;x<image.getWidth(); x++)
			for(int y=0;y<image.getHeight();y++) {
				Color newColor = new Color(A[x][y],A[x][y],A[x][y]);
	    		image.setRGB(x, y,newColor.getRGB());
			}
	}		
	
	
	
	//*******************************************************************
	//************** Basic IO and other functions ***********************
	
	BufferedImage getImage() {
		return this.image;
	}
		
	void saveToFile(String filename,String extension) {
		 try{
		      ImageIO.write(image, "jpg", new File(filename+"."+extension));
		    }catch(IOException e){
		      System.out.println("Error: "+e);
		    }
	}
	
	public static void saveToFile(int[][] f, String filename, String extension) {
		Image im = new Image(f);
		im.saveToFile(filename, extension);
	}
	
	void display(String title) {
		ImageIcon icon=new ImageIcon(this.image);
		JFrame frame=new JFrame(title);
        frame.setLayout(new FlowLayout());
        frame.setSize(this.image.getWidth(),this.image.getHeight());
        JLabel lbl=new JLabel();
        lbl.setIcon(icon);
        frame.add(lbl);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
	}
	
	public static void display(int[][] f,String title) {
		//clip intensities to the range [0,255]
		for(int x=0;x<f.length;x++)
			for(int y=0;y<f[0].length;y++) {
				if(f[x][y]>255)
					f[x][y]=255;
				if(f[x][y]<0)
					f[x][y]=0;
				}

		Image img = new Image(f);
		img.display(title);
	}
	
	public static void display(double[][] f,String title){
		//clip intensities to the range [0,255]
		int[][] F = new int[f.length][f[0].length];
		for(int x=0;x<f.length;x++)
			for(int y=0;y<f[0].length;y++) {
				F[x][y] = (int) Math.round(f[x][y]);
				if(F[x][y]>255)
							F[x][y]=255;
				if(F[x][y]<0)
							F[x][y]=0;
				}
		Image img = new Image(F);
		img.display(title);
                
                

	}
	
	
	int[][] getPixelArray(){
		int[][] A = new int[image.getWidth()][image.getHeight()];
		for(int x=0;x<image.getWidth();x++)
			for(int y=0;y<image.getHeight();y++) {
				Color c = new Color(image.getRGB(x, y));
				A[x][y] = (int) (c.getRed()+c.getGreen()+c.getBlue())/3;			}
		return A;	
	}
	
       // Apply log to focus in dark parts
        public static int [][] logTrans( int[][] f, int c)
        {
            int min=255,max=0;
            int[][] g= new int[f.length][f[0].length];
            
            
            for( int x=0; x<f.length;x++)
                 for(int y=0;y<f[0].length;y++)
                    {
                        g[x][y]= c*(int) Math.log(f[x][y]+1);
                    }
            
            for( int x=0; x<f.length;x++)
                    for(int y=0;y<f[0].length;y++)
                    {
                        //f[x][y]= (int) Math.log(f[x][y]);
                        if(g[x][y]<min)
                            min=f[x][y];
                        if ((g[x][y]>max))
                            max=g[x][y];
                    }
            
            for( int x=0; x<f.length;x++)
                for(int y=0;y<f[0].length;y++)
                        g[x][y]= (int)(((float) (g[x][y]-min)/(max-min))*255);
                return g;
        }
        
        // to apply histogram Question 2
        public static int [][] histogram( int[][] f){
            int l= 256;
            int [] n = new int [l];
            int [] s = new int [l];
            
            float [] p = new float [l];
            int [][] g = new int [f.length][f[0].length];
            
            
            for(int x=0; x<f.length;x++)
                    for(int y=0;y<f[0].length;y++)
                    {
                        n[f[x][y]]++;
                    }
            
            for(int i=0;i<l;i++){
                p[i]=(float) ((double) n[i]/(f.length*f[0].length));
            }
            
            
            //Transformation function for histogram equaliztion
            
            for(int j=0;j<l;j++){
                float sum=0;
            
                for(int i=0;i<=j;i++){
                    sum= p[i]+ sum;
                    
                   }
                
                 s[j] = (int)((l- 1) * sum ); //sum should not be ERRORRRRRRR
            } 
            
            for( int x=0; x<f.length;x++)
                    for(int y=0;y<f[0].length;y++)
                    {
                        g[x][y]=s[f[x][y]];
                    }
            return g;       
        }
                
	public static void main(String[] args){
		Image img = new Image("C:\\Users\\Asus\\Desktop\\taudaha.jpg");
		int[][] f = img.getPixelArray();
		Image.display(f, "Original");
		Image.saveToFile(f,"sameAsOriginal","png");
                
                f=Image.logTrans(f, 1000);
                Image.display(f, "After log");
		Image.saveToFile(f,"sameAsOriginal","png");
                
                f=Image.histogram(f);
                Image.display(f, "After histogram");
		Image.saveToFile(f,"sameAsOriginal","png");
	}
}
