import org.opencv.core.Core;
import org.opencv.core.Size;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Scalar;

import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

import java.util.Stack;

public class Test {
  // static{
  //   System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
  // }

  public static void main(String[] args) {
    //System.out.println("Hello, OpenCV");

    // Load the native library.
    // Mat source = Highgui.imread("/Users/shashwat/Downloads/watershed1.tiff");
    // source = Test.scaledResize(source, 500);
    // Imgproc.cvtColor(source, source , Imgproc.COLOR_BGR2GRAY);
    // Imgproc.threshold(source,source,0,255,Imgproc.THRESH_BINARY_INV|Imgproc.THRESH_OTSU);
    //System.out.println(count(source, 0));
    // Now to label and print connected components

    //label(source);
  }

  public static int getPixel(Mat img, int i, int j){
    try{
      int val = (int)img.get(i,j)[0];
      return val;
    }
    catch(Exception e){
      System.out.println(e);
      return 0;
    }
  }

  public static Mat scaledResize(Mat img, int w_new){
    Mat resized_image = new Mat();
    int h_new = (img.rows()*w_new)/img.cols();
    Imgproc.resize(img, resized_image, new Size(w_new, h_new));
    return resized_image;
  }

  public static void saveImg(String name, Mat img){
    //System.out.println("Saving: "+name);
    Mat img2 = copyToBlack(img);//img.clone();
    Core.normalize(img2, img2, 0, 255, Core.NORM_MINMAX,  CvType.CV_8UC1);
    Highgui.imwrite(name, img2);
  }
  // public static int count_new(Mat source, int index){
  //       Mat dt = new Mat(source.rows(), source.cols(), CvType.CV_8UC1);
  //       //Imgproc.cvtColor(source, source , Imgproc.COLOR_BGR2GRAY);
  //       //Imgproc.threshold(source,source,0,255,Imgproc.THRESH_BINARY|Imgproc.THRESH_OTSU);
  //       String name1 = "thresh"+String.valueOf(index)+".png";
  //       String name2 = "dt"+String.valueOf(index)+".png";
  //       String name3 = "max"+String.valueOf(index)+".png";
  //       saveImg("dtorig.png", dt);
  //       //Log.v("success", name1);
  //       //Log.v("success", name2);
  //       //Log.v("success", name2);
  //       saveImg(name1, source);
  //
  //       //System.out.println(source.dump());
  //       // Operate on the distance transform image now
  //       //dt = source;
  //       Imgproc.distanceTransform(source, source, Imgproc.CV_DIST_L2, 3);
  //       saveImg(name2, dt);
  //       // Cool so distance transform works fine
  //       Mat rm = regional_maxima(dt);
  //
  //       Imgproc.threshold(rm,rm,0,1,Imgproc.THRESH_BINARY);
  //
  //       Mat element1 = Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, new  Size(2,2));
  //       Imgproc.dilate(rm, rm, element1);
  //
  //       //System.out.println(rm.dump());
  //       label(rm);
  //       Core.MinMaxLocResult mmr = Core.minMaxLoc(rm);
  //       //System.out.println(rm);
  //       int count = (int)mmr.maxVal-1;
  //       saveImg(name3, rm);
  //       return count;
  //       //return 0;
  //   }

  // public static int count(Mat source, int i){
  //   //Imgproc.cvtColor(source, source , Imgproc.COLOR_BGR2GRAY);
  //   Imgproc.threshold(source,source,0,255,Imgproc.THRESH_BINARY|Imgproc.THRESH_OTSU);
  //   saveImg("1.jpg", source);
  //
  //   //System.out.println(source.dump());
  //   // Operate on the distance transform image now
  //   Imgproc.distanceTransform(source, source, Imgproc.CV_DIST_L2, 3);
  //   System.out.println(source.dump());
  //   Core.normalize(source, source, 0, 255, Core.NORM_MINMAX,  CvType.CV_8UC1);
  //   saveImg("2.jpg", source);
  //   // Cool so distance transform works fine
  //   Mat rm = regional_maxima(source);
  //
  //   Imgproc.threshold(rm,rm,0,1,Imgproc.THRESH_BINARY);
  //
  //   Mat element1 = Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, new  Size(2,2));
  //   Imgproc.dilate(rm, rm, element1);
  //
  //   //System.out.println(rm.dump());
  //   label(rm);
  //   //Imgproc.dilate(rm, )
  //   //Core.normalize(rm, rm, 0, 255, Core.NORM_MINMAX,  CvType.CV_8UC1);
  //   //Highgui.imwrite("3.jpg", rm);
  //
  //   //System.out.println(rm.dump());
  //   saveImg("3.jpg", rm);
  //
  //
  //
  //   Core.MinMaxLocResult mmr = Core.minMaxLoc(rm);
  //   //System.out.println(rm);
  //   int count = (int)mmr.maxVal-1;
  //   saveImg("3.jpg", rm);
  //   return count;
  //   //return 0;
  // }

  //Returns 1 for pixels marked as regional maxima and 0 for non maxima pixels
  // public static void dfsset_stack(Mat inp, int i, int j, int pixel) {
  //     if(getPixel(inp, i, j) != pixel)
  //         return;
  //     else
  //         inp.put(i,j,0);
  //     if(j>0)
  //         dfsset(inp, i, j-1, pixel);
  //     if(j<inp.cols()-1)
  //         dfsset(inp, i, j+1, pixel);
  //     if(i>0)
  //         dfsset(inp, i-1, j, pixel);
  //     if(i<inp.rows()-1)
  //         dfsset(inp, i+1, j, pixel);
  //  }

   // Code review is the best way to spot bugs
   // Stack based version of dfsset to avoid running into memory leaks
  //  public static void dfsset_old_confirmed(Mat inp, int i, int j, int pixel) {
  //     Node parent = new Node(i, j);
   //
  //     Stack<Node> st = new Stack<Node>();
   //
  //     st.push(parent);
   //
  //     while(!st.empty()){
  //         // Equivalent dfsset code here
  //         Node node = st.pop();
   //
  //         // Color all pixels with the value=pixel
  //         if(getPixel(inp, node.i, node.j) == pixel){
  //           // Set this node as black
  //           inp.put(node.i, node.j, 0);
   //
  //           if(node.j>0)
  //               st.push(new Node(node.i, node.j-1));
  //           if(node.j<inp.cols()-1)
  //               st.push(new Node(node.i, node.j+1));
  //           if(node.i>0)
  //               st.push(new Node(node.i-1, node.j));
  //           if(node.i<inp.rows()-1)
  //               st.push(new Node(node.i+1, node.j));
  //         }
  //     }
  //  }

   public static void dfsset(Mat inp, int i, int j, int pixel) {
        Node parent = new Node(i, j);

        Stack<Node> st = new Stack<Node>();

        st.push(parent);

        while(!st.empty()){
            // Equivalent dfsset code here
            Node node = st.pop();

            // Color all pixels with the value=pixel
            if(getPixel(inp, node.i, node.j) == pixel){
                // Set this node as black
                inp.put(node.i, node.j, 0);

                if(node.j>0)
                    st.push(new Node(node.i, node.j-1));
                if(node.j<inp.cols()-1)
                    st.push(new Node(node.i, node.j+1));
                if(node.i>0)
                    st.push(new Node(node.i-1, node.j));
                if(node.i<inp.rows()-1)
                    st.push(new Node(node.i+1, node.j));
            }
        }
    }

  // Regional maxima detection using
  public static Mat regional_maxima(Mat inp){
      Mat out = inp.clone();

      // Iterate over all pixels
      for(int i = 1; i< inp.rows()-1; i++){
        for(int j=1; j<inp.cols()-1; j++){
              int pixel = getPixel(inp, i, j);

              // If this pixel has already been marked as non minima, screw this
              if((int)out.get(i,j)[0] == 0)
                  continue;

              // Compare pixel with its neighbours in input, if small, do dfs and set to 255
              if(check(inp, i, j) == false)
                  dfsset(out, i, j, pixel);
        }
      }
      Imgproc.threshold(out, out, 0,1, Imgproc.THRESH_BINARY);
      return out;
  }

  public static Mat copyToBlack(Mat mat){
    Mat black = Mat.zeros(mat.rows(), mat.cols(), CvType.CV_8UC1);
    mat.copyTo(black);
    return black;
  }

  public static Boolean check(Mat inp, int i_p, int j_p){
      int max = -1;

      for(int i = i_p-1; i<=i_p+1; i++){
        for(int j=j_p-1; j<=j_p+1; j++){
            int pxl = getPixel(inp, i, j);
            max = Math.max(max, pxl);
        }
      }

      if(getPixel(inp, i_p, j_p) == max)
        return true;
      else
        return false;
  }

  // public static void dfsset2_deprecated(Mat damon, int i, int j, int value){
  //
  //   //System.out.println(i+","+j);
  //   int pixel = getPixel(damon, i, j);
  //   //System.out.println("pixel:"+pixel);
  //   if(pixel==1){
  //     damon.put(i, j, value);
  //
  //     if(j>0)
  //         dfsset2(damon, i, j-1, value);
  //     if(j<damon.cols()-1)
  //         dfsset2(damon, i, j+1, value);
  //     if(i>0)
  //         dfsset2(damon, i-1, j, value);
  //     if(i<damon.rows()-1)
  //         dfsset2(damon, i+1, j, value);
  //     if(i>0 && j>0)
  //         dfsset2(damon, i-1, j-1, value);
  //     if(i>0 && j<damon.cols())
  //         dfsset2(damon, i-1, j+1, value);
  //     if(i<damon.rows() && j>0)
  //         dfsset2(damon, i+1, j-1, value);
  //     if(i<damon.rows() && j<damon.cols())
  //         dfsset2(damon, i+1, j+1, value);
  //   }
  // }

  public static void dfsset2(Mat damon, int i, int j, int label){
        Node parent = new Node(i, j);

        Stack<Node> st = new Stack<Node>();

        st.push(parent);

        while(!st.empty()){
            // Process current pixel node
            Node node = st.pop();

            int pixel = getPixel(damon, node.i, node.j);

            // If it's a white connected pixel, label it and add it's neighbours
            if(pixel==1){
                damon.put(node.i, node.j, label);

                // Also label the 8 connected neighbourhood of this pixel
                if(node.j>0)
                    st.push(new Node(node.i, node.j-1));
                if(node.j<damon.cols()-1)
                    st.push(new Node(node.i, node.j+1));
                if(node.i>0)
                    st.push(new Node(node.i-1, node.j));
                if(node.i<damon.rows()-1)
                    st.push(new Node(node.i+1, node.j));
                if(node.i>0 && node.j>0)
                    st.push(new Node(node.i-1, node.j-1));
                if(node.i>0 && node.j<damon.cols())
                    st.push(new Node(node.i-1, node.j+1));
                if(node.i<damon.rows() && node.j>0)
                    st.push(new Node(node.i+1, node.j-1));
                if(node.i<damon.rows() && node.j<damon.cols())
                    st.push(new Node(node.i+1, node.j+1));
            }
        }
    }

  // public static void dfsset2(Mat damon, int i, int j, int label){
  //   Node parent = new Node(i, j);
  //
  //   Stack<Node> st = new Stack<Node>();
  //
  //   st.push(parent);
  //
  //   while(!st.empty()){
  //     // Process current pixel node
  //     Node node = st.pop();
  //
  //     int pixel = getPixel(damon, node.i, node.j);
  //
  //     // If it's a white connected pixel, label it and add it's neighbours
  //     if(pixel==1){
  //         damon.put(node.i, node.j, label);
  //
  //         // Also label the 8 connected neighbourhood of this pixel
  //         if(node.j>0)
  //             st.push(new Node(node.i, node.j-1));
  //         if(node.j<damon.cols()-1)
  //             st.push(new Node(node.i, node.j+1));
  //         if(node.i>0)
  //             st.push(new Node(node.i-1, node.j));
  //         if(node.i<damon.rows()-1)
  //             st.push(new Node(node.i+1, node.j));
  //         if(node.i>0 && node.j>0)
  //             st.push(new Node(node.i-1, node.j-1));
  //         if(node.i>0 && node.j<damon.cols())
  //             st.push(new Node(node.i-1, node.j+1));
  //         if(node.i<damon.rows() && node.j>0)
  //             st.push(new Node(node.i+1, node.j-1));
  //         if(node.i<damon.rows() && node.j<damon.cols())
  //             st.push(new Node(node.i+1, node.j+1));
  //     }
  //   }
  // }

  // 4 connectivity connected componenets labelling
  public static void label(Mat damon) {
    //System.out.println(damon.rows()+","+damon.cols());
    //System.out.println(damon.dump());
    int label = 2;

    for(int i = 0; i< damon.rows(); i++){
      for(int j=0; j<damon.cols(); j++){
        int pixel = getPixel(damon, i, j);
        if(pixel==1){
          dfsset2(damon, i, j, label);
          label += 1;
        }
        //System.out.println((int)damon.get(i,j)[0]) ;
      }
    }

    //System.out.println(damon.dump());

  }
}
