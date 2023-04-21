    TimeRelativeResponse beforeTimeRelative(TimeRelativeRequest requestI, RequestOptions roI) throws com.rbnb.utility.SortException, com.rbnb.api.AddressException, com.rbnb.api.SerializeException, java.io.EOFException, java.io.IOException, java.lang.InterruptedException {
        TimeRelativeResponse responseR = new TimeRelativeResponse();
        responseR.setStatus(-1);
        boolean locked = false;
        try {
            lockRead();
            locked = true;
            if (getRegistered() != null) {
                if ((roI != null) && roI.getExtendStart()) {
                    Rmap lastChild = getChildAt(getNchildren() - 1);
                    responseR = lastChild.beforeTimeRelative(requestI, roI);
                } else {
                    com.rbnb.utility.SortedVector channels = requestI.getByChannel();
                    DataArray limits = getRegistered().extract(((TimeRelativeChannel) channels.firstElement()).getChannelName().substring(requestI.getNameOffset()));
                    if ((limits.timeRanges != null) && (limits.timeRanges.size() != 0)) {
                        responseR.setTime(limits.getStartTime() + limits.getDuration());
                        responseR.setStatus(0);
                        responseR.setInvert(true);
                    }
                }
            }
        } finally {
            if (locked) {
                unlockRead();
            }
        }
        return (responseR);
    }
