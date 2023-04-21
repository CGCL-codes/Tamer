    public String getHash() {
        StringBuffer sb = new StringBuffer();
        sb.append(getStartDate()).append("-").append(getEndDate()).append("-").append(getChannelId()).append("-").append(getTitle());
        return sb.toString();
    }
