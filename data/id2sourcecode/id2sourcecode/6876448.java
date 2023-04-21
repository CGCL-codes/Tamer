    public BufferedImage getImg(String imageUrl) {
        ImageIO.setUseCache(false);
        if (imageUrl == null) return null;
        BufferedImage img = null;
        try {
            if (!imageUrl.matches("http.*")) imageUrl = "file:" + imageUrl;
            URL url = new URL(imageUrl);
            img = ImageIO.read(new MemoryCacheImageInputStream(url.openStream()));
        } catch (Exception e) {
            if (Prefs.current.verbose) System.out.println("ImageLoader: couldnt read " + imageUrl + ", for reason:" + e);
        }
        return img;
    }
