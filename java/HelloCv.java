import org.opencv.core.Core;
import org.opencv.core.Size;
import org.opencv.core.Point;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Scalar;

import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;


import java.util.*;

public class HelloCv extends Test{
  public static void main(String[] args){

       try{

          System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

          Mat source = Highgui.imread("/Users/shashwat/Downloads/5855040805151358023-account_id=2.jpg",  Highgui.CV_LOAD_IMAGE_COLOR);
          source = Test.scaledResize(source, 1000);
          Mat mask = new Mat(source.rows(), source.cols(), CvType.CV_8UC1, Scalar.all(0));
          Mat tophat_mask = new Mat(source.rows(), source.cols(), CvType.CV_8UC1, Scalar.all(0));

          Mat gray = source.clone();
          Test.saveImg("original.png", gray);
          //
          //  // RGB to gray
          Imgproc.cvtColor(gray, gray, Imgproc.COLOR_BGR2GRAY);
          //
          //  // Do top hat filtering to correct for uneven illumination, does it work for all images? Let's hope so or we'll implement rolling ball algorithm
          Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, new Size(35,35));
          Imgproc.morphologyEx(gray, gray, Imgproc.MORPH_TOPHAT, kernel);
          Test.saveImg("tophatted.png", gray);
          //
          //  // Otsu thresholding on the tophat image
          //Imgproc.threshold(gray,gray,0,255,Imgproc.THRESH_OTSU);
          //
          //  // Save everything
          //Test.saveImg("thresholded.png", gray);
          //
          //  // Testing, testing
          //Imgproc.distanceTransform(gray, gray, Imgproc.CV_DIST_L2, 3);
          //Test.saveImg("dt.png", gray);

          // Create a mask
          Point center = new Point(source.cols()/2, source.rows()/2);
          Scalar maskColor = new Scalar(255, 255, 255);
          Core.circle(mask, center, Math.min(source.rows()/2, source.cols()/2), maskColor, -1);
          Test.saveImg("mask.jpg", mask);

          // Create a mask, figure out why this is not working
          //tophat.copyTo(tophat_mask, mask);
          //Core.bitwise_and(tophat, tophat, tophat, mask);
          //Test.saveImg("tophat_mask.jpg", tophat);
          System.out.println(gray.dump());
          Core.bitwise_and(gray, gray, tophat_mask, mask);
          Test.saveImg("tophat_mask.jpg", tophat_mask);

          // Test creating and applying the mask here


          //
          //  // Find contours
          //  List<MatOfPoint> contours = new ArrayList<MatOfPoint>(); //
          //  List<MatOfPoint> cnts = new ArrayList<MatOfPoint>();
          //  Imgproc.findContours(gray, contours, new Mat(), Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);
          //  //System.out.println("Total Contours: "+contours.size());
          //
          //  // Iterate over contours and print only the ones who have a circularity greater than 5
          //  for (int i=0; i<contours.size(); i++){
          //      MatOfPoint2f cont = new MatOfPoint2f(contours.get(i).toArray());
          //      double perimeter = Imgproc.arcLength(cont, true);
          //      double area = Imgproc.contourArea(cont);
          //
          //      if(perimeter ==0){
          //          continue;
          //      }
          //
          //      double circ = (4*Math.PI*area)/(Math.pow(perimeter,2));
          //
          //      if(circ>0.2 && area>20){
          //          //System.out.println("Id"+i);
          //          cnts.add(contours.get(i));
          //          //Mat cr = cropContour(source, cnts, i);
          //          //System.out.println(Test.count(cr));
          //      }
          //
          //
          //      //System.out.println(circ);
          //  }
          // int totalCount = 0;
          //
          //  for (int i=0; i<cnts.size(); i++){
          //      Mat cr = cropContour(source, cnts, i);
          //      int count = Test.count(cr, i);
          //      //Log.d("count",Integer.toString(count));
          //      totalCount += count;
          //  }

           //Log.d("yo", "contours: "+ cnts.size() +" total: "+totalCount);
           //System.out.println();

           //Mat cr = cropContour(source, cnts, 1);
           //Highgui.imwrite("0.jpg", cr);
           //System.out.println("count: "+Test.count(cr));
           //    System.out.println("count: "+Test.count(cr));
           //Highgui.imwrite("3.jpg", cr);
           //System.out.println(cr.dump());


           // Draw contours on gray image
           //Imgproc.drawContours(source, cnts, -1, new Scalar(255,0,0), 4);
           //Test.saveImg("contours.jpg", source);
           //Highgui.imwrite("3.jpg", source);


           // Crop contours and run colony count on them
           // Make class for counting colonies given an image patch as input

       }catch (Exception e) {
           System.out.println("error: " + e.getMessage());
       }
   }

   public static Mat cropContour(Mat img, List<MatOfPoint> contours, int idx){
       Mat black = Mat.zeros(img.rows(), img.cols(), CvType.CV_8UC1);
       //System.out.println(black.rows()+","+black.cols());
       Imgproc.drawContours(black, contours, idx, new Scalar(255,255,255), -1);
       //Highgui.imwrite("5.jpg", black);
       //System.out.println(contours.get(idx));
       Rect bounds = Imgproc.boundingRect(contours.get(idx));
       //System.out.println(img.rows()+" "+img.cols());
       //System.out.println(bounds.x+","+bounds.y+","+bounds.width+","+bounds.height);
       return black.submat(bounds.y-1, bounds.y + bounds.height+1, bounds.x-1, bounds.x+bounds.width+1);
       //return black(Rect(0,0,10,10));
   }
}
