    public String getMD5sum() {
        String result = "";
        try {
            InputStream is = new FileInputStream(fileInfo.getPath());
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] buf = new byte[1024];
            int count;
            count = is.read(buf);
            while (count != -1) {
                md.update(buf, 0, count);
                count = is.read(buf);
            }
            is.close();
            byte[] b = md.digest();
            for (int i = 0; i < b.length; i++) {
                result += Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
        return result;
    }
