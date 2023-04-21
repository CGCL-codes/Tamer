    public byte[] getData() {
        byte[] data = super.getBinaryData();
        if (data != null) return data;
        String str = getImageURL();
        if (str == null || str.length() == 0) {
            log.config("No Image URL");
            return null;
        }
        URL url = getURL();
        if (url == null) {
            log.config("No URL");
            return null;
        }
        try {
            URLConnection conn = url.openConnection();
            conn.setUseCaches(false);
            InputStream is = conn.getInputStream();
            byte[] buffer = new byte[1024 * 8];
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            int length = -1;
            while ((length = is.read(buffer)) != -1) os.write(buffer, 0, length);
            is.close();
            data = os.toByteArray();
            os.close();
        } catch (Exception e) {
            log.config(e.toString());
        }
        return data;
    }
