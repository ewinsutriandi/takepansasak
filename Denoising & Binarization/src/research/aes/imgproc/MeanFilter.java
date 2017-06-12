package research.aes.imgproc;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class MeanFilter implements Filter{
	BufferedImage srcImage;
	int kernelSize;
	BufferedImage filteredImage;
	public MeanFilter(BufferedImage source,int kernelSize){
		this.srcImage = source;
		this.kernelSize = kernelSize;
	}
	private int width(){
		return srcImage.getWidth();
	}
	private int height(){
		return srcImage.getHeight();
	}
	public BufferedImage getFilteredImage(){
		if (filteredImage==null){
			executeFilter();
		}
		return filteredImage;
	}
	public void executeFilter(){
		filteredImage = new BufferedImage(width(), height(), BufferedImage.TYPE_INT_RGB);
		//traverse image's pixels
		for (int x=0;x<width();x++){
			for (int y=0;y<height();y++){
				Color c = calculateMean(x,y);
				//System.out.println(x+" "+y);
				filteredImage.setRGB(x, y, c.getRGB());
			}
		}
	}
	private Color calculateMean(int x, int y) {
		List<Color> lc = getNeighborColors(x,y);
		int sumR=0,sumG=0,sumB=0;
		int cnt=0;
		for (Color color : lc) {
			cnt++;
			sumR += color.getRed();
			sumG += color.getGreen();
			sumB += color.getBlue();
		}
		Color medianColor = new Color(sumR/cnt, sumG/cnt, sumB/cnt);
		return medianColor;
	}
	private List<Color> getNeighborColors(int x, int y) {
		List<Color> lc = new ArrayList<>();
		if (x>0){
			Color c = new Color(srcImage.getRGB(x-1, y));
			lc.add(c);
		}
		if (y>0){
			Color c = new Color(srcImage.getRGB(x, y-1));
			lc.add(c);
		}
		if (x>0&&y>0){
			Color c = new Color(srcImage.getRGB(x-1, y-1));
			lc.add(c);
		}
		if (x<width()-1){
			Color c = new Color(srcImage.getRGB(x+1, y));
			lc.add(c);
		}
		if (y<height()-1){
			Color c = new Color(srcImage.getRGB(x, y+1));
			lc.add(c);
		}
		if (x<width()-1&&y<height()-1){
			Color c = new Color(srcImage.getRGB(x+1, y+1));
			lc.add(c);
		}
		if (x<width()-1&&y>0){
			Color c = new Color(srcImage.getRGB(x+1, y-1));
			lc.add(c);
		}
		if (x>0&&y<height()-1){
			Color c = new Color(srcImage.getRGB(x-1, y+1));
			lc.add(c);
		}
		return lc;
	}
	
}
