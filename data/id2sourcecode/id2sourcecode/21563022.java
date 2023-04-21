    public String getChannelDescription() {
        int chnls = hdr.getNumChannels();
        if (chnls == 1) return "Mono"; else if (chnls == 2) return "Stereo"; else return chnls + " channel";
    }
