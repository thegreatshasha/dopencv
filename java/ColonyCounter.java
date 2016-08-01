import org.opencv.core.Core;
import org.opencv.core.Size;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Scalar;

import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

import java.util.*;

public class ColonyCounter {
   public static void main( String[] args ){

      try{

         System.loadLibrary( Core.NATIVE_LIBRARY_NAME );
         Mat source = Highgui.imread("/Users/shashwat/workspace/dopencv/images/set1/B_170.jpg",  Highgui.CV_LOAD_IMAGE_COLOR);


         // Crop contours and run colony count on them
         // Make class for counting colonies given an image patch as input

      }catch (Exception e) {
         System.out.println("error: " + e.getMessage());
      }
   }

   public static int count(Mat source){

   }

   public static void check(arr, x,y){
       val = max(arr[x][y], arr[x-1][y], arr[x+1][y], arr[x][y-1], arr[x][y+1], arr[x-1][y-1], arr[x-1][y+1], arr[x+1][y-1], arr[x+1][y+1])
       if val == arr[x][y]:
         return True
       else:
         return False
   }

   // Find the region which has the same value as the current pixel and set it to 255
   public static void dfsset(inp, i, j, pixel) {
       #print i,j,inp[i][j]==pixel,inp[i][j]
       if not (inp[i][j] == pixel):
           return
       else:
           inp[i][j] = 0
       if j>0:
           dfsset(inp, i, j-1, pixel)
       if j<inp.shape[1]-1:
           dfsset(inp, i+1, j, pixel)
       if i>0:
           dfsset(inp, i-1, j, pixel)
       if i<inp.shape[0]-1:
           dfsset(inp, i, j+1, pixel)
    }

   // Regional maxima detection using
   public static void regional_maxima(Mat inp):
       Mat out = inp.copy()

       # Iterate over all pixels
       for i in xrange(1, inp.shape[0]-1):
           for j in xrange(1, inp.shape[1]-1):
               #print i,j,inp[i][j]
               pixel = inp[i][j]

               # If this pixel has already been marked as non minima, screw this
               if out[i][j] == 0:
                   continue

               # Compare pixel with its neighbours in input, if small, do dfs and set to 255
               if check(inp, i, j) == False:
                   dfsset(out, i, j, pixel)

       return out

   public static void main(img_b):
       dist_transform = Imgproc.distanceTransform(img_b,cv2.cv.CV_DIST_L2,3)
       mx = regional_maxima(dist_transform)
       return measure.label(mx, background=0).max()

   public static void cropContour(img, contours, idx):
       cimg = np.zeros_like(img)j
       Imgproc.drawContours(cimg, contours, idx, color=(255,255,255), thickness=-1)
       x,y,w,h = Imgproc.boundingRect(contours[idx])
       return cimg[y-1:y+h+1,x-1:x+w+1]

}
