package org.occ.Image;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import org.libtiff.jai.codec.XTIFFField;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import com.bbn.openmap.dataAccess.image.geotiff.GeoTIFFFile;
import com.sun.media.jai.codec.ImageCodec;
import com.sun.media.jai.codec.ImageDecoder;
import com.sun.media.jai.codec.TIFFDecodeParam;

@Deprecated
public class ReadTIFF {

    public static void main(String[] args) throws IOException {
        String filename;
        if (args.length > 0) {
            filename = args[0];
        } else {
            filename = "/devel/data/GeoTIFF/USGS/o41078a1.tif";
        }
        String[] formatNames = ImageIO.getReaderFormatNames();
        System.out.println("Image formats that can be read: ");
        for (int x = 0; x < formatNames.length; x++) {
            System.out.println("\t" + formatNames[x]);
        }
        GeoTIFFFile gtf = new GeoTIFFFile(filename);
        BufferedImage gtfbi = gtf.getBufferedImage();
        System.out.println("GeoTIFF image: " + gtfbi.getWidth() + " x " + gtfbi.getHeight());
        XTIFFField[] keys = gtf.getGeoKeys();
        gtf.dumpTags(keys);
        for (int x = 0; x < keys.length; x++) {
            String[] ks = keys[x].getAsStrings();
            for (int y = 0; y < ks.length; y++) {
                System.out.println(ks[y]);
            }
        }
        File file = new File(filename);
        ImageInputStream iis = ImageIO.createImageInputStream(file);
        ImageReader ir = null;
        Iterator<ImageReader> itList = ImageIO.getImageReaders(iis);
        while (itList.hasNext()) {
            ImageReader irTmp = itList.next();
            System.out.println(irTmp.getFormatName());
        }
        Iterator<ImageReader> it = ImageIO.getImageReadersByFormatName("tif");
        while (it.hasNext()) {
            ir = it.next();
            System.out.println(ir.getFormatName());
        }
        if (ir == null) {
            System.out.println("Problem getting image reader for tiff.");
            return;
        }
        ir.setInput(iis, true);
        TIFFDecodeParam param = null;
        ImageDecoder dec = ImageCodec.createImageDecoder("tiff", file, param);
        int pageCount = dec.getNumPages();
        System.out.println("Number of pages: " + pageCount);
        for (int x = 0; x < pageCount; x++) {
            BufferedImage bi = ir.read(x);
            System.out.println(x + ": " + bi.getWidth() + " x " + bi.getHeight());
            ImageIO.write(bi, "png", new File("/devel/j-" + x + ".png"));
        }
    }

    static void displayMetadata(Node root) {
        displayMetadata(root, 0);
    }

    static void indent(int level) {
        for (int i = 0; i < level; i++) System.out.print("    ");
    }

    static void displayMetadata(Node node, int level) {
        indent(level);
        System.out.print("<" + node.getNodeName());
        NamedNodeMap map = node.getAttributes();
        if (map != null) {
            int length = map.getLength();
            for (int i = 0; i < length; i++) {
                Node attr = map.item(i);
                System.out.print(" " + attr.getNodeName() + "=\"" + attr.getNodeValue() + "\"");
            }
        }
        Node child = node.getFirstChild();
        if (child == null) {
            System.out.println("/>");
            return;
        }
        System.out.println(">");
        while (child != null) {
            displayMetadata(child, level + 1);
            child = child.getNextSibling();
        }
        indent(level);
        System.out.println("</" + node.getNodeName() + ">");
    }
}
