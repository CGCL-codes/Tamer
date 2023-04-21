    public static byte[] readBytesFormZipFile(String fileName, String fname) {
        byte[] bytes = null;
        ByteArrayOutputStream os = null;
        ZipInputStream in = null;
        try {
            os = new ByteArrayOutputStream(4096);
            in = new ZipInputStream(new FileInputStream(fileName));
            boolean found = false;
            while (!found) {
                ZipEntry entry = in.getNextEntry();
                if (fname.equalsIgnoreCase(entry.getName())) {
                    found = true;
                }
            }
            int read;
            byte[] buffer = new byte[4096];
            while ((read = in.read(buffer)) >= 0) {
                os.write(buffer, 0, read);
            }
            bytes = os.toByteArray();
        } catch (Exception e) {
            bytes = null;
        } finally {
            try {
                if (os != null) {
                    os.flush();
                    os = null;
                }
                if (in != null) {
                    in.close();
                    in = null;
                }
            } catch (Exception e) {
            }
        }
        return bytes;
    }
