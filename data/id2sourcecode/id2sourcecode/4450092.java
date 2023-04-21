    public String getUrlDynamic(Boolean whole) {
        if (!StringUtils.isBlank(getLink())) {
            return getLink();
        }
        CmsSite site = getSite();
        StringBuilder url = site.getUrlBuffer(true, whole, false);
        url.append(SPT).append(getChannel().getPath());
        url.append(SPT).append(getId()).append(site.getDynamicSuffix());
        return url.toString();
    }
