    protected String createXInfo() {
        assert event != null : "event may not be null!";
        StringBuilder sb = new StringBuilder("<vdrassistant>");
        if (event.getChannel() != null) sb.append("<channelid>").append(event.getChannel().getShortId()).append("</channelid>");
        if (event.getBegin() != null) sb.append("<start>").append(event.getBegin().getTime()).append("</start>");
        sb.append("<eventid>").append(event.getEpgId()).append("</eventid>");
        sb.append("<title>").append(event.getTitle()).append("</title>");
        sb.append("</vdrassistant>");
        return sb.toString();
    }
