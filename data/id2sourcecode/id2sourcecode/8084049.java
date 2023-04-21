    public String getChannelStatusString(Integer index) {
        int status = getChannelStatus(index);
        String str = vch_status[status];
        return str;
    }
