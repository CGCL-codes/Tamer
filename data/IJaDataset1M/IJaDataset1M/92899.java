package org.gerhardb.jibs.help;

import java.io.InputStream;
import java.io.StringWriter;
import java.net.URL;
import com.saic.isd.util.report.Report;

/**
 * Display HTML information.
 */
public class HtmlViewer extends Report {

    /**
    * Display HTML information.
    * @param title to show on the title bar of the window.
    */
    public HtmlViewer(String title) {
        super(title, "text/html");
    }

    public void displayHTML(String html) {
        this.myHTML = html;
        this.myEditorPane.setText(this.myHTML);
        this.setVisible(true);
        this.myEditorPane.setCaretPosition(0);
    }

    public boolean displayResource(String path) {
        try {
            InputStream in = this.getClass().getResourceAsStream(path);
            if (in == null) {
                System.out.println("Null input stream");
                return false;
            }
            StringWriter writer = new StringWriter(3000);
            int aChar = in.read();
            while (aChar > -1) {
                writer.write(aChar);
                aChar = in.read();
            }
            StringBuffer buff = writer.getBuffer();
            String html = remapImageTags(buff);
            displayHTML(html);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    private String remapImageTags(StringBuffer buff) {
        String imgPath = getPathToImages();
        if (imgPath == null) {
            return buff.toString();
        }
        String tag = "src=\"";
        int tagLength = tag.length();
        int index = buff.indexOf(tag, 0);
        while (index > -1) {
            int insertAt = index + tagLength;
            buff.insert(insertAt, imgPath);
            index = buff.indexOf(tag, insertAt);
        }
        return buff.toString();
    }

    /**
    * Only works for images in this directory.
    * Original idea came from:
    * http://www.javaworld.com/javatips/jw-javatip120_p.html
    * However, that article used getClass().getClassLoader().getSystemResource()
    * which always returns a null from a local jar file.  So does
    * getClass().getClassLoader().getResource().  Switching to
    * getClass().getResource() solved all problems.
    * @return path to images
    */
    private String getPathToImages() {
        String path = null;
        try {
            URL urlJar = this.getClass().getResource("tutorial.html");
            String urlStr = urlJar.toString();
            int to = urlStr.indexOf("tutorial.html");
            path = urlStr.substring(0, to);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return path;
    }

    /**
    * Expriments leading to the development of getPathToImages()
    * @return jar file name
    */
    private String getJarFileName() {
        String className = this.getClass().getName() + ".class";
        URL urlJar = null;
        urlJar = ClassLoader.getSystemResource(this.getClass().getName());
        System.out.println("urlJar a: " + urlJar);
        urlJar = this.getClass().getClassLoader().getResource(this.getClass().getName());
        System.out.println("urlJar b: " + urlJar);
        urlJar = ClassLoader.getSystemResource(className);
        System.out.println("urlJar c: " + urlJar);
        urlJar = this.getClass().getClassLoader().getResource(className);
        System.out.println("urlJar d: " + urlJar);
        urlJar = this.getClass().getClassLoader().getResource("banner.gif");
        System.out.println("urlJar e: " + urlJar);
        urlJar = this.getClass().getClassLoader().getResource("/org/gerhardb/jibs/help/banner.gif");
        System.out.println("urlJar f: " + urlJar);
        urlJar = this.getClass().getResource("tutorial.html");
        System.out.println("urlJar e: " + urlJar);
        urlJar = this.getClass().getResource("/org/gerhardb/jibs/help/tutorial.html");
        System.out.println("urlJar f: " + urlJar);
        return "ending early";
    }

    public static void main(String[] args) {
        HtmlViewer htmlViewer = new HtmlViewer("JIBS Help");
        System.out.println(htmlViewer.getJarFileName());
        htmlViewer.displayResource("tutorial-viewer.html");
    }
}
