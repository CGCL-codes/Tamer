    public String convertMessage(String message) throws UnsupportedEncodingException {
        digest.update(message.getBytes(charEncoding), 0, message.length());
        byte[] byteMessage = digest.digest();
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < byteMessage.length; i++) {
            int halfbyte = (byteMessage[i] >>> 4) & 0x0F;
            int two_halfs = 0;
            do {
                if ((0 <= halfbyte) && (halfbyte <= 9)) buf.append((char) ('0' + halfbyte)); else buf.append((char) ('a' + (halfbyte - 10)));
                halfbyte = byteMessage[i] & 0x0F;
            } while (two_halfs++ < 1);
        }
        return buf.toString();
    }
