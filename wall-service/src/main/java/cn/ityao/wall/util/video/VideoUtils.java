package cn.ityao.wall.util.video;

import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * wall-service >>> 【cn.ityao.wall.util.video】
 * 视频截取封面
 *
 * @author: tongyao
 * @since: 2023-02-16
 */
public class VideoUtils {

    public BufferedImage getCover(String videoPath){
        BufferedImage bufferedImage = null;

        try {
            FFmpegFrameGrabber ff = new FFmpegFrameGrabber(videoPath);
            ff.start();
            int lenght = ff.getLengthInFrames();
            //获取视频时常
            int middleFrame = lenght / 2;
            int i = 0;
            Frame f = null;
            while (i < lenght) {
                // 获取中间帧数得图片，避免出现全黑的图片，依自己情况而定
                f = ff.grabFrame();
                if ((i > middleFrame) && (f.image != null)) {
                    break;
                }
                i++;
            }
            opencv_core.IplImage img = f.image;
            int owidth = img.width();
            int oheight = img.height();
            // 对截取的帧进行等比例缩放
            int width = 800;
            int height = (int) (((double) width / owidth) * oheight);


            bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
            bufferedImage.getGraphics().drawImage(f.image.getBufferedImage().getScaledInstance(width, height, Image.SCALE_SMOOTH),
                    0, 0, null);
            ff.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bufferedImage;
    }
}
