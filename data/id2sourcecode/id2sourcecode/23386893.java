    public String getDigest(MessageDigest md, String message) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DigestOutputStream dos = new DigestOutputStream(baos, md);
        PrintWriter pw = new PrintWriter(dos);
        pw.print(message);
        pw.flush();
        byte digest[] = dos.getMessageDigest().digest();
        StringBuffer sb = new StringBuffer();
        for (int offset = 0; offset < digest.length; offset++) {
            byte b = digest[offset];
            if ((b & 0xF0) == 0) {
                sb.append("0");
            }
            sb.append(Integer.toHexString(b & 0xFF));
        }
        return sb.toString();
    }
