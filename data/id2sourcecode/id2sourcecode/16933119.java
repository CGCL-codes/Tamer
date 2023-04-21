    public static Image getInstance(URL url) throws BadElementException, MalformedURLException, IOException {
        InputStream is = null;
        try {
            is = url.openStream();
            int c1 = is.read();
            int c2 = is.read();
            int c3 = is.read();
            int c4 = is.read();
            is.close();
            is = null;
            if (c1 == 'G' && c2 == 'I' && c3 == 'F') {
                GifImage gif = new GifImage(url);
                Image img = gif.getImage(1);
                return img;
            }
            if (c1 == 0xFF && c2 == 0xD8) {
                return new Jpeg(url);
            }
            if (c1 == PngImage.PNGID[0] && c2 == PngImage.PNGID[1] && c3 == PngImage.PNGID[2] && c4 == PngImage.PNGID[3]) {
                return PngImage.getImage(url);
            }
            if (c1 == '%' && c2 == '!' && c3 == 'P' && c4 == 'S') {
                return new ImgPostscript(url);
            }
            if (c1 == 0xD7 && c2 == 0xCD) {
                return new ImgWMF(url);
            }
            if (c1 == 'B' && c2 == 'M') {
                return BmpImage.getImage(url);
            }
            if ((c1 == 'M' && c2 == 'M' && c3 == 0 && c4 == 42) || (c1 == 'I' && c2 == 'I' && c3 == 42 && c4 == 0)) {
                RandomAccessFileOrArray ra = null;
                try {
                    if (url.getProtocol().equals("file")) {
                        String file = url.getFile();
                        ra = new RandomAccessFileOrArray(file);
                    } else ra = new RandomAccessFileOrArray(url);
                    Image img = TiffImage.getTiffImage(ra, 1);
                    img.url = url;
                    return img;
                } finally {
                    if (ra != null) ra.close();
                }
            }
            throw new IOException(url.toString() + " is not a recognized imageformat.");
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }
