    private String calcChechsum(File f) {
        StringBuffer csum = new StringBuffer();
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            FileInputStream fis = new FileInputStream(f);
            BufferedInputStream bis = new BufferedInputStream(fis);
            int blocksize = 4096;
            byte[] buf = new byte[blocksize];
            int readlen = -1;
            while ((readlen = bis.read(buf)) != -1) {
                md5.update(buf, 0, readlen);
            }
            char[] hex = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
            byte[] digest = md5.digest();
            for (int i = 0; i < digest.length; i++) {
                csum.append(hex[(digest[i] & 0xf0) >>> 4]);
                csum.append(hex[(digest[i] & 0x0f)]);
                if (i != digest.length - 1) csum.append("-");
            }
        } catch (Exception e) {
            ServerConsoleServlet.printSystemLog(e.toString() + " " + e.getMessage(), ServerConsoleServlet.LOG_ERROR);
            e.printStackTrace();
        }
        return csum.toString();
    }
