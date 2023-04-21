    public List getChannelSequence(String channelStr) {
        String[] channels = channelStr.split("\\|");
        List ls = null;
        ls = Arrays.asList(channels);
        Collections.sort(ls, new AjaxChannelPreferenceComparator());
        return ls;
    }
