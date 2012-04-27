#include <opencv.hpp>
#include <string>

void compute_skew(const char* filename)
{
  // Load in grayscale.
  cv::Mat img = cv::imread(filename, 0);
  cv::Mat original_img = img.clone();

  if (img.empty())
  {
	  throw std::string("Can't read image");
	  return;
  }
	//cv::imshow("Read", img);
  // Binarize
  cv::threshold(img, img, 40, 255, cv::THRESH_BINARY);
  cv::imwrite("binarized.jpg", img);
 //cv::imshow("Binarized", img);
  // Invert colors
  cv::bitwise_not(img, img);
  cv::imwrite("inverted.jpg", img);
  //cv::imshow("Inverted", img);
  cv::Mat element = cv::getStructuringElement(cv::MORPH_RECT, cv::Size(5, 3));
  cv::erode(img, img, element);
  cv::imwrite("eroded.jpg", img);
  //cv::imshow("Eroded", img);
  std::vector<cv::Point> points;

  size_t t = img.elemSize();

  
  cv::Mat_<uchar>::iterator it = img.begin<uchar>();
  cv::Mat_<uchar>::iterator end = img.end<uchar>();
 /* typedef cv::Vec<unsigned char, 3> V4;
  cv::Mat_<V4>::iterator it = img.begin<V4>();
  cv::Mat_<V4>::iterator end = img.end<V4>();*/
  /*cv::Mat_<cv::Vec3b>::iterator it = img.begin<cv::Vec3b>();
  cv::Mat_<cv::Vec3b>::iterator end = img.end<cv::Vec3b>();*/
  for (; it != end; ++it)
	  if (*it)
	  //if ((*it)[0] || (*it)[1] || (*it)[2])
      points.push_back(it.pos());

  cv::RotatedRect box = cv::minAreaRect(cv::Mat(points));

    double angle = box.angle;
    if (angle < -45.)
    angle += 90.;

  cv::Point2f vertices[4];
  box.points(vertices);
  for(int i = 0; i < 4; ++i)
    cv::line(img, vertices[i], vertices[(i + 1) % 4], cv::Scalar(255, 0, 0), 1, CV_AA);
  cv::imwrite("boxed.jpg", img);

  // Deskew
  // rotate
  cv::Mat rot_mat = cv::getRotationMatrix2D(box.center, angle, 1);
  cv::Mat rotated;
  cv::warpAffine(original_img, rotated, rot_mat, original_img.size(), cv::INTER_CUBIC);
  cv::imwrite("rotated.jpg", rotated);
  // crop
  cv::Size box_size = box.size;
  if (box.angle < -45.)
    std::swap(box_size.width, box_size.height);
  cv::Mat cropped;
  box_size.height += 25;
  box_size.width += 25;
  cv::getRectSubPix(rotated, box_size, box.center, cropped);
  cv::imwrite("cropped.jpg", cropped);
  cv::imwrite("original.jpg", original_img);

 
  std::cout << "File " << filename << ": " << angle << std::endl;
  cv::imwrite("result.jpg", img);
  //cv::imshow("Result", img);
  cv::waitKey(0);
}

int main(int argc, char **argv)
{
	compute_skew(argv[1]);
	return 0;
}
