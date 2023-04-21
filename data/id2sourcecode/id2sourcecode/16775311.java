    public static String checkForUpdate() {
        InputStream is = null;
        ByteArrayOutputStream bos = null;
        try {
            URL url = new URL(URL_LATEST_VERSION);
            URLConnection ucnn = url.openConnection();
            bos = new ByteArrayOutputStream();
            is = ucnn.getInputStream();
            byte[] data = new byte[256];
            int offset;
            while ((offset = is.read(data)) != -1) {
                bos.write(data, 0, offset);
            }
            String version = bos.toString();
            if (version == null || version.equals("null")) version = null;
            return version;
        } catch (Exception ex) {
        } finally {
            try {
                is.close();
            } catch (Exception e) {
            }
            try {
                bos.close();
            } catch (Exception e) {
            }
        }
        return null;
    }
