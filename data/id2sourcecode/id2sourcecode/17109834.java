    public String toSdp() {
        String encName = this.getEncoding().toLowerCase();
        StringBuffer buff = new StringBuffer();
        buff.append(payloadType);
        buff.append(" ");
        if (encName.equals("alaw")) {
            buff.append("pcma");
        } else if (encName.equals("ulaw")) {
            buff.append("pcmu");
        } else if (encName.equals("linear")) {
            buff.append("l" + this.sampleSizeInBits);
        } else {
            buff.append(encName);
        }
        double sr = getSampleRate();
        if (sr > 0) {
            buff.append("/");
            if ((sr - (int) sr) < 1E-6) {
                buff.append((int) sr);
            } else {
                buff.append(sr);
            }
        }
        if (getChannels() > 1) {
            buff.append("/" + getChannels());
        }
        return buff.toString();
    }
