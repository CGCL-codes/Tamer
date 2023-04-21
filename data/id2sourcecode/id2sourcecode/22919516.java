    public static ImageIcon createImage(String url) {
        ImageIcon result = null;
        try {
            URL address = new URL(url);
            URLConnection urlCon = address.openConnection();
            InputStream iStream = urlCon.getInputStream();
            int contentLength = urlCon.getContentLength();
            byte[] content = new byte[contentLength];
            int i = 0;
            while (i < contentLength) {
                int bytesReaded = iStream.read(content, i, contentLength - i);
                if (bytesReaded > 0) {
                    i += bytesReaded;
                } else {
                    return null;
                }
            }
            iStream.close();
            result = new ImageIcon(content);
        } catch (Exception e) {
            System.out.println("WARNING: NamtiaUtilities.createImage() -> " + e.getMessage());
        }
        return result;
    }
