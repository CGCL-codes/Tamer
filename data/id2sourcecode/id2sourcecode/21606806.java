    public void write(StringBuffer buff, int p, Format f) {
        AudioFormat fmt = (AudioFormat) f;
        String encName = f.getEncoding().toLowerCase();
        buff.append("a=rtpmap:");
        buff.append(p);
        buff.append(" ");
        if (encName.equals("alaw")) {
            buff.append("pcma");
        } else if (encName.equals("ulaw")) {
            buff.append("pcmu");
        } else if (encName.equals("linear")) {
            buff.append("l" + fmt.getSampleSizeInBits());
        } else {
            buff.append(encName);
        }
        double sr = fmt.getSampleRate();
        if (sr > 0) {
            buff.append("/");
            if ((sr - (int) sr) < 1E-6) {
                buff.append((int) sr);
            } else {
                buff.append(sr);
            }
        }
        if (fmt.getChannels() > 1) {
            buff.append("/" + fmt.getChannels());
        }
        if (f.equals(AVProfile.DTMF)) {
            buff.append("\na=fmtp:" + p + " 0-15");
        }
    }
