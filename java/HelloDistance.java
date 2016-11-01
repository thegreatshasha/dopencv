import org.opencv.core.Core;
import org.opencv.core.Size;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Scalar;
import org.opencv.core.Point;
import java.io.PrintWriter;

import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;


import java.util.*;
import java.io.File;

public class HelloDistance extends Test{

  public static int countColonies(String filePath, String fileName){
    try{
     long time1, time2;
      // Raw image without mask
     Mat source = Highgui.imread(filePath, Highgui.CV_LOAD_IMAGE_COLOR);
     time1 = System.nanoTime();
     source = Test.scaledResize(source, 1000);
     time2 = System.nanoTime();
     //System.out.println("read: " + (time2/1000000 - time1/1000000) + " ms");

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
     time1 = System.nanoTime();
     Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, new Size(50,50));
     Imgproc.morphologyEx(gray, tophat, Imgproc.MORPH_TOPHAT, kernel);
     Test.saveImg(fileName+"_tophat.jpg", tophat);
     time2 = System.nanoTime();
     //System.out.println("tophat: " + (time2/1000000 - time1/1000000) + " ms");

     // Blur before thresholding
     time1 = System.nanoTime();
     Imgproc.GaussianBlur(tophat, blur, new Size(5,5), 0);
     Test.saveImg(fileName+"_blurred.jpg", blur);
     time2 = System.nanoTime();
     //System.out.println("gaussianblur: " + (time2/1000000 - time1/1000000) + " ms");

     // Apply mask on the image
     time1 = System.nanoTime();
     Point center = new Point(source.cols()/2, source.rows()/2);
     Scalar maskColor = new Scalar(255, 255, 255);

     Core.circle(mask, center, Math.min(source.rows()/2, source.cols()/2) - 15, maskColor, -1);
     Test.saveImg(fileName+"_mask.jpg", mask);
     blur.copyTo(tophat_mask, mask);
     Core.bitwise_and(blur, blur, tophat_mask, mask);
     Test.saveImg(fileName+"_tophat_mask.jpg", tophat_mask);
     time2 = System.nanoTime();
     //System.out.println("mask: " + (time2/1000000 - time1/1000000) + " ms");

     // Otsu thresholding on the tophat image
     time1 = System.nanoTime();
     Imgproc.threshold(tophat_mask,gray,0,255,Imgproc.THRESH_BINARY|Imgproc.THRESH_OTSU);
     Test.saveImg(fileName+"_threshold.png", gray);
     time2 = System.nanoTime();
     //System.out.println("threshold: " + (time2/1000000 - time1/1000000) + " ms");

     // Find contours
     time1 = System.nanoTime();
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
     time2 = System.nanoTime();
     //System.out.println("find contours: " + (time2/1000000 - time1/1000000) + " ms");

     time1 = System.nanoTime();
     Mat black = Mat.zeros(gray.rows(), gray.cols(), CvType.CV_8UC1);
     Imgproc.drawContours(black, cnts, -1, new Scalar(255,255,255), -1);
     Test.saveImg(fileName+"_black_contours.png", black);
     time2 = System.nanoTime();
     //System.out.println("draw contours: " + (time2/1000000 - time1/1000000) + " ms");

     time1 = System.nanoTime();
     // Do the distance trnasform and count
     Imgproc.distanceTransform(black, dt, Imgproc.CV_DIST_L2, Imgproc.CV_DIST_MASK_PRECISE);
     Test.saveImg(fileName+"_distance_transform.png", dt);
     time2 = System.nanoTime();
     //System.out.println("distance transform: " + (time2/1000000 - time1/1000000) + " ms");

     time1 = System.nanoTime();
     rm = Test.regional_maxima(dt);
     Test.saveImg(fileName+"_regional_maxima.png", rm);
     time2 = System.nanoTime();
     //System.out.println("regional max: " + (time2/1000000 - time1/1000000) + " ms");

     time1 = System.nanoTime();
     Test.label(rm);
     Test.saveImg(fileName+"_label.png", rm);
     //System.out.println("label: " + (time2/1000000 - time1/1000000) + " ms");
     //System.out.println(rm.dump());
     time1 = System.nanoTime();
     Core.MinMaxLocResult mmr = Core.minMaxLoc(rm);
     int count = (int)mmr.maxVal-1;
     time2 = System.nanoTime();
     //System.out.println("minmax: " + (time2/1000000 - time1/1000000) + " ms");
     //System.out.println(count);

     time1 = System.nanoTime();
     //Imgproc.drawContours(source, cnts, -1, new Scalar(255,0,0), 2);
     Mat bgr = new Mat(rm.size(), CvType.CV_8UC3, new Scalar(0,0,0));
     kernel = Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, new Size(5,5));
     Imgproc.dilate(rm, rm, kernel);
     rm.convertTo(rm, CvType.CV_8U);
     bgr.setTo(new Scalar(0,0,255), rm);
     bgr.copyTo(source, rm);
     Test.saveImg(fileName+"_final.png", source);
     time2 = System.nanoTime();
     //System.out.println("drawfinal: " + (time2/1000000 - time1/1000000) + " ms");

     // Release all matrices
     destination.release();
     gray.release();
     blur.release();
     tophat.release();
     dt.release();
     mask.release();
     tophat_mask.release();
     rm.release();
     bgr.release();

     return count;

    }catch (Exception e) {
        System.out.println("error: " + e.getMessage());

        return 0;
    }
  }

  public static String generateHtmlRow(String fileName, int trueCount, int predCountm, double accuracy) {
    String html = "<tr><td>"+accuracy+"</td><td><img src=\""+trueCount+".jpg_final.png\"></td><td><img src=\""+trueCount+".jpg_tophat_mask.jpg\"/></td><td><img src=\""+trueCount+".jpg_threshold.png\"/></td><td><img src=\""+trueCount+".jpg_black_contours.png\"/></td></td></tr>";
    return html;
  }

  public static void main(String[] args){
    System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

    // Read files from dataset
    File folder = new File("/Users/shashwat/workspace/dopencv/java/dataset/");
    File[] listOfFiles = folder.listFiles();
    double errorSum = 0.0;
    double varSum = 0.0;
    int count = 0;
    String html = "<html><head><meta charset=\"utf-8\"><title></title></head><body><table><tbody><tr><th>Accuracy</th><th>Final</th><th>Tophat</th><th>Threshold</th><th>Contours</th></tr><tr>";

    for (File file : listOfFiles) {
        if (file.isFile()) {
            String filePath = file.getAbsolutePath();
            String fileName = file.getName();
            int trueCount = Integer.parseInt(fileName.replace(".jpg",""));
            int predCount = countColonies(filePath, fileName);

            double deviation = 100*((double)Math.abs(trueCount - predCount))/((double) trueCount);
            errorSum += (100-deviation);
            varSum += deviation*deviation;
            count += 1;
            System.out.println(trueCount + "->" + predCount + " percent: " + (100-deviation));

            html += generateHtmlRow(fileName, trueCount, predCount, 100-deviation);
        }
    }

    html += "</tr><tr><h3>Avg accuracy: " + errorSum/count + " Variance: "+varSum/count+"</h3></tr></tbody></table></body></html>";



    try{
        PrintWriter writer = new PrintWriter("report.html", "UTF-8");
        writer.println(html);
        writer.close();
    } catch (Exception e) {
       // do something
    }

    //System.out.println(html);
    System.out.println("Net accuracy: "+errorSum/count + " variance: "+varSum/count);

    // Write hmtl to file
    //int count = countColonies("/Users/shashwat/Downloads/75.jpg");
   }
}
