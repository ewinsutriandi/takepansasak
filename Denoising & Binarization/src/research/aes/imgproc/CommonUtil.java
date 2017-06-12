package research.aes.imgproc;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class CommonUtil {
	/**
	 * 
	 * @param curPoint current pixel position
	 * @param maxPoint image size in pixel
	 * @param kernel kernel size, must be odd and greater than 2
	 * @return list of available neighboring pixel coordinates
	 */
	public static List<Point> getNeighboringCoordinates(Point curPoint,Point maxPoint, int kernel){
		if (kernel % 2 == 0) kernel++;
		int hiBound = (kernel - 1 )/2;
		int loBound = -1 * hiBound;
		List<Point> lp = new ArrayList<Point>();
		for (int i = loBound; i <= hiBound; i++) {
			//horizontal
			for (int j = loBound; j <= hiBound; j++) {
				int nx,ny;
				nx = i+curPoint.x;
				ny = j+curPoint.y;
				if (nx>=0 
						&& ny>=0
						&&nx<maxPoint.x 
						&& ny < maxPoint.y){
					Point p = new Point(nx, ny);
					lp.add(p);
				}
			}
		}
		return lp;
	}

}
