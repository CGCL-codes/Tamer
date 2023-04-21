    public URLImage(Graphics g, URL url) {
        super(g);
        _url = url;
        BufferedImage bufferedImage;
        try {
            bufferedImage = ImageIO.read(_url.openStream());
            setWidth(bufferedImage.getWidth());
            setHeight(bufferedImage.getHeight());
        } catch (IOException e) {
            _logger.throwing(getClass().getName(), "constructor", e);
        }
    }
