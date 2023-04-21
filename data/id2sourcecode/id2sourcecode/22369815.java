    public static synchronized String calcHash(File file) {
        try {
            MessageDigest md = null;
            FileInputStream fileInputStream = new FileInputStream(file);
            byte[] data = new byte[(int) file.length()];
            fileInputStream.read(data);
            fileInputStream.close();
            try {
                md = MessageDigest.getInstance("MD5");
                md.update(data);
            } catch (Exception ex) {
                ex.printStackTrace();
                System.exit(1);
            }
            return convertToHex(md.digest()) + data.length;
        } catch (Exception e) {
            return "";
        }
    }
