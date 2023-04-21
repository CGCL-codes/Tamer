    public Set<CmsGroup> getViewGroupsExt() {
        Set<CmsGroup> set = getViewGroups();
        if (set != null && set.size() > 0) {
            return set;
        } else {
            return getChannel().getViewGroups();
        }
    }
