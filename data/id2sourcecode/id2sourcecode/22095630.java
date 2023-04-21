    public final Object sortField(Object sidI) throws SortException {
        if (sidI != SORT_CHANNEL_NAME) {
            throw new com.rbnb.utility.SortException("The sort identifier is not valid.");
        }
        return (getChannelName());
    }
