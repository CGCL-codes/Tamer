    public static byte[] createBufferedImageData(URL url) {
        try {
            filename = url.toString();
            InputStream istream = url.openStream();
            byte[] imgData = null;
            byte[] buf = new byte[1024];
            int length;
            while ((length = istream.read(buf, 0, buf.length)) == buf.length) {
                if (imgData != null) {
                    byte[] imgDataTmp = new byte[imgData.length + length];
                    System.arraycopy(imgData, 0, imgDataTmp, 0, imgData.length);
                    System.arraycopy(buf, 0, imgDataTmp, imgData.length, length);
                    imgData = imgDataTmp;
                } else {
                    imgData = new byte[length];
                    System.arraycopy(buf, 0, imgData, 0, length);
                }
            }
            if (imgData != null) {
                byte[] imgDataTmp = new byte[imgData.length + length];
                System.arraycopy(imgData, 0, imgDataTmp, 0, imgData.length);
                System.arraycopy(buf, 0, imgDataTmp, imgData.length, length);
                imgData = imgDataTmp;
            } else {
                imgData = new byte[length];
                System.arraycopy(buf, 0, imgData, 0, length);
            }
            istream.close();
            return imgData;
        } catch (IOException ex) {
            return null;
        }
    }
