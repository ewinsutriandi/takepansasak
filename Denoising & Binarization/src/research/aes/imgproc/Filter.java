package research.aes.imgproc;

import java.awt.image.BufferedImage;

public interface Filter {
	public void executeFilter();
	public BufferedImage getFilteredImage();

}
