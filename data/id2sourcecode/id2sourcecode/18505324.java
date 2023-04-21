    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + getChannelGroup().hashCode();
        result = prime * result + basecalls.hashCode();
        result = prime * result + peaks.hashCode();
        result = prime * result + properties.hashCode();
        return result;
    }
