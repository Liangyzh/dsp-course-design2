package pic.utility;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import pic.algorithm.sift.ImageTransform;
import pic.ui.MainFrame;

public class Test {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
			String source="E:\\eclipse data\\Pic_True\\img\\target.jpg";
			String target="E:\\eclipse data\\Pic_True\\img\\target.jpg";
			double[] rgb = new double[3];
			double[] hsv = new double[3];
			BufferedImage bi = null;
			MainFrame siftmain=new MainFrame(source, target);
//			System.out.println("y="+siftmain.fina+"x+"+siftmain.finb);
	
			File file = new File(target);
			try {
				bi = ImageIO.read(file);
			} catch (Exception e) {
				e.printStackTrace();
			}
			int width = bi.getWidth();
			int height = bi.getHeight();
			int minx = bi.getMinX();
			int miny = bi.getMinY();
			int[] result=new int[width];
			int[] conv_resulth=new int[width];
			int[] record=new int[width];
			int[] recordh=new int[width];
			double[] records=new double[width];
			double[] recordv=new double[width];
			
//			System.out.println(width);
			
			for (int i = minx; i < width; i++) {
					double k=siftmain.fina*i+siftmain.finb;
					if (k>0&&k<height)
						{
						int pixel = bi.getRGB(i, (int)k); // 下面三行代码将一个数字转换为RGB数字
						rgb[0] = ((pixel & 0xff0000) >> 16)/255.0;
						rgb[1] = ((pixel & 0xff00) >> 8)/255.0;
						rgb[2] = (pixel & 0xff)/255.0;
//						System.out.println("i=" + i + ",j=" + (int)k + ":(" + rgb[0] + ","+ rgb[1] + "," + rgb[2] + ")");
						//System.out.println(j+"  "+i);
						
						if (rgb[0]>rgb[1])
						{
							if (rgb[2]>=rgb[0])
							{//b>r>g
							hsv[2]=rgb[2];
							hsv[1]=(hsv[2]-rgb[1])/hsv[2];
							hsv[0]=240.0+60.0*(rgb[0]-rgb[1])/(hsv[2]-rgb[1]);
							}
							else
							{	
								if(rgb[2]<rgb[1])
								{//r>g>b
								hsv[2]=rgb[0];
								hsv[1]=(hsv[2]-rgb[2])/hsv[2];
								hsv[0]=60.0*(rgb[1]-rgb[2])/(hsv[2]-rgb[2]);
								
								}
								else{//r>b>g 
								hsv[2]=rgb[0];	
								hsv[1]=(hsv[2]-rgb[1])/hsv[2];
								hsv[0]=60.0*(rgb[1]-rgb[2])/(hsv[2]-rgb[1])+360;
								}
							}
						}
						
						if (rgb[0]<rgb[1])
						{
							if (rgb[2]<=rgb[0])
							{//b<r<g
							hsv[2]=rgb[1];
							hsv[1]=(hsv[2]-rgb[2])/hsv[2];
							hsv[0]=120.0+60.0*(rgb[2]-rgb[0])/(hsv[2]-rgb[2]);
							}
							else
							{	
								if(rgb[2]>=rgb[1])
								{//r<g<b
								hsv[2]=rgb[2];
								hsv[1]=(hsv[2]-rgb[0])/hsv[2];
								hsv[0]=240.0+60.0*(rgb[0]-rgb[1])/(hsv[2]-rgb[0]);
								}
								else{//r<b<g
								hsv[2]=rgb[1];
								hsv[1]=(hsv[2]-rgb[0])/hsv[2];
								hsv[0]=120.0+60.0*(rgb[2]-rgb[0])/(hsv[2]-rgb[0]);
								}
							}
							
						}
						
						if(rgb[0]==rgb[1]&&rgb[1]==rgb[0])
						{	
							hsv[0]=0;
							hsv[1]=0;
							hsv[2]=rgb[0];
						}
						
						
						if (hsv[0]<0)
							hsv[0]=hsv[0]+360;
//						System.out.println("i=" + i + ",j=" + (int)k + "hsv  :(" + hsv[0] + ","+ hsv[1] + "," + hsv[2] + ")"+ "  rgb  :(" + rgb[0] + ","+ rgb[1] + "," + rgb[2] + ")");
//						System.out.println(hsv[0]);
						result[i]=(int)hsv[0];
						recordh[i]=(int)hsv[0];
						records[i]=hsv[1];
						recordv[i]=hsv[2];
						}
				
			}
			
			for(int i=0;i<5;i++){
					for (int j=0;j<width;j++){
					if(j==0)
						conv_resulth[j]=result[0]+result[1];
					else if(j==width-1)
						conv_resulth[j]=result[width-2]+result[width-1];
					else 
						conv_resulth[j]=result[j]+result[j-1]+result[j+1];
					}
					for (int j=0;j<width;j++){
						result[j]=conv_resulth[j];
					}
					
			}
				
			for (int j=1;j<width-1;j++){
				if(conv_resulth[j]!=0){
					if ((conv_resulth[j]>=conv_resulth[j-1])&&(conv_resulth[j]>=conv_resulth[j+1])){
						record[j]=1;
						System.out.println("h: "+recordh[j]+" s: "+records[j]+" v: "+recordv[j]);
						if ((conv_resulth[j]-conv_resulth[j-1])>1000||(conv_resulth[j]-conv_resulth[j-1])<-1000){
							if((conv_resulth[j]-conv_resulth[j+1]>1000)||(conv_resulth[j]-conv_resulth[j+1])<-1000){
								record[j]++;
							}
						}
					}
				}

//				System.out.println(record[j]);
			}

		
	}	
}
