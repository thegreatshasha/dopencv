import org.opencv.core.Core;
import org.opencv.core.Size;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Scalar;

import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;


import java.util.*;

public class HelloDistance extends Test{

  public static int countColonies(String fileName){
    try{
     Mat source = Highgui.imread(fileName, Highgui.CV_LOAD_IMAGE_COLOR);

     // Apply mask here

     source = Test.scaledResize(source, 1000);
     Mat destination = new Mat(source.rows(),source.cols(),source.type());
     Mat gray = new Mat(source.rows(), source.cols(), CvType.CV_8UC1);
     Mat blur = new Mat(source.rows(), source.cols(), CvType.CV_8UC1);
     Mat tophat = new Mat(gray.rows(), gray.cols(), CvType.CV_8UC1);
     Mat dt = new Mat(gray.rows(), gray.cols(), CvType.CV_8UC1);
     Mat rm = new Mat(gray.rows(), gray.cols(), CvType.CV_8UC1);
     destination = source;

     // RGB to gray
     Imgproc.cvtColor(destination, gray, Imgproc.COLOR_BGR2GRAY);

     // Do top hat filtering to correct for uneven illumination, does it work for all images? Let's hope so or we'll implement rolling ball algorithm
     Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, new Size(35,35));
     Imgproc.morphologyEx(gray, tophat, Imgproc.MORPH_TOPHAT, kernel);
     Test.saveImg("tophat.jpg", tophat);

     // Blur before thresholding
     Imgproc.GaussianBlur(tophat, blur, new Size(5,5), 0);
     Test.saveImg("blurred.jpg", blur);

     // Otsu thresholding on the tophat image
     Imgproc.threshold(blur,gray,0,255,Imgproc.THRESH_BINARY|Imgproc.THRESH_OTSU);
     Test.saveImg("threshold.png", gray);

     // Find contours
     List<MatOfPoint> contours = new ArrayList<MatOfPoint>(); //
     List<MatOfPoint> cnts = new ArrayList<MatOfPoint>();
     Imgproc.findContours(gray, contours, new Mat(), Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);
     System.out.println("Total Contours: "+contours.size());

     // Iterate over contours and print only the ones who have a circularity greater than 5
     for (int i=0; i<contours.size(); i++){
       MatOfPoint2f cont = new MatOfPoint2f(contours.get(i).toArray());
       double perimeter = Imgproc.arcLength(cont, true);
       double area = Imgproc.contourArea(cont);

       if(perimeter ==0){
         continue;
       }

       double circ = (4*Math.PI*area)/(Math.pow(perimeter,2));

       if(circ>0.05 && area>100 && area<8000){
         cnts.add(contours.get(i));
       }
     }

     Mat black = Mat.zeros(gray.rows(), gray.cols(), CvType.CV_8UC1);
     Imgproc.drawContours(black, cnts, -1, new Scalar(255,255,255), -1);
     Test.saveImg("black_contours.png", black);

     // Do the distance trnasform and count
     Imgproc.distanceTransform(black, dt, Imgproc.CV_DIST_L2, Imgproc.CV_DIST_MASK_PRECISE);
     Test.saveImg("distance_transform.png", dt);

     rm = Test.regional_maxima(dt);
     Test.saveImg("regional_maxima.png", rm);

     Test.label(rm);
     Test.saveImg("label.png", rm);
     //System.out.println(rm.dump());
     Core.MinMaxLocResult mmr = Core.minMaxLoc(rm);
     int count = (int)mmr.maxVal-1;
     System.out.println(count);

     Imgproc.drawContours(source, cnts, -1, new Scalar(255,0,0), 2);
     Test.saveImg("final.png", source);

     return count;

    }catch (Exception e) {
        System.out.println("error: " + e.getMessage());

        return 0;
    }
  }

  public static void main(String[] args){
    System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
      int count = countColonies("/Users/shashwat/Downloads/trouble.jpg");
   }
}
