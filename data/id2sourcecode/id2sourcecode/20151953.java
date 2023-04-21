    public static BufferedImage getImage(String source) {
        Object test = imageMap.get(source);
        if (test != null) {
            Logger.writeLog(Logger.LOG_DEBUG, "Hit in image cache.");
            return (BufferedImage) test;
        }
        java.net.URL u = imageMap.getClass().getResource("/graphics/" + source);
        Image i = new javax.swing.ImageIcon(u).getImage();
        int w = i.getWidth(observer);
        int h = i.getHeight(observer);
        if (w <= 0 || h <= 0) {
            Logger.writeLog("Couldn't read image...");
        }
        BufferedImage b = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        b.getGraphics().drawImage(i, 0, 0, observer);
        imageMap.put(source, b);
        return b;
    }
