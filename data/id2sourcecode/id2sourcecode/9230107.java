    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("SoundInfo : " + getResource() + "\n");
        sb.append("\tchannels : " + getChannels() + "\n");
        sb.append("\tbit_length : " + getBitLength() + "\n");
        sb.append("\tframe_rate : " + getFrameRate() + "\n");
        sb.append(getComment());
        return sb.toString();
    }
