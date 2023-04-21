    public String getStaticFilename(int pageNo) {
        CmsSite site = getSite();
        StringBuilder url = new StringBuilder();
        String staticDir = site.getStaticDir();
        if (!StringUtils.isBlank(staticDir)) {
            url.append(staticDir);
        }
        String filename = getStaticFilenameByRule();
        if (!StringUtils.isBlank(filename)) {
            int index = filename.indexOf(".", filename.lastIndexOf("/"));
            if (pageNo > 1) {
                if (index != -1) {
                    url.append(filename.substring(0, index));
                    url.append("_").append(pageNo);
                    url.append(filename.substring(index));
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
