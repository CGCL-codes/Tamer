    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("\nTitle: " + this.getTitle());
        sb.append("\nStartdate: " + this.getStartDate());
        sb.append("\nDuration: " + this.getDuration());
        sb.append("\nTotalDuration: " + this.getTotalDuration());
        sb.append("\nChannelname: " + this.getChannelName());
        sb.append("\nDescription: " + this.getDescription());
        sb.append("\nShort text: " + this.getShortText());
        if (streamInfos != null && streamInfos.size() > 0) {
            for (StreamInfo streamInfo : streamInfos) {
                sb.append("\nStreaminfo: " + streamInfo.toString());
            }
        }
        sb.append("\nPath: " + this.getPath());
        sb.append("\nParts: " + this.getParts());
        sb.append("\nTS: " + this.isTs());
        sb.append("\nID: " + this.getId());
        return sb.toString();
    }
