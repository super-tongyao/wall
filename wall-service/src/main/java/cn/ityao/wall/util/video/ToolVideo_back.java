/*
package cn.ityao.wall.util.video;

import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

*/
/**
 * <h5>功能:视频文件处理工具类</h5>
 *
 *//*

public class ToolVideo_back {
    // main函数中slf4j无法正常显示,这里暂时使用log4j2输出日志,迁移到web项目时,建议使用slf4j
//	private static final Logger LOG = LoggerFactory.getLogger(ToolVideo.class);
    private static final org.apache.logging.log4j.Logger LOG = org.apache.logging.log4j.LogManager.getLogger();

    // 获取要取得的帧数
    private static final int GET_FRAMES_LENGTH = 5;

    public static void main(String[] args) {
        Map<String, Object> screenshot = getScreenshot("D:\\resources\\bb2931de-a656-4729-9cc0-52fe8d676016.mov");
        System.out.println(screenshot.toString());
    }

    */
/**
     * <h5>功能:获取一张视频截图并保存同名的jpg文件到视频同目录下</h5>
     *
     * @param filePath 视频文件全路径
     * @return
     *//*

    public static Map<String, Object> getScreenshot(String filePath) {
        return getScreenshot(filePath, null);
    }

    */
/**
     * <h5>功能:获取一张视频截图并保存同名的jpg文件到指定目录</h5>
     *
     * @param filePath 视频文件地址
     * @param saveImagePath 截图的图片存放路径(绝对路径,不包含文件名称)
     * @return
     *//*

    public static Map<String, Object> getScreenshot(String filePath, String saveImagePath) {
        LOG.info("视频文件[{}]截图开始", filePath);

        Map<String, Object> result = new HashMap<String, Object>();
        FFmpegFrameGrabber grabber;
        try {
            grabber = FFmpegFrameGrabber.createDefault(filePath);
            // 第一帧图片存储位置(也是视频路径)
            String targerFilePath = filePath.substring(0, filePath.lastIndexOf("\\"));
            // 视频文件名
            String fileName = filePath.substring(filePath.lastIndexOf("\\") + 1);
            // 图片名称
            String targetFileName = fileName.substring(0, fileName.lastIndexOf("."));
            grabber.start();
            // 视频总帧数
            int videoLength = grabber.getLengthInFrames();

            Frame frame = null;
            int i = 0;
            while (i < videoLength) {
                // 过滤前5帧,避免出现全黑的图片,依自己情况而定(每循环一次取一帧)
                frame = grabber.grabFrame();
                if ((i > GET_FRAMES_LENGTH) && (frame.image != null)) {
                    break;
                }
                i++;
            }

            // 视频旋转度
            String rotate = grabber.getVideoMetadata("rotate");
            Java2DFrameConverter converter = new Java2DFrameConverter();
            // 绘制图片
            BufferedImage bi = converter.getBufferedImage(frame);
            if (rotate != null) {
                // 旋转图片
                bi = rotate(bi, Integer.parseInt(rotate));
            }
            // 图片的类型
            String imageMat = "jpg";
            // 图片的完整路径
            String imagePath = targerFilePath + File.separator + targetFileName + "." + imageMat;
            if (null != saveImagePath && !"".equals(saveImagePath)) {
                // 指定路径
                imagePath = saveImagePath + "." + imageMat;
            }

            // 创建文件
            File output = new File(imagePath);
            ImageIO.write(bi, imageMat, output);

            // 拼接Map信息
            result.put("videoLength", videoLength);	// 视频总帧数
            result.put("videoWide", bi.getWidth());	// 视频的宽
            result.put("videoHigh", bi.getHeight());// 频的高
            long duration = grabber.getLengthInTime() / (1000 * 1000); // 此视频时长(s/秒)
            result.put("rotate", (null == rotate || "".equals(rotate))? "0" : rotate); // 视频的旋转度
            result.put("format", grabber.getFormat()); // 视频的格式
            result.put("imgPath", output.getPath());
            result.put("duration", duration);

            grabber.stop();
            LOG.info("视频文件[{}]截图结束,图片地址为[{}]", filePath, imagePath);
        } catch (IOException e) {
            LOG.error("视频信息帧数处理发生异常 [{}]", e.getMessage());
            e.printStackTrace();
        }

        return result;
    }

    // ==================== private method ====================

    */
/**
     * <h5>功能:根据视频旋转度来调整图片</h5>
     *
     * @param src 捕获的图像
     * @param angel 视频旋转度
     * @return BufferedImage
     *//*

    private static BufferedImage rotate(BufferedImage src, int angel) {
        int src_width = src.getWidth(null);
        int src_height = src.getHeight(null);
        int type = src.getColorModel().getTransparency();
        Rectangle rect_des = calcRotatedSize(new Rectangle(new Dimension(src_width, src_height)), angel);
        BufferedImage bi = new BufferedImage(rect_des.width, rect_des.height, type);
        Graphics2D g2 = bi.createGraphics();
        g2.translate((rect_des.width - src_width) / 2, (rect_des.height - src_height) / 2);
        g2.rotate(Math.toRadians(angel), src_width / 2, src_height / 2);
        g2.drawImage(src, 0, 0, null);
        g2.dispose();
        return bi;
    }

    */
/**
     * <h5>功能:计算图片旋转大小</h5>
     *
     * @param src 屏幕坐标中捕获的矩形区域
     * @param angel 视频旋转度
     * @return
     *//*

    private static Rectangle calcRotatedSize(Rectangle src, int angel) {
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
        return new Rectangle(new Dimension(des_width, des_height));
    }
}
*/
