    @Override
    public ImageData getImageData() {
        InputStream in = null;
        try {
            if (key1 != null) {
                ImageDataSynthesizer imageDataSynthesizer = new ImageDataSynthesizer(url);
                in = imageDataSynthesizer.generateGIF(key1, key2);
                return new ImageData(in);
            } else {
                in = url.openStream();
            }
            return new ImageData(in);
        } catch (IOException e) {
            return null;
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    return null;
                }
            }
        }
    }
