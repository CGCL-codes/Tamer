    public Word(String text, int position, MessageDigest msgdiggest) {
        this.text = text;
        this.chtext = text.toCharArray();
        this.position = position;
        byte[] b = (text + position).getBytes();
        md5 = U.toHex(msgdiggest.digest(b));
    }
