package dev.wumie.system.actions;

import dev.wumie.system.Action;
import dev.wumie.utils.RandomUtils;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

public class PicAction extends Action {
    public URL file;
    public ImageType type;

    public PicAction(File file) {
        try {
            this.file = file.toURI().toURL();
        } catch (MalformedURLException e) {
            throw new RuntimeException("Failed to get Picture: "+file);
        }
        this.type = ImageType.Photo;
    }

    public PicAction(URL url) {
        this(url,ImageType.Photo);
    }

    public PicAction(URL url, ImageType type) {
        this.file = url;
        this.type = type;
    }

    @Override
    public String toAction() {
        return "[CQ:image,file="+ RandomUtils.randomString("114514bywumie".length())+".image"+",subType="+type.type+",url="+file+"]";
    }

    @Override
    public PicAction get() {
        return this;
    }

    public enum ImageType {
        Photo(0),
        GIF(1);

        public final int type;

        ImageType(int type) {
            this.type = type;
        }
    }
}
