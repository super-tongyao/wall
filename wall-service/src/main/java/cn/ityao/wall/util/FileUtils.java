package cn.ityao.wall.util;

import com.drew.imaging.jpeg.JpegMetadataReader;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import com.sun.xml.internal.ws.api.addressing.WSEndpointReference;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

/**
 * wall-service >>> 【cn.ityao.wall.util】
 * 文件工具类
 *
 * @author: tongyao
 * @since: 2023-02-16
 */
@Slf4j
public class FileUtils {


    public String getFileSuffix(MultipartFile multipartFile){
        String fileName = multipartFile.getOriginalFilename();
        String suffix = fileName.substring(fileName.lastIndexOf(".")+1,fileName.length());
        return suffix;
    }

    // 资源和封面的文件写入
    public void writeFile(MultipartFile multipartFile,String path){
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(path);
            IOUtils.copy(multipartFile.getInputStream(), fileOutputStream);
            IOUtils.closeQuietly(fileOutputStream);
            fileOutputStream.close();
        } catch (IOException e) {
            throw new RuntimeException("存储文件异常，可能最近修改了文件存储路径，请重启Wall服务！");
        }
    }

    /**
     * 压缩后的资源和封面写入
     * @param bufferedImage
     * @param path
     */
    public void writeFile(BufferedImage bufferedImage, String path){
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
    }

    public static BufferedImage rotateImage(BufferedImage bufferedImage, int angel) {
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
    }
    //计算旋转后的尺寸
    private static Rectangle calculatorRotatedSize(Rectangle src, int angel) {
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
    }

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


}
