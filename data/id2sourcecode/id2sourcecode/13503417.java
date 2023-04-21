    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString());
        sb.append(" [Id:" + getId() + "]");
        sb.append(" [Nick:" + getNickname() + "]");
        sb.append(" [ServerPrivileges:" + getServerPrivileges().toString() + "]");
        sb.append(" [ChannelPrivileges:" + getChannelPrivileges().toString() + "]");
        sb.append(" [Status:" + getStatus().toString() + "]");
        return sb.toString();
    }
