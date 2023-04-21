    public static String md5sum(InputStream is, int bufferSize, String alg, DspmsgHandler msgHandler) {
        try {
            BufferedInputStream bis = new BufferedInputStream(is);
            MessageDigest md = MessageDigest.getInstance(alg);
            byte[] inb = new byte[bufferSize];
            int br = bis.read(inb);
            while (br > -1) {
                md.update(inb, 0, br);
                if (msgHandler != null) {
                    msgHandler.dspmsg(EMPTY_STRING + br);
                }
                br = bis.read(inb);
            }
            StringBuffer sb = new StringBuffer();
            synchronized (sb) {
                for (byte b : md.digest()) sb.append(pad(Integer.toHexString(0xFF & b), ZERO.charAt(0), 2, true));
            }
            bis.close();
            return sb.toString();
        } catch (Exception ex) {
            log(ex);
        }
        return null;
    }
