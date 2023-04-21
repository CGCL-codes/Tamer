    public GraphicInfo(InputStream imageInputStream, int width, boolean widthPixel, int height, boolean heightPixel, short verticalAlignment, short horizontalAlignment, TextContentAnchorType anchor) throws NOAException {
        if (imageInputStream == null) throw new IllegalArgumentException();
        try {
            tmpFile = File.createTempFile("NOATempImage", ".noa.tmp");
            tmpFile.deleteOnExit();
            FileOutputStream streamOut = new FileOutputStream(tmpFile);
            int c;
            while ((c = imageInputStream.read()) != -1) streamOut.write(c);
            imageInputStream.close();
            streamOut.close();
        } catch (Throwable throwable) {
            throw new NOAException(throwable);
        }
        String url = tmpFile.getAbsolutePath();
        init(url, width, widthPixel, height, heightPixel, verticalAlignment, horizontalAlignment, anchor);
    }
