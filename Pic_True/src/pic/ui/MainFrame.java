package pic.ui;

import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import pic.algorithm.sift.ImageTransform;
import pic.algorithm.sift.MyPoint;
import pic.utility.Image_Utility;

public class MainFrame extends JFrame{
	public double fina,finb;
	
	public MainFrame(String sourcePath,String targetPath) {
		// TODO Auto-generated constructor stub	
		///�Ҷ�ͼ��
		BufferedImage sourceImage=grayTran(sourcePath);
		BufferedImage targetImage=grayTran(targetPath);		
		sourceImage=targetImage;
		
		HashMap<Integer,double[][]> result=ImageTransform.getGaussPyramid(Image_Utility.imageToDoubleArray(sourceImage), 20, 3,1.6);  
		HashMap<Integer,double[][]> dog=ImageTransform.gaussToDog(result, 6);
		HashMap<Integer,List<MyPoint>> keyPoints=ImageTransform.getRoughKeyPoint(dog,6);
		keyPoints=ImageTransform.filterPoints(dog, keyPoints, 10,0.03);
		sourceImage=ImageTransform.drawPoints(result,keyPoints);
		fina=ImageTransform.finlinea;
		finb=ImageTransform.finlineb;
	}

	
	public BufferedImage getBlock(BufferedImage source,BufferedImage edge,int threshold){
		int width=source.getWidth();
		int height=source.getHeight();
		BufferedImage resultImage=new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
	
		for(int j=0;j<height;j++){
			for(int i=0;i<width;i++){
				int rgb=edge.getRGB(i, j);
				int grey=(rgb>>16)&0xFF;
				if(grey>threshold){
					resultImage.setRGB(i, j, 0);
				}else{
					resultImage.setRGB(i, j, source.getRGB(i, j));
				}

			}
		} 
		
		return resultImage;
	}
	
	
	
	/**
	 * �Ҷȴ�����ȡ�Ҷ�ͼ��
	 * @param imagePath
	 * @return
	 */
	public BufferedImage grayTran(String imagePath){
		 
		BufferedImage bimg=null;
		try {
			bimg = ImageIO.read(new File(imagePath));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		int width=bimg.getWidth();
		int height=bimg.getHeight();

		
		BufferedImage targetImage = null;

			targetImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);//BufferedImage.TYPE_BYTE_BINARY);
		

		for(int j=0;j<height;j++){
			for(int i=0;i<width;i++){
				int rgb=bimg.getRGB(i, j);
				int c_red=(rgb>>16)&0xFF;
				int c_green=(rgb>>8)&0xFF;
				int c_blue=rgb&0xFF;				
				int grayRGB=(int) (0.3 * c_red + 0.59 * c_green + 0.11 * c_blue);////�ҶȻ�			
				 	
				rgb=(255<<24)|(grayRGB<<16)|(grayRGB<<8)|grayRGB;///�ҶȻ��ָ�

				targetImage.setRGB(i, j, rgb);
			}
		}
		
		
		return targetImage;
				
	}
	
	
	
	
	/**
	 * ����ԴͼƬ��Ŀ��ͼƬ��С
	 * @param sourceImage
	 * @param width Ŀ��ͼ���
	 * @param height Ŀ��ͼ���
	 * @return  ��С���ͼ��
	 */
	public BufferedImage shrinkToSize(BufferedImage sourceImage,int width,int height){	
		
		BufferedImage targetImage=new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		
		double widthStep=(double)sourceImage.getWidth()/width;///���������
		double heightStep=(double)sourceImage.getHeight()/height;//�߱�������
		
		
		for(int j=0;j<height;j++){//��
			for(int i=0;i<width;i++){//��
				///���и��㲽���������������ڴ������ŵ�Դͼ���o.5����1��֮���Ҫ��
				int x=(int)(i*widthStep);
				int y=(int)(j*heightStep);
				int rgb=sourceImage.getRGB(x, y);////+sourceImage.getRGB(x,y+1)+sourceImage.getRGB(x+1,y);
				targetImage.setRGB(i, j, rgb);
			}
		}
		
		return targetImage;
	}
	
	
	/**
	 * ��ȡ�Ҷ���������:����ÿ���Ҷ�ֵ�����ص�
	 * 
	 * @param bufferedImage
	 * @return
	 */
	public int[] greyHistogram(BufferedImage bufferedImage){
		
		int width=bufferedImage.getWidth();
		int height=bufferedImage.getHeight();
		
		int featureArray[]=new int[256];
		for(int j=0;j<height;j++){
			for(int i=0;i<width;i++){
				int rgb=bufferedImage.getRGB(i, j);
				int grey=(rgb>>16)&0xFF;
//				System.out.println(grey);
				featureArray[grey]=featureArray[grey]+1;
				
			}
		}
		 return featureArray;
	}
	
	
	/**
	 * �Ƚ����ƶȣ����ü���ƽ��ֵ��С��
	 * @param source
	 * @param target
	 */
	public double calSimilarity(int[] source,int[] target){
		int length=source.length;
		double min=0,max=0;
		for(int i=0;i<length;i++){
			if(source[i]>target[i]){
				max=max+Math.sqrt(source[i]*target[i]);
				min=min+target[i];
			}else{
				max=max+Math.sqrt(source[i]*target[i]);
				min=min+source[i];
			}
		}
		 
		return (double)min/max;
		
	}
	
	
	
}
