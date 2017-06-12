package research.aes.takepan;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.objdetect.CascadeClassifier;

public class Hello
{
   public static void main( String[] args )
   {
	   System.out.println("Welcome to OpenCV " + Core.VERSION+" "+Core.NATIVE_LIBRARY_NAME+" ");
	   System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	   Mat mat = Mat.eye(3, 3, CvType.CV_8UC1);
       System.out.println("mat = " + mat.dump());
       
       
   }

   
}
