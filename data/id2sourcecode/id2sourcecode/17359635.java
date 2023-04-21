    @Override
    public void run() throws Exception {
        final BufferedImageReader reader = new BufferedImageReader();
        final ServiceFactory factory = new ServiceFactory();
        final OMEXMLService service = factory.getInstance(OMEXMLService.class);
        final IMetadata omexml = service.createOMEXMLMetadata();
        reader.setMetadataStore(omexml);
        reader.setId(sourceFile);
        int series = -1;
        long currentPixelSize = 0;
        for (int i = 0; i < reader.getSeriesCount(); i++) {
            reader.setSeries(i);
            final long imagePixelSize = reader.getSizeX() * reader.getSizeY() * reader.getSizeC();
            if (maximumImageSize == 0 || (imagePixelSize < maximumImageSize && imagePixelSize > minimumImageSize)) {
                if (imagePixelSize > currentPixelSize) {
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("Image pixel size " + imagePixelSize + " in series " + i + " fits within bounds and is the largest series so far.");
                    }
                    series = i;
                    currentPixelSize = imagePixelSize;
                } else if (LOG.isDebugEnabled()) {
                    LOG.debug("Image pixel size " + imagePixelSize + " in series " + i + " fits within bounds but is not the largest series.");
                }
            } else if (LOG.isDebugEnabled()) {
                LOG.debug("Image pixel size " + imagePixelSize + " in series " + i + " doesn't fit within bounds.");
            }
        }
        if (series < 0) {
            throw new Exception("Cound not find an image series that fits within bounds.");
        }
        reader.setSeries(series);
        final LociImageSource source = new LociImageSource(reader, omexml, 0);
        final LociImageDestination destination = new LociImageDestination(destinationFile, (int) source.getWidth(), (int) source.getHeight(), omexml);
        long remainingWidth = source.getWidth();
        long remainingHeight = source.getHeight();
        long readWidth, readHeight;
        long x = 0, y = 0;
        final long bufferWidth = source.getWidth();
        final long bufferHeight = source.getHeight();
        boolean finished = false;
        while (!finished) {
            if (remainingWidth > bufferWidth) {
                readWidth = bufferWidth;
            } else {
                readWidth = remainingWidth;
            }
            if (remainingHeight > bufferHeight) {
                readHeight = bufferHeight;
            } else {
                readHeight = remainingHeight;
            }
            final byte[] bytes = source.readBytes(x, y, readWidth, readHeight);
            destination.writeBytes(bytes, x, y, readWidth, readHeight);
            x += readWidth;
            remainingWidth -= readWidth;
            if (remainingWidth == 0) {
                y += readHeight;
                remainingHeight -= readHeight;
                if (remainingHeight == 0) {
                    finished = true;
                } else {
                    x = 0;
                    remainingWidth = source.getWidth();
                }
            }
        }
        destination.close();
        source.close();
    }
