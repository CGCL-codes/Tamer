    public Image createImage(URL url) {
        try {
            String resId = "image:" + url.toString();
            Resource res = getResourceCache().getResourceByKey(resId);
            if (res != null) {
                return (Image) res.getResourceObject();
            } else {
                BufferedImage buffImg = ImageIO.read(url.openStream());
                if (buffImg != null) {
                    registerResource(new BufferedImageResource(resId, "image", buffImg));
                }
                return buffImg;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return new EmptyImage(getGraphics());
        }
    }
