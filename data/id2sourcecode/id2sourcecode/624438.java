    TiledImageExample(String inputFile, String outputFile) throws IOException {
        ImageInputStream input = ImageIO.createImageInputStream(new File(inputFile));
        ImageReader reader = (ImageReader) ImageIO.getImageReaders(input).next();
        reader.setInput(input);
        RenderedImage image = reader.readAsRenderedImage(0, null);
        int tileWidth = TILE_SIZE;
        int tileHeight = TILE_SIZE;
        boolean isImageTiled = image.getNumXTiles() > 1 || image.getNumYTiles() > 1;
        if (isImageTiled) {
            tileWidth = image.getTileWidth();
            tileHeight = image.getTileHeight();
        }
        writer = (ImageWriter) ImageIO.getImageWritersByFormatName("TIFF").next();
        ImageOutputStream output = ImageIO.createImageOutputStream(new File(outputFile));
        writer.setOutput(output);
        ImageWriteParam param = writer.getDefaultWriteParam();
        param.setTilingMode(ImageWriteParam.MODE_EXPLICIT);
        param.setTiling(tileWidth, tileHeight, 0, 0);
        writer.write(null, new IIOImage(image, null, reader.getImageMetadata(0)), param);
        input = ImageIO.createImageInputStream(new File(outputFile));
        reader = (ImageReader) ImageIO.getImageReaders(input).next();
        reader.setInput(input);
        image = reader.readAsRenderedImage(0, null);
        SelfCleaningTiledImage ti = new SelfCleaningTiledImage(image, true, MAX_TILES);
        ti.addTileObserver(this);
        Graphics2D g2d = ti.createGraphics();
        int numTiles = 0;
        int numBands = image.getSampleModel().getNumBands();
        int y = ti.getMinY();
        for (int ty = 0; ty < ti.getNumYTiles(); ty++, y += tileHeight) {
            int x = ti.getMinX();
            for (int tx = 0; tx < ti.getNumXTiles(); tx++, x += tileWidth) {
                int r;
                int g;
                int b;
                if (numBands == 1) {
                    r = g = b = ti.getTile(tx, ty).getSample(x + tileWidth / 2, y + tileHeight / 2, 0);
                } else {
                    Raster tile = ti.getTile(tx, ty);
                    r = tile.getSample(x + tileWidth / 2, y + tileHeight / 2, 0);
                    g = tile.getSample(x + tileWidth / 2, y + tileHeight / 2, 1);
                    b = tile.getSample(x + tileWidth / 2, y + tileHeight / 2, 2);
                }
                g2d.setColor(new Color(r, g, b));
                g2d.fill(ti.getTileRect(tx, ty));
            }
        }
        Rectangle rect = ti.getTileRect(ti.getNumXTiles() / 2, ti.getNumYTiles() / 2);
        rect.x += rect.width / 4;
        rect.y += rect.height / 4;
        rect.width -= rect.width / 2;
        rect.height -= rect.height / 2;
        g2d.setColor(Color.red);
        g2d.fill(rect);
    }
