    public Long getFeatureImageCrc() {
        if (featureImageCrc != null) {
            return featureImageCrc;
        }
        URL url = getFeatureImageUrl();
        if (url == null) {
            return null;
        }
        InputStream in = null;
        try {
            CRC32 checksum = new CRC32();
            in = new CheckedInputStream(url.openStream(), checksum);
            byte[] sink = new byte[1024];
            while (true) {
                if (in.read(sink) <= 0) {
                    break;
                }
            }
            featureImageCrc = new Long(checksum.getValue());
            return featureImageCrc;
        } catch (IOException e) {
            return null;
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                }
            }
        }
    }
