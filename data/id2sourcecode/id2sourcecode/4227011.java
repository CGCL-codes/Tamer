    public static byte[] toByteArray(InputStream in) throws IOException {
        ByteArrayOutputStream out = null;
        try {
            out = new ByteArrayOutputStream();
            int i;
            while ((i = in.read()) > -1) out.write(i);
            return out.toByteArray();
        } finally {
            try {
                in.close();
            } catch (Exception ex) {
            }
            try {
                out.close();
            } catch (Exception ex) {
            }
        }
    }
