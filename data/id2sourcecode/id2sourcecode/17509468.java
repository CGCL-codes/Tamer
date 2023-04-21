    private BlockGen getImageDataFromFile(File fileIn) throws IOException, TipoImmagineSconosciuto {
        RAWImageInputStream iis = new RAWImageInputStream(ImageIO.createImageInputStream(fileIn));
        iis.selectBitReader(-1, 262144);
        if (iis.length() != (h * w * colorSpace.getChannelsNumber())) throw new IOException(ERR_DIM);
        int[][] raster = colorSpace.convertFromRgb(iis, w, h);
        iis.close();
        return new BlockGen(raster, N, colorSpace);
    }
