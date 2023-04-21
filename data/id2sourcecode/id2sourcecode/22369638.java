    private String readInputStream(InputStream is) {
        try {
            BufferedInputStream buis = new BufferedInputStream(is);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[512];
            int read = 0;
            while (true) {
                read = buis.read(buffer, 0, buffer.length);
                if (read == -1) {
                    break;
                } else {
                    baos.write(buffer, 0, read);
                }
            }
            baos.flush();
            String s = baos.toString();
            baos.close();
            return s;
        } catch (IOException ioe) {
            throw new RuntimeException("Error while reading from input stream. Error message : " + ioe.getMessage(), ioe);
        }
    }
