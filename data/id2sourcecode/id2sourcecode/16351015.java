    public static <T extends OutputStream> T transfer(InputStream i, T o, byte[] buffer, boolean closeInput) {
        try {
            int read = -1;
            if (buffer == null) buffer = new byte[defaultBufferSize];
            while ((read = i.read(buffer)) != -1) {
                o.write(buffer, 0, read);
                o.flush();
            }
            return o;
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        } finally {
            if (closeInput) try {
                i.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
