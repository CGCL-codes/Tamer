    public void getPennerImage(String sURL) throws IOException {
        URL targetURL = new URL(sURL);
        URLConnection url = (URLConnection) targetURL.openConnection();
        setRequestProperties(url);
        InputStream is = url.getInputStream();
        ImageInputStream inStream = ImageIO.createImageInputStream(is);
        Iterator<ImageReader> readers = ImageIO.getImageReaders(inStream);
        ImageReader read = readers.next();
        while (readers.hasNext()) {
            read = readers.next();
        }
        read.setInput(inStream, true, true);
        Image bimage = read.read(0);
        File outfile = new File("gfx/Penner.jpg");
        ImageIO.write((RenderedImage) bimage, "jpg", outfile);
        is.close();
        inStream.close();
    }
