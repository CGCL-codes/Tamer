    public static byte[] readToByteArray(File file) throws IOException {
        assert file.isFile() && file.canRead();
        long length = file.length();
        if (length > Integer.MAX_VALUE) {
            throw new IOException("The file is too big");
        }
        byte[] buffer = new byte[(int) length];
        int offset = 0;
        int numRead = 0;
        BufferedInputStream bis = null;
        try {
            FileInputStream is = new FileInputStream(file);
            bis = new BufferedInputStream(is);
            while (offset < buffer.length && (numRead = bis.read(buffer, offset, buffer.length - offset)) >= 0) {
                offset += numRead;
            }
            if (offset < buffer.length) {
                throw new IOException("The file was not completely read: " + file.getName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                }
            }
        }
        return buffer;
    }
