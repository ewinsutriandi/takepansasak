package research.aes.imgproc;

import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class MedianFilter implements Filter{
	BufferedImage srcImage;
	int kernelSize = 3;
	BufferedImage filteredImage;
	public MedianFilter(BufferedImage source,int kernelSize){
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
				Color c = calculateMedian(x,y);
				//System.out.println(x+" "+y);
				filteredImage.setRGB(x, y, c.getRGB());
			}
		}
	}
	private Color calculateMedian(int x, int y) {
		List<Color> lc = getNeighborColors(x,y);
		int[] arrR = new int[lc.size()];
		int[] arrG = new int[lc.size()];
		int[] arrB = new int[lc.size()];
		for (int i = 0; i < lc.size(); i++) {
			Color color = lc.get(i);
			arrR[i] = color.getRed();
			arrG[i] = color.getGreen();
			arrB[i] = color.getBlue();
		}
		int medR,medG,medB;
		int pos = lc.size()/2;
		if (lc.size() % 2 == 1){
			medR = arrR[pos];
			medG = arrG[pos];
			medB = arrB[pos];
		} else {
			medR = (arrR[pos]+arrR[pos-1])/2;
			medG = (arrG[pos]+arrG[pos-1])/2;
			medB = (arrB[pos]+arrB[pos-1])/2;
		}
		Color medianColor = new Color(medR,medG,medB);
		return medianColor;
	}
	private List<Color> getNeighborColors(int x, int y) {
		Point cp = new Point(x,y);
		Point mp = new Point(srcImage.getWidth(), srcImage.getHeight());
		System.out.println(mp.x+" "+mp.y);
		List<Point> lp = CommonUtil.getNeighboringCoordinates(cp, mp, kernelSize);
		List<Color> lc = new ArrayList<>();
		for (Point point : lp) {
			System.out.println(point.x+" "+point.y);
			lc.add(new Color(srcImage.getRGB(point.x,point.y)));
		}
		return lc;
	}
	
}
