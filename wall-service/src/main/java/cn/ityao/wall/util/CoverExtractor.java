/*
package cn.ityao.wall.util;

*/
/**
 * wall-service >>> 【cn.ityao.wall.util】
 *
 * @author: tongyao
 * @since: 2023-08-22 14:13
 *//*

import net.coobird.thumbnailator.Thumbnails;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

public class CoverExtractor {
    public static InputStream extractCover(InputStream inputStream, String outputPath) throws IOException {
        // 将输入流保存为临时文件
        File tempFile = File.createTempFile("temp", null);
        FileOutputStream fileOutputStream = new FileOutputStream(tempFile);
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            fileOutputStream.write(buffer, 0, bytesRead);
        }
        fileOutputStream.close();

        // 提取封面
        Image coverImage;
        if (outputPath.toLowerCase().endsWith(".mp4") || outputPath.toLowerCase().endsWith(".mov") ||
                outputPath.toLowerCase().endsWith(".avi")) {
            coverImage = getVideoCover(tempFile.getAbsolutePath());
        } else {
            coverImage = getImageCover(tempFile.getAbsolutePath());
        }

        // 压缩封面图片
        BufferedImage resizedImage = Thumbnails.of(coverImage)
                .size(200, 200)  // 设置封面图片的宽高
                .asBufferedImage();

        // 将压缩后的图片保存到输出路径
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(resizedImage, "jpeg", outputStream);

        // 删除临时文件
        tempFile.delete();

        // 返回压缩后的图片作为InputStream流
        return new ByteArrayInputStream(outputStream.toByteArray());
    }

    private static Image getVideoCover(String videoPath) {
        // 使用你喜欢的方法获取视频封面，这里使用的是某个假设的方法
        // 这里需要调用第三方库或自定义代码来提取视频封面
        // 可以参考FFmpeg、OpenCV等库来实现
        // 返回一个Image对象作为视频的封面图像
        return null;
    }

    private static Image getImageCover(String imagePath) throws IOException {
        // 从给定的路径读取图像文件
        BufferedImage image = ImageIO.read(new File(imagePath));

        // 返回BufferedImage作为图片的封面
        return image;
    }

    public static void main(String[] args) {
        try {
            // 示例：从输入流中提取图片或视频封面并进行压缩
            InputStream inputStream = new FileInputStream("C:\\wall-test233\\70ba43d1-3b8c-4648-bcc4-9230d3c2586b.wmv");
            InputStream coverInputStream = extractCover(inputStream, "D:\\");

            // 在这里可以使用你的专门保存inputStream流的方法来保存coverInputStream

            // 关闭输入流
            inputStream.close();
            coverInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

*/
