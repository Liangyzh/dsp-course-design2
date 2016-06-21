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
		///灰度图像
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
	 * 灰度处理，获取灰度图像
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
				int grayRGB=(int) (0.3 * c_red + 0.59 * c_green + 0.11 * c_blue);////灰度化			
				 	
				rgb=(255<<24)|(grayRGB<<16)|(grayRGB<<8)|grayRGB;///灰度化恢复

				targetImage.setRGB(i, j, rgb);
			}
		}
		
		
		return targetImage;
				
	}
	
	
	
	
	/**
	 * 缩放源图片到目标图片大小
	 * @param sourceImage
	 * @param width 目标图像宽
	 * @param height 目标图像高
	 * @return  缩小后的图像
	 */
	public BufferedImage shrinkToSize(BufferedImage sourceImage,int width,int height){	
		
		BufferedImage targetImage=new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		
		double widthStep=(double)sourceImage.getWidth()/width;///宽遍历步长
		double heightStep=(double)sourceImage.getHeight()/height;//高遍历步长
		
		
		for(int j=0;j<height;j++){//行
			for(int i=0;i<width;i++){//列
				///进行浮点步长搜索，这样便于处理缩放到源图像的o.5倍到1倍之间的要求
				int x=(int)(i*widthStep);
				int y=(int)(j*heightStep);
				int rgb=sourceImage.getRGB(x, y);////+sourceImage.getRGB(x,y+1)+sourceImage.getRGB(x+1,y);
				targetImage.setRGB(i, j, rgb);
			}
		}
		
		return targetImage;
	}
	
	
	/**
	 * 获取灰度特征向量:计算每个灰度值的像素点
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
	 * 比较相似度，采用几何平均值最小法
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
