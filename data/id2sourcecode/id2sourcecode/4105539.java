    TimeRelativeResponse afterTimeRelative(TimeRelativeRequest requestI, RequestOptions roI) throws com.rbnb.utility.SortException, com.rbnb.api.AddressException, com.rbnb.api.SerializeException, java.io.EOFException, java.io.IOException, java.lang.InterruptedException {
        TimeRelativeResponse responseR = new TimeRelativeResponse();
        responseR.setStatus(-1);
        boolean locked = false;
        try {
            lockRead("FrameManager.afterTimeRelative");
            locked = true;
            if (getRegistered() != null) {
                com.rbnb.utility.SortedVector channels = requestI.getByChannel();
                DataArray limits = getRegistered().extract(((TimeRelativeChannel) channels.firstElement()).getChannelName().substring(requestI.getNameOffset()));
                responseR.setTime(limits.getStartTime());
                responseR.setStatus(0);
                responseR.setInvert(false);
            } else if ((getParent() instanceof FileSet) && (getNchildren() != 0)) {
                responseR = super.afterTimeRelative(requestI, roI);
            }
        } finally {
            if (locked) {
                unlockRead();
            }
        }
        return (responseR);
    }
