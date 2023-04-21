    public String toDescription() {
        StringBuffer sb = new StringBuffer(500);
        sb.append("WHOIS Information for ").append(getNick()).append('\n');
        sb.append("As of ").append(getUpdateTime()).append('\n');
        sb.append("").append('\n');
        sb.append("Nick: ").append(getNick()).append('\n');
        sb.append("Address: ").append(getUserName()).append('@').append(getHostName()).append('\n');
        sb.append("Server: ").append(getServerName()).append(" (").append(getServerDesc()).append(')').append('\n');
        sb.append('\n');
        sb.append("Channels: ").append(getChannels()).append('\n');
        sb.append('\n');
        if (_isOnline) {
            sb.append("Seconds Idle: ").append(getIdleTime()).append('\n');
            sb.append("On since: ").append(getSignonTime()).append('\n');
        } else {
            sb.append("User is currently OFFLINE").append('\n');
        }
        return sb.toString();
    }
