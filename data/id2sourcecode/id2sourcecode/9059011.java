    public static byte[] downloadBytes(URL url) {
        InputStream inputStream = null;
        BufferedInputStream in = null;
        ByteArrayOutputStream out = null;
        try {
            inputStream = downloadStream(url);
            if (inputStream == null) return null;
            in = new BufferedInputStream(inputStream);
            out = new ByteArrayOutputStream();
            int i;
            while ((i = in.read()) > -1) out.write(i);
            out.close();
            in.close();
            return out.toByteArray();
        } catch (Exception ex) {
            try {
                in.close();
            } catch (Exception ex1) {
            }
            try {
                inputStream.close();
            } catch (Exception ex1) {
            }
            try {
                out.close();
            } catch (Exception ex1) {
            }
            return null;
        }
    }
