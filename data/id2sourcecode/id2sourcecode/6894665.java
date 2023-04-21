    final Rmap buildRequest(TimeRelativeRequest requestI, RequestOptions optionsI) throws com.rbnb.utility.SortException, com.rbnb.api.AddressException, com.rbnb.api.SerializeException, java.io.EOFException, java.io.IOException, java.lang.InterruptedException {
        Rmap requestR = new DataRequest();
        TimeRelativeChannel trc;
        Rmap trChild;
        Rmap child = null;
        for (int idx = 0; idx < requestI.getByChannel().size(); ++idx) {
            trc = (TimeRelativeChannel) requestI.getByChannel().elementAt(idx);
            trChild = Rmap.createFromName("/" + trc.getChannelName());
            trChild.moveToBottom().setDblock(Rmap.MarkerBlock);
            if (child == null) {
                child = trChild;
            } else {
                child = child.mergeWith(trChild);
            }
        }
        double ltime = getTime();
        double lduration = requestI.getTimeRange().getDuration();
        boolean ldirection = getInvert();
        if ((optionsI != null) && optionsI.getExtendStart()) {
            if (ltime != 0.) {
                long lltime = Double.doubleToLongBits(ltime);
                lltime--;
                ltime = Double.longBitsToDouble(lltime);
            }
            lduration += requestI.getTimeRange().getTime() - ltime;
            ldirection = false;
        } else {
            switch(requestI.getRelationship()) {
                case TimeRelativeRequest.BEFORE:
                case TimeRelativeRequest.AT_OR_BEFORE:
                    ltime -= lduration;
                    break;
                case TimeRelativeRequest.AFTER:
                case TimeRelativeRequest.AT_OR_AFTER:
                    break;
            }
        }
        child.setTrange(new TimeRange(ltime, lduration));
        child.getTrange().setDirection(ldirection);
        requestR.addChild(child);
        return (requestR);
    }
