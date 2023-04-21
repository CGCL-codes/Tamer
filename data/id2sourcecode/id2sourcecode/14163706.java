    public boolean equals(Object obj) {
        if (obj instanceof SiteChannelsPartReal == false) return false;
        if (this == obj) return true;
        SiteChannelsPartReal other = (SiteChannelsPartReal) obj;
        return new EqualsBuilder().append(getId(), other.getId()).append(getChannelsId(), other.getChannelsId()).append(getPartId(), other.getPartId()).isEquals();
    }
