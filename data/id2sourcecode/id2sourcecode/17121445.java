    public String getCustomToolTipText() {
        Channel channel = (Channel) this.getUserObject();
        StringBuffer buf = new StringBuffer();
        buf.append("<html>");
        buf.append("Channel #: " + channel.getChannelNum());
        buf.append("<br>Channel description: " + channel.getDescription());
        buf.append("</html>");
        return buf.toString();
    }
