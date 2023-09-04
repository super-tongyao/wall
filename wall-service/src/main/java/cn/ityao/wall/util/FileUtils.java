package cn.ityao.wall.util;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.io.FileUtil;
import com.drew.imaging.jpeg.JpegMetadataReader;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import com.sun.xml.internal.ws.api.addressing.WSEndpointReference;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;


/**
 * wall-service >>> 【cn.ityao.wall.util】
 * 文件工具类
 *
 * 作者注：如果你看到本类代码，觉得写的很优雅的话，那么不用质疑，这是作者舍弃旧代码花了3天时间又重新全部重构了一遍代码！
 *
 * @author: tongyao
 * @since: 2023-02-16
 */
@Slf4j
public class FileUtils {

    /**
     * 获取文件名称后缀
     * @param fileName
     * @return
     */
    public String getFileSuffix(String fileName){
        String suffix = fileName.substring(fileName.lastIndexOf(".")+1,fileName.length());
        return suffix.toLowerCase();
    }

    /**
     * 资源和封面的文件写入
     * @param inputStream
     * @param path
     */
    public void writeFile(InputStream inputStream,String path){
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(path);
            IOUtils.copy(inputStream, fileOutputStream);
            IOUtils.closeQuietly(fileOutputStream);
            fileOutputStream.close();
        } catch (IOException e) {
            throw new RuntimeException("存储文件异常，可能最近修改了文件存储路径，请重启Wall服务！详细错误："+ExceptionUtil.stacktraceToString(e));
        }
    }

    /**
     * 根据本地上传类型或者图床URL写入图片/视频封面资源
     * @param resourcePath
     * @param coverPath
     */
    public void writeCover(String resourcePath,String coverPath){
        InputStream inputStream = null;
        String contentType = null;
        try {
            // 看是本地上传还是图床URL
            if (resourcePath.indexOf("http") != -1){
                URL url = new URL(resourcePath);
                URLConnection connection = url.openConnection();
                inputStream = connection.getInputStream();
                contentType = connection.getContentType();
            }else{
                inputStream = new FileInputStream(new File(resourcePath));
                contentType = Files.probeContentType(Paths.get(resourcePath));
            }
            // 看是视频还是图片
            if (contentType.startsWith("image/")) {
                // 这里不要问为什么不写，因为不想写 - -
            } else if (contentType.startsWith("video/")) {
                inputStream = videoCover(inputStream,2);
            }else{
                throw new RuntimeException("未知的资源存储类型：" + contentType);
            }
            // 不管图床URL还是本地上传 统一 保存 inputStream 写入到封面路径
            writeFile(inputStream,coverPath);
        } catch (IOException e) {
            throw new RuntimeException(ExceptionUtil.stacktraceToString(e));
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                throw new RuntimeException(ExceptionUtil.stacktraceToString(e));
            }
        }
    }

    /**
     * 压缩后的资源和封面写入
     * @param bufferedImage
     * @param path
     */
    /*public void writeFile(BufferedImage bufferedImage, String path){
        try {

            FileOutputStream fileOutputStream = new FileOutputStream(path);
            JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(fileOutputStream);

            // 图像获取旋转角度和旋转图像，有问题，请启动项目上传手机照片测试，英雄好汉请助手相助！
            //getImgRotateAngle(bufferedImage)
            //bufferedImage = rotateImage(bufferedImage,90);

            encoder.encode(bufferedImage);
            fileOutputStream.close();
        } catch (IOException e) {
            throw new RuntimeException("存储文件异常，可能最近修改了文件存储路径，请重启Wall服务！");
        }
    }*/

    /*public static BufferedImage rotateImage(BufferedImage bufferedImage, int angel) {
        if (bufferedImage == null) {
            return null;
        }
        if (angel < 0) {
            // 将负数角度，纠正为正数角度
            angel = angel + 360;
        }
        int imageWidth = bufferedImage.getWidth(null);
        int imageHeight = bufferedImage.getHeight(null);
        // 计算重新绘制图片的尺寸
        Rectangle rectangle = calculatorRotatedSize(new Rectangle(new Dimension(imageWidth, imageHeight)), angel);
        // 获取原始图片的透明度
        int type = bufferedImage.getColorModel().getTransparency();
        BufferedImage newImage = null;
        newImage = new BufferedImage(rectangle.width, rectangle.height, type);
        Graphics2D graphics = newImage.createGraphics();
        // 平移位置
        graphics.translate((rectangle.width - imageWidth) / 2, (rectangle.height - imageHeight) / 2);
        // 旋转角度
        graphics.rotate(Math.toRadians(angel), imageWidth / 2, imageHeight / 2);
        // 绘图
        graphics.drawImage(bufferedImage, null, null);
        return newImage;
    }*/
    //计算旋转后的尺寸
    /*private static Rectangle calculatorRotatedSize(Rectangle src, int angel) {
        if (angel >= 90) {
            if (angel / 90 % 2 == 1) {
                int temp = src.height;
                src.height = src.width;
                src.width = temp;
            }
            angel = angel % 90;
        }
        double r = Math.sqrt(src.height * src.height + src.width * src.width) / 2;
        double len = 2 * Math.sin(Math.toRadians(angel) / 2) * r;
        double angel_alpha = (Math.PI - Math.toRadians(angel)) / 2;
        double angel_dalta_width = Math.atan((double) src.height / src.width);
        double angel_dalta_height = Math.atan((double) src.width / src.height);
        int len_dalta_width = (int) (len * Math.cos(Math.PI - angel_alpha - angel_dalta_width));
        int len_dalta_height = (int) (len * Math.cos(Math.PI - angel_alpha - angel_dalta_height));
        int des_width = src.width + len_dalta_width * 2;
        int des_height = src.height + len_dalta_height * 2;
        return new java.awt.Rectangle(new Dimension(des_width, des_height));
    }*/

    // 获取图片旋转角度
    /*public static Integer getImgRotateAngle(BufferedImage bufferedImage){
        Integer angel = 0;
        Metadata metadata = null;
        try{

            ByteArrayOutputStream os = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "jpg", os);
            InputStream is = new ByteArrayInputStream(os.toByteArray());

            metadata = JpegMetadataReader.readMetadata(is);
            Directory directory = metadata.getDirectory(ExifDirectory.class);
            if(directory != null && directory.containsTag(ExifDirectory.TAG_ORIENTATION)){
                int orientation = directory.getInt(ExifDirectory.TAG_ORIENTATION);
                // 原图片的方向信息
                if(6 == orientation ){
                    //6旋转90
                    angel = 90;
                }else if( 3 == orientation){
                    //3旋转180
                    angel = 180;
                }else if( 8 == orientation){
                    //8旋转90
                    angel = 270;
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return angel;
    }*/

    /**
     * 获取视频帧数
     * @param inputStream 输入流
     * @param frameCount 帧数
     * @return
     */
    public InputStream videoCover(InputStream inputStream,int frameCount) {
        int flag = 0;
        Frame frame = null;
        try {
            FFmpegFrameGrabber fFmpegFrameGrabber = new FFmpegFrameGrabber(inputStream);
            fFmpegFrameGrabber.start();
            // 获取视频的总帧数
            int lengthInFrames = fFmpegFrameGrabber.getLengthInFrames();
            double frameNumber = fFmpegFrameGrabber.getFrameRate();
            while (flag <= lengthInFrames) {
                frame = fFmpegFrameGrabber.grabImage();
                // 帧数截取
                if (frame != null && flag == frameCount) {
                    Java2DFrameConverter converter = new Java2DFrameConverter();
                    BufferedImage bufferedImage = converter.getBufferedImage(frame);
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    ImageIO.write(bufferedImage, "jpg", byteArrayOutputStream);
                    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
                    inputStream = byteArrayInputStream;
                    break;
                }
                flag++;
            }
            fFmpegFrameGrabber.stop();
        } catch (Exception e) {
            throw new RuntimeException("在获取视频帧时失败：" + ExceptionUtil.stacktraceToString(e));
        }
        return inputStream;
    }

    public static void main(String[] args) throws Exception {
        Thumbnails.of("C:\\wall-test233\\57eacccd-3610-4d68-a446-9893623d8dd2.jpg").scale(1).toFile("D:\\222_t2.jpg");

        Thumbnails.of("C:\\wall-test233\\57eacccd-3610-4d68-a446-9893623d8dd2.jpg").size(376,600).toFile("D:\\222_t.jpg");

    }

}
