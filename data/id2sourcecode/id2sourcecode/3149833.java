    public boolean equals(Object obj) {
        if (obj instanceof SiteChannels == false) return false;
        if (this == obj) return true;
        SiteChannels other = (SiteChannels) obj;
        return new EqualsBuilder().append(getId(), other.getId()).append(getChannelsName(), other.getChannelsName()).append(getShowType(), other.getShowType()).append(getSortValue(), other.getSortValue()).append(getEffective(), other.getEffective()).append(getChannelsNotes(), other.getChannelsNotes()).append(getInsertTime(), other.getInsertTime()).append(getSiteId(), other.getSiteId()).append(getLogoUrl(), other.getLogoUrl()).append(getPartIds(), other.getPartIds()).isEquals();
    }
