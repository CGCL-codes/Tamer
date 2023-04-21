    public Image getImage(URL url) {
        Image img = null;
        try {
            URLConnection connect = url.openConnection();
            Object oo = connect.getContent();
            if (oo instanceof ImageProducer) {
                System.out.println("image producer " + oo);
                img = createImage((ImageProducer) oo);
            } else if (oo instanceof Image) {
                img = (Image) oo;
            }
        } catch (java.net.MalformedURLException ex) {
            img = null;
            System.err.println("Image loading: " + ex);
        } catch (java.io.IOException ex2) {
            img = null;
            System.err.println("Image loading: " + ex2);
        } finally {
            return img;
        }
    }
