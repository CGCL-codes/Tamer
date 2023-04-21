    public byte[] getBinaryResource(String uri) {
        InputStream is = null;
        try {
            URL url = new URL(uri);
            URLConnection conn = url.openConnection();
            is = conn.getInputStream();
            ByteArrayOutputStream result = new ByteArrayOutputStream();
            byte[] buf = new byte[10240];
            int i;
            while ((i = is.read(buf)) != -1) {
                result.write(buf, 0, i);
            }
            is.close();
            is = null;
            return result.toByteArray();
        } catch (IOException e) {
            return null;
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                }
            }
        }
    }
