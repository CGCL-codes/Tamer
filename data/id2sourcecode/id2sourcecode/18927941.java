    public String getUrl() {
        if (!StringUtils.isBlank(getOuterUrl())) {
            return getOuterUrl();
        }
        StringBuilder sb = getWebsite().getWebUrlBuf();
        String path = getChannel().getPath();
        if (!StringUtils.isBlank(path)) {
            sb.append(SPT).append(path);
        }
        sb.append(SPT).append(getId()).append(".").append(getWebsite().getSuffix());
        return sb.toString();
    }
