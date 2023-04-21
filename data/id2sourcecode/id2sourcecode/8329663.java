    private static byte[] getStreamBytes(InputStream in) {
        if (in != null) {
            ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
            try {
                byte[] buff = new byte[2048];
                int read = 0;
                while ((read = in.read(buff)) != -1) {
                    bytesOut.write(buff, 0, read);
                }
            } catch (IOException ioe) {
                if (GlobalProps.DEBUG) {
                    System.out.println("getStreamBytes - IOexp");
                }
            }
            byte[] bytes = bytesOut.toByteArray();
            if (GlobalProps.DEBUG) {
                System.out.println("getStreamBytes - size: " + bytes.length);
            }
            return bytesOut.toByteArray();
        }
        return null;
    }
