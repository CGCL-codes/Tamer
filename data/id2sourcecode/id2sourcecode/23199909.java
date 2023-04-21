    public void write(StringBuffer buff, int p, Format f) {
        AudioFormat fmt = (AudioFormat) f;
        String encName = f.getEncoding();
        buff.append("a=rtpmap:");
        buff.append(p);
        buff.append(" ");
        if (encName.equalsIgnoreCase("alaw")) {
            buff.append("PCMA");
        } else if (encName.equalsIgnoreCase("ulaw")) {
            buff.append("PCMU");
        } else if (encName.equalsIgnoreCase("linear")) {
            buff.append("L" + fmt.getSampleSizeInBits());
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
