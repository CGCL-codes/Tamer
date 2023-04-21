    TimeRelativeResponse beforeTimeRelative(TimeRelativeRequest requestI, RequestOptions roI) throws com.rbnb.utility.SortException, com.rbnb.api.AddressException, com.rbnb.api.SerializeException, java.io.EOFException, java.io.IOException, java.lang.InterruptedException {
        TimeRelativeResponse responseR = new TimeRelativeResponse();
        responseR.setStatus(-1);
        boolean locked = false;
        try {
            lockRead("FrameSet.beforeTimeRelative");
            locked = true;
            if (((roI == null) || !roI.getExtendStart()) && (getRegistered() != null)) {
                com.rbnb.utility.SortedVector channels = requestI.getByChannel();
                DataArray limits = getRegistered().extract(((TimeRelativeChannel) channels.firstElement()).getChannelName().substring(requestI.getNameOffset()));
                responseR.setTime(limits.getStartTime() + limits.getDuration());
                responseR.setStatus(0);
                responseR.setInvert(true);
            } else if ((((roI != null) && roI.getExtendStart()) || (getParent() instanceof FileSet)) && (getNchildren() != 0)) {
                responseR = super.beforeTimeRelative(requestI, roI);
            }
        } finally {
            if (locked) {
                unlockRead();
            }
        }
        return (responseR);
    }
