    String calculate(String Filename) {
        String MD5SUM = new String();
        ;
        try {
            MessageDigest md = MessageDigest.getInstance("md5");
            FileInputStream in = new FileInputStream(Filename);
            int len;
            byte[] data = new byte[1024];
            while ((len = in.read(data)) > 0) {
                md.update(data, 0, len);
            }
            in.close();
            byte[] result = md.digest();
            for (int i = 0; i < result.length; ++i) {
                MD5SUM = MD5SUM + toHexString(result[i]);
            }
        } catch (Exception e) {
            log.error("[MD5Calculator] MD5-Fehler: " + e.toString());
        }
        return MD5SUM;
    }
