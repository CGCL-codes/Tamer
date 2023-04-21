    final TimeRelativeResponse matchTimeRelative(TimeRelativeRequest requestI, RequestOptions roI, int nPointsI) {
        TimeRelativeResponse responseR = new TimeRelativeResponse();
        if (getNptimes() == 1) {
            if (getDuration() == 0.) {
                switch(requestI.getRelationship()) {
                    case TimeRelativeRequest.AT_OR_BEFORE:
                    case TimeRelativeRequest.AT_OR_AFTER:
                        responseR.setStatus(0);
                        responseR.setTime(getTime());
                        responseR.setInvert(requestI.getRelationship() == TimeRelativeRequest.AT_OR_BEFORE);
                        break;
                    case TimeRelativeRequest.BEFORE:
                        responseR.setStatus(-1);
                        break;
                    case TimeRelativeRequest.AFTER:
                        responseR.setStatus(1);
                        break;
                }
            } else {
                double offset = requestI.getTimeRange().getTime() - getTime();
                double step = getDuration() / nPointsI;
                int point_old = (int) (offset / step);
                int point_round = (int) (0.5 + offset / step);
                int point = (int) (offset / step + 0.00000000000005);
                switch(requestI.getRelationship()) {
                    case TimeRelativeRequest.BEFORE:
                        if (--point < 0) {
                            responseR.setStatus(-1);
                        } else {
                            responseR.setTime(getTime() + (point + 1) * step);
                            responseR.setInvert(true);
                        }
                        break;
                    case TimeRelativeRequest.AT_OR_BEFORE:
                        if ((roI != null) && roI.getExtendStart()) {
                            responseR.setTime(getTime() + point * step);
                        } else {
                            responseR.setTime(getTime() + (point + 1) * step);
                            responseR.setInvert(true);
                        }
                        break;
                    case TimeRelativeRequest.AT_OR_AFTER:
                        if (point * step == offset) {
                            responseR.setTime(getTime() + point * step);
                            responseR.setInvert(false);
                        } else if (++point == nPointsI) {
                            responseR.setStatus(1);
                        } else {
                            responseR.setTime(getTime() + point * step);
                            responseR.setInvert(false);
                        }
                        break;
                    case TimeRelativeRequest.AFTER:
                        if (++point >= nPointsI) {
                            responseR.setStatus(1);
                        } else {
                            if (requestI.getTimeRange().getDuration() == 0.0) {
                                responseR.setTime(getTime() + point * step + 0.00000000000005);
                            } else {
                                responseR.setTime(getTime() + point * step - 0.00000000000005);
                            }
                            responseR.setInvert(false);
                        }
                }
            }
        } else {
            int lo = 0;
            int hi = nPointsI - 1;
            int lastIdx = 0;
            int idx;
            double direction = 0.;
            double time = 0.;
            for (idx = (lo + hi) / 2; lo <= hi; idx = (lo + hi) / 2) {
                time = getPointTime(idx, nPointsI);
                direction = requestI.getTimeRange().getTime() - time;
                lastIdx = idx;
                if (direction < 0.) {
                    hi = idx - 1;
                } else if (direction == 0.) {
                    break;
                } else {
                    lo = idx + 1;
                }
            }
            if (lo <= hi) {
                double lPtTime = 0.;
                double wPtTime = getPointTime(lastIdx, nPointsI);
                switch(requestI.getRelationship()) {
                    case TimeRelativeRequest.AT_OR_BEFORE:
                    case TimeRelativeRequest.AT_OR_AFTER:
                        responseR.setStatus(0);
                        responseR.setTime(time);
                        responseR.setInvert(requestI.getRelationship() == TimeRelativeRequest.AT_OR_BEFORE);
                        break;
                    case TimeRelativeRequest.BEFORE:
                        for (; lastIdx > 0; --lastIdx) {
                            lPtTime = getPointTime(lastIdx - 1, nPointsI);
                            if (lPtTime < wPtTime) {
                                break;
                            }
                        }
                        if (lastIdx == 0) {
                            responseR.setStatus(-1);
                        } else {
                            responseR.setTime(lPtTime);
                            responseR.setInvert(true);
                        }
                        break;
                    case TimeRelativeRequest.AFTER:
                        for (; lastIdx < nPointsI - 1; ++lastIdx) {
                            lPtTime = getPointTime(lastIdx + 1, nPointsI);
                            if (lPtTime > wPtTime) {
                                break;
                            }
                        }
                        if (lastIdx == nPointsI - 1) {
                            responseR.setStatus(1);
                        } else {
                            responseR.setTime(lPtTime);
                            responseR.setInvert(false);
                        }
                        break;
                }
            } else if ((lastIdx == 0) && (direction < 0)) {
                responseR.setStatus(-1);
            } else if ((lastIdx == nPointsI - 1) && (direction > 0)) {
                responseR.setStatus(1);
            } else {
                if (direction > 0) {
                    ++lastIdx;
                }
                switch(requestI.getRelationship()) {
                    case TimeRelativeRequest.BEFORE:
                    case TimeRelativeRequest.AT_OR_BEFORE:
                        responseR.setTime(getPointTime(lastIdx - 1, nPointsI));
                        responseR.setInvert(true);
                        break;
                    case TimeRelativeRequest.AT_OR_AFTER:
                    case TimeRelativeRequest.AFTER:
                        responseR.setTime(getPointTime(lastIdx, nPointsI));
                        responseR.setInvert(false);
                        break;
                }
            }
        }
        return (responseR);
    }
