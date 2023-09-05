package cn.ityao.wall.util;

import cn.hutool.core.exceptions.ExceptionUtil;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;

import javax.imageio.ImageIO;
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
    public String writeCover(String resourcePath,String coverPath){
        InputStream inputStream = null;
        String contentType = null;
        String resourceType = "0";
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
                resourceType = "1";
            } else if (contentType.startsWith("video/")) {
                inputStream = videoCover(inputStream,2);
                resourceType = "2";
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
            return resourceType;
        }
    }

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
