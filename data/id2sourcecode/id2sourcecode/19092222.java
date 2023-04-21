    private byte[] getFileBytes(File file) {
        if (file.exists()) {
            ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
            try {
                FileInputStream fileIn = new FileInputStream(file);
                byte[] buff = new byte[2048];
                int read = 0;
                while ((read = fileIn.read(buff)) != -1) {
                    bytesOut.write(buff, 0, read);
                }
            } catch (FileNotFoundException fnfe) {
                if (GlobalProps.DEBUG) {
                    System.out.println("getFileBy - FNFexp");
                }
            } catch (IOException ioe) {
                if (GlobalProps.DEBUG) {
                    System.out.println("getFileBy - IOexp");
                }
            }
            byte[] bytes = bytesOut.toByteArray();
            if (GlobalProps.DEBUG) {
                System.out.println("getFileBy - size: " + bytes.length);
            }
            return bytesOut.toByteArray();
        }
        return null;
    }
