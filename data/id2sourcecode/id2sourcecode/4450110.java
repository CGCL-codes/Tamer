    public void init() {
        short zero = 0;
        byte bzero = 0;
        if (getViewsDay() == null) {
            setViewsDay(0);
        }
        if (getCommentsDay() == null) {
            setCommentsDay(zero);
        }
        if (getDownloadsDay() == null) {
            setDownloadsDay(zero);
        }
        if (getUpsDay() == null) {
            setUpsDay(zero);
        }
        if (getHasTitleImg() == null) {
            setHasTitleImg(false);
        }
        if (getRecommend() == null) {
            setRecommend(false);
        }
        if (getSortDate() == null) {
            setSortDate(new Timestamp(System.currentTimeMillis()));
        }
        if (getTopLevel() == null) {
            setTopLevel(bzero);
        }
        if (getChannels() == null) {
            setChannels(new HashSet<Channel>());
        }
        if (getTopics() == null) {
            setTopics(new HashSet<CmsTopic>());
        }
        if (getViewGroups() == null) {
            setViewGroups(new HashSet<CmsGroup>());
        }
        if (getTags() == null) {
            setTags(new ArrayList<ContentTag>());
        }
        if (getPictures() == null) {
            setPictures(new ArrayList<ContentPicture>());
        }
        if (getAttachments() == null) {
            setAttachments(new ArrayList<ContentAttachment>());
        }
    }
