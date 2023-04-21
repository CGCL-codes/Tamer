    public Png(URL url) throws BadElementException, IOException {
        super(url);
        type = PNG;
        InputStream is = null;
        try {
            is = url.openStream();
            for (int i = 0; i < PNGID.length; i++) {
                if (PNGID[i] != is.read()) {
                    throw new BadElementException(url.toString() + " is not a valid PNG-file.");
                }
            }
            while (true) {
                int len = getInt(is);
                if (IHDR.equals(getString(is))) {
                    scaledWidth = getInt(is);
                    setRight((int) scaledWidth);
                    scaledHeight = getInt(is);
                    setTop((int) scaledHeight);
                    break;
                }
                if (IEND.equals(getString(is))) {
                    break;
                }
                skip(is, len + 4);
            }
        } finally {
            if (is != null) {
                is.close();
            }
            plainWidth = width();
            plainHeight = height();
        }
    }
