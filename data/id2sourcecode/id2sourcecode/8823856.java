    public String getLog() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.getChannelString());
        if (sb.length() > 0) sb.append(" ");
        sb.append(this.content);
        return sb.toString();
    }
