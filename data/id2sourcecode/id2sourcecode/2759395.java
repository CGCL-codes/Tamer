    public ListRange getResults(Map map) {
        ListRange listRange = new ListRange();
        Map params = buildParamWithInt(map, "start", "limit");
        listRange.setData(channelMapper.getChannels(params).toArray());
        listRange.setTotalSize(channelMapper.getTotalResults(params));
        return listRange;
    }
