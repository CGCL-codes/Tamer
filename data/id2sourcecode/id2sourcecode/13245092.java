    public String getUrlStatic(Boolean whole, int pageNo) {
        if (!StringUtils.isBlank(getLink())) {
            return getLink();
        }
        CmsSite site = getSite();
        StringBuilder url = site.getUrlBuffer(false, whole, false);
        String filename = getStaticFilenameByRule();
        if (!StringUtils.isBlank(filename)) {
            if (pageNo > 1) {
                int index = filename.indexOf(".", filename.lastIndexOf("/"));
                if (index != -1) {
                    url.append(filename.subSequence(0, index)).append("_");
                    url.append(pageNo).append(filename.subSequence(index, filename.length()));
                } else {
                    url.append(filename).append("_").append(pageNo);
                }
            } else {
                url.append(filename);
            }
        } else {
            url.append(SPT).append(getChannel().getPath());
            url.append(df.format(getReleaseDate()));
            url.append(SPT).append(getId());
            if (pageNo > 1) {
                url.append("_").append(pageNo);
            }
            url.append(site.getStaticSuffix());
        }
        return url.toString();
    }
