import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.awt.AWTTextureIO;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class ImageResource {

    private Texture texture = null;
    private BufferedImage image = null;
    public float getWidthImage(){
        return image.getWidth();
    }
    public float getHeightImage(){
        return image.getHeight();
    }
    public ImageResource(String path){
        //path is relative to java project this way
        //images in res zetten lukt niet, dan lijkt url naar image null te zijn om een of andere reden
        //System.out.println("woensel: " + path);
        try{
            image = ImageIO.read(ImageResource.class.getResource(path));
        } catch (IOException e){
            e.printStackTrace();
        }
        //raar, maar goed voor memory ofzoiets voor tutorial
        if (image != null){
            image.flush();
        }
    }

    //pas texture aanmaken als ie wordt opgeroepen om geheugen te besparen
    public Texture getTexture(){
        if (image == null) return null;
        if (texture == null) texture = AWTTextureIO.newTexture(Renderer.getProfile(), image, true);
        return texture;
    }
}
