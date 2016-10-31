import org.opencv.core.Core;
import org.opencv.core.Size;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Scalar;
import org.opencv.core.Point;

import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;


import java.util.*;
import java.io.File;

public class HelloDistance extends Test{

  public static int countColonies(String filePath, String fileName){
    try{
      // Raw image without mask
     Mat source = Highgui.imread(filePath, Highgui.CV_LOAD_IMAGE_COLOR);
     source = Test.scaledResize(source, 1000);

     Mat destination = new Mat(source.rows(),source.cols(),source.type());
     Mat gray = new Mat(source.rows(), source.cols(), CvType.CV_8UC1);
     Mat blur = new Mat(source.rows(), source.cols(), CvType.CV_8UC1);
     Mat tophat = new Mat(gray.rows(), gray.cols(), CvType.CV_8UC1);
     Mat dt = new Mat(gray.rows(), gray.cols(), CvType.CV_8UC1);
     Mat mask = new Mat(gray.rows(), gray.cols(), CvType.CV_8UC1, Scalar.all(0));
     Mat tophat_mask = new Mat(gray.rows(), gray.cols(), CvType.CV_8UC1);
     Mat rm = new Mat(gray.rows(), gray.cols(), CvType.CV_8UC1);
     destination = source;

     // RGB to gray
     Imgproc.cvtColor(destination, gray, Imgproc.COLOR_BGR2GRAY);

     // Do top hat filtering to correct for uneven illumination, does it work for all images? Let's hope so or we'll implement rolling ball algorithm
     Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, new Size(35,35));
     Imgproc.morphologyEx(gray, tophat, Imgproc.MORPH_TOPHAT, kernel);
     Test.saveImg(fileName+"_tophat.jpg", tophat);

     // Blur before thresholding
     Imgproc.GaussianBlur(tophat, blur, new Size(5,5), 0);
     Test.saveImg(fileName+"_blurred.jpg", blur);

     // Apply mask on the image
     Point center = new Point(source.cols()/2, source.rows()/2);
     Scalar maskColor = new Scalar(255, 255, 255);

     Core.circle(mask, center, Math.min(source.rows()/2, source.cols()/2) - 20, maskColor, -1);
     Test.saveImg(fileName+"_mask.jpg", mask);
     blur.copyTo(tophat_mask, mask);
     Core.bitwise_and(blur, blur, tophat_mask, mask);
     Test.saveImg(fileName+"_tophat_mask.jpg", tophat_mask);

     // Otsu thresholding on the tophat image
     Imgproc.threshold(tophat_mask,gray,0,255,Imgproc.THRESH_BINARY|Imgproc.THRESH_OTSU);
     Test.saveImg(fileName+"_threshold.png", gray);

     // Find contours
     List<MatOfPoint> contours = new ArrayList<MatOfPoint>(); //
     List<MatOfPoint> cnts = new ArrayList<MatOfPoint>();
     Imgproc.findContours(gray, contours, new Mat(), Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);
     //System.out.println("Total Contours: "+contours.size());

     // Iterate over contours and print only the ones who have a circularity greater than 5
     for (int i=0; i<contours.size(); i++){
       MatOfPoint2f cont = new MatOfPoint2f(contours.get(i).toArray());
       double perimeter = Imgproc.arcLength(cont, true);
       double area = Imgproc.contourArea(cont);

       if(perimeter ==0){
         continue;
       }

       double circ = (4*Math.PI*area)/(Math.pow(perimeter,2));

       if(area>100){
         cnts.add(contours.get(i));
       }
     }

     Mat black = Mat.zeros(gray.rows(), gray.cols(), CvType.CV_8UC1);
     Imgproc.drawContours(black, cnts, -1, new Scalar(255,255,255), -1);
     Test.saveImg(fileName+"_black_contours.png", black);

     // Do the distance trnasform and count
     Imgproc.distanceTransform(black, dt, Imgproc.CV_DIST_L2, Imgproc.CV_DIST_MASK_PRECISE);
     Test.saveImg(fileName+"_distance_transform.png", dt);

     rm = Test.regional_maxima(dt);
     Test.saveImg(fileName+"_regional_maxima.png", rm);

     Test.label(rm);
     Test.saveImg(fileName+"_label.png", rm);
     //System.out.println(rm.dump());
     Core.MinMaxLocResult mmr = Core.minMaxLoc(rm);
     int count = (int)mmr.maxVal-1;
     //System.out.println(count);

     Imgproc.drawContours(source, cnts, -1, new Scalar(255,0,0), 2);
     Test.saveImg(fileName+"_final.png", source);

     // Release all matrices
     destination.release();
     gray.release();
     blur.release();
     tophat.release();
     dt.release();
     mask.release();
     tophat_mask.release();
     rm.release();

     return count;

    }catch (Exception e) {
        System.out.println("error: " + e.getMessage());

        return 0;
    }
  }

  public static void main(String[] args){
    System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

    // Read files from dataset
    File folder = new File("/Users/shashwat/workspace/dopencv/java/dataset/");
    File[] listOfFiles = folder.listFiles();
    double errorSum = 0.0;

    for (File file : listOfFiles) {
        if (file.isFile()) {
            String filePath = file.getAbsolutePath();
            String fileName = file.getName();
            int trueCount = Integer.parseInt(fileName.replace(".jpg",""));
            int predCount = countColonies(filePath, fileName);
            double deviation = 100 - 100*((double)Math.abs(trueCount - predCount))/((double) trueCount);
            errorSum += deviation;
            System.out.println(trueCount + "->" + predCount + " percent: " + deviation);
        }
    }

    System.out.println("Net accuracy: "+errorSum/listOfFiles.length);

    //int count = countColonies("/Users/shashwat/Downloads/75.jpg");
   }
}
