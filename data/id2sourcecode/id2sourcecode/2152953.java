    public static byte[] toByteArray(InputStream in) throws IOException {
        try {
            int bufferSize = in.available();
            if (bufferSize <= 0) {
                bufferSize = 8 * 1024;
            }
            byte[] buffer = new byte[bufferSize];
            ByteArrayOutputStream out = new ByteArrayOutputStream(bufferSize);
            int read;
            while ((read = in.read(buffer, 0, bufferSize)) != -1) {
                out.write(buffer, 0, read);
            }
            return out.toByteArray();
        } catch (IOException e) {
            throw e;
        } catch (Exception e) {
            throw new IOException(e.toString());
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (Exception e) {
                }
            }
        }
    }
