    final TimeRelativeResponse matchTimeRelative(TimeRelativeRequest requestI, RequestOptions roI) throws com.rbnb.utility.SortException, com.rbnb.api.AddressException, com.rbnb.api.SerializeException, java.io.EOFException, java.io.IOException, java.lang.InterruptedException {
        TimeRelativeResponse responseR = new TimeRelativeResponse();
        boolean locked = false;
        try {
            getDoor().lockRead("StorageManager.matchTimeRelative");
            locked = true;
            com.rbnb.utility.SortedVector toMatch = requestI.getByChannel();
            java.util.Hashtable requests = new java.util.Hashtable();
            TimeRelativeRequest request;
            String channelName;
            DataArray limits;
            TimeRelativeChannel trc;
            int direction;
            int finalDirection = 2;
            int idx;
            FrameManager fm;
            for (idx = 0; (finalDirection != -2) && (idx < toMatch.size()); ++idx) {
                trc = (TimeRelativeChannel) toMatch.elementAt(idx);
                channelName = trc.getChannelName().substring(requestI.getNameOffset());
                limits = getRegistered().extract(channelName);
                if ((limits.timeRanges == null) || (limits.timeRanges.size() == 0)) {
                    continue;
                } else {
                    direction = requestI.compareToLimits(limits);
                    if (idx == 0) {
                        finalDirection = direction;
                    } else if (direction != finalDirection) {
                        finalDirection = -2;
                    }
                }
            }
            if (finalDirection == 2) {
                responseR.setStatus(-1);
            } else if (finalDirection != 0) {
                responseR.setStatus(finalDirection);
            } else {
                int lo = 0;
                int hi = getNchildren() - 1;
                int lastIdx = 0;
                int lastGoodStatus = Integer.MIN_VALUE;
                responseR.setStatus(-1);
                for (idx = (lo + hi) / 2; (responseR.getStatus() != -2) && (responseR.getStatus() != 0) && (lo <= hi); idx = (lo + hi) / 2) {
                    fm = (FrameManager) getChildAt(idx);
                    responseR = fm.matchTimeRelative(requestI, roI);
                    if ((responseR.getStatus() != -3) && (responseR.getStatus() != 3)) {
                        lastGoodStatus = responseR.getStatus();
                    }
                    lastIdx = idx;
                    switch(responseR.getStatus()) {
                        case -2:
                            break;
                        case -1:
                        case -3:
                            hi = idx - 1;
                            break;
                        case 0:
                            break;
                        case 1:
                        case 3:
                            lo = idx + 1;
                            break;
                    }
                }
                if (((responseR.getStatus() == -1) && (lastIdx > 0)) || ((responseR.getStatus() == 1) && (lastIdx < getNchildren() - 1))) {
                    if (responseR.getStatus() == 1) {
                        ++lastIdx;
                    }
                    switch(requestI.getRelationship()) {
                        case TimeRelativeRequest.BEFORE:
                        case TimeRelativeRequest.AT_OR_BEFORE:
                            fm = (FrameManager) getChildAt(lastIdx - 1);
                            responseR = fm.beforeTimeRelative(requestI, roI);
                            if ((responseR.getStatus() == -3) || (responseR.getStatus() == 3)) {
                                responseR.setStatus(-1);
                            }
                            break;
                        case TimeRelativeRequest.AT_OR_AFTER:
                        case TimeRelativeRequest.AFTER:
                            fm = (FrameManager) getChildAt(lastIdx);
                            responseR = fm.afterTimeRelative(requestI, roI);
                            if ((responseR.getStatus() == -3) || (responseR.getStatus() == 3)) {
                                responseR.setStatus(1);
                            }
                            break;
                    }
                }
                if ((responseR.getStatus() == -3) || (responseR.getStatus() == 3)) {
                    if (lastGoodStatus != Integer.MIN_VALUE) {
                        responseR.setStatus(lastGoodStatus);
                    }
                }
            }
        } finally {
            if (locked) {
                getDoor().unlockRead();
            }
        }
        return (responseR);
    }
