    @Transactional
    public void addStartURL(Channel chl) {
        StartURL startURL = new StartURL();
        startURL.setCreateTime(chl.getCreateTime());
        startURL.setCreateUser(chl.getCreateUser());
        startURL.setLastUpdateUser(chl.getLastUpdateUser());
        startURL.setSiteId(chl.getSiteId());
        startURL.setName(chl.getChannel());
        startURL.setJobname(chl.getSiteId() + "_" + chl.getId());
        startURL.setStatus(chl.getStatus());
        startURL.setStartURL(chl.getUrl());
        startURLMapper.insert(startURL);
    }
