    public void run() {
        if (constraintInactive) return;
        double curLA = getLastLA();
        if (curLA > highCpuLimit) {
            CurLimits curlimit = null;
            if (curLimits.isEmpty()) {
                curlimit = new CurLimits(getReadLimit(), getWriteLimit());
                if (curlimit.read == 0) {
                    curlimit.read = handler.getTrafficCounter().getLastReadThroughput();
                    if (curlimit.read < limitLowBandwidth) {
                        curlimit.read = 0;
                    }
                }
                if (curlimit.write == 0) {
                    curlimit.write = handler.getTrafficCounter().getLastWriteThroughput();
                    if (curlimit.write < limitLowBandwidth) {
                        curlimit.write = 0;
                    }
                }
            } else {
                curlimit = curLimits.getLast();
            }
            long newread = (long) (curlimit.read * (1 - percentageDecreaseRatio));
            if (newread < limitLowBandwidth) {
                newread = limitLowBandwidth;
            }
            long newwrite = (long) (curlimit.write * (1 - percentageDecreaseRatio));
            if (newwrite < limitLowBandwidth) {
                newwrite = limitLowBandwidth;
            }
            CurLimits newlimit = new CurLimits(newread, newwrite);
            if (curLimits.isEmpty() || curlimit.read != newread || curlimit.write != newwrite) {
                curLimits.add(newlimit);
                logger.debug("Set new low limit since CPU = " + curLA + " " + newwrite + ":" + newread);
                handler.configure(newlimit.write, newlimit.read);
                nbSinceLastDecrease += payload;
            }
        } else if (curLA < lowCpuLimit) {
            if (curLimits.isEmpty()) {
                return;
            }
            if (nbSinceLastDecrease > 0) {
                nbSinceLastDecrease--;
                return;
            }
            nbSinceLastDecrease = 0;
            curLimits.pollLast();
            CurLimits newlimit = null;
            if (curLimits.isEmpty()) {
                long newread = getReadLimit();
                long newwrite = getWriteLimit();
                logger.debug("restore limit since CPU = " + curLA + " " + newwrite + ":" + newread);
                handler.configure(newwrite, newread);
            } else {
                newlimit = curLimits.getLast();
                long newread = newlimit.read;
                long newwrite = newlimit.write;
                logger.debug("Set new upper limit since CPU = " + curLA + " " + newwrite + ":" + newread);
                handler.configure(newwrite, newread);
                nbSinceLastDecrease = payload;
            }
        }
    }
