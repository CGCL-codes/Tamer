    public static void writeToZip(FileInputStream in, ZipOutputStream out) {
        byte[] buffer = new byte[18024];
        int len;
        try {
            while ((len = in.read(buffer)) > 0) out.write(buffer, 0, len);
            out.closeEntry();
            in.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
