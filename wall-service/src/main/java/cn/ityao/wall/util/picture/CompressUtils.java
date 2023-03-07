package cn.ityao.wall.util.picture;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * wall-service >>> 【cn.ityao.wall.util】
 * 图片压缩
 *
 * @author: tongyao
 * @since: 2023-02-16
 */
@Slf4j
public class CompressUtils {

    public BufferedImage commpressPicture(MultipartFile multipartFile){
        try {
            BufferedImage templateImage = ImageIO.read(multipartFile.getInputStream());

            //原始图片的长度和宽度
            int height = templateImage.getHeight();
            int width = templateImage.getWidth();

            //通过比例压缩
            float scale = 0.5f;

            //通过固定长度压缩
            /*int doWithHeight = 100;
            int dowithWidth = 300;*/

            //压缩之后的长度和宽度
            int doWithHeight = (int) (scale * height);
            int dowithWidth = (int) (scale * width);

            BufferedImage finalImage = new BufferedImage(dowithWidth, doWithHeight, BufferedImage.TYPE_INT_RGB);
            finalImage.getGraphics()
                    .drawImage(
                            templateImage.getScaledInstance(dowithWidth, doWithHeight, java.awt.Image.SCALE_SMOOTH)
                            , 0, 0, null);

            return finalImage;
        } catch (IOException e) {
            log.error("图片压缩异常："+e.getMessage());
        }
        return null;
    }


    public BufferedImage commpressPicture(BufferedImage templateImage){
        //原始图片的长度和宽度
        int height = templateImage.getHeight();
        int width = templateImage.getWidth();

        //通过比例压缩
        float scale = 1f;

        //通过固定长度压缩
            /*int doWithHeight = 100;
            int dowithWidth = 300;*/

        //压缩之后的长度和宽度
        int doWithHeight = (int) (scale * height);
        int dowithWidth = (int) (scale * width);

        BufferedImage finalImage = new BufferedImage(dowithWidth, doWithHeight, BufferedImage.TYPE_INT_RGB);
        finalImage.getGraphics().drawImage(templateImage.getScaledInstance(dowithWidth, doWithHeight, java.awt.Image.SCALE_SMOOTH), 0, 0, null);

        return finalImage;
    }


}
