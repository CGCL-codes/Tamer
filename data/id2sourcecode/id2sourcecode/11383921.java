    public static long getPossitionForTimeInMpeg(File f, int timeS) throws IOException {
        RandomAccessFile raf = new RandomAccessFile(f, "r");
        Map<Integer, Integer> ptsStart = checkRange(raf, 0, 250000, false);
        long currentPos = 0;
        if (ptsStart != null && !ptsStart.isEmpty()) {
            long minRangePos = 0;
            long maxRangePos = raf.length();
            boolean nextPossition = true;
            while (maxRangePos - minRangePos > 250000 && nextPossition) {
                nextPossition = false;
                currentPos = minRangePos + (maxRangePos - minRangePos) / 2;
                Map<Integer, Integer> ptsEnd = checkRange(raf, currentPos, 250000, false);
                if (ptsEnd != null) {
                    Iterator<Integer> iterator = ptsStart.keySet().iterator();
                    while (iterator.hasNext()) {
                        Integer id = iterator.next();
                        if (ptsEnd.get(id) != null) {
                            int time = (ptsEnd.get(id).intValue() - ptsStart.get(id).intValue()) / 90000;
                            if (time == timeS) {
                                return currentPos;
                            }
                            nextPossition = true;
                            if (time > timeS) {
                                maxRangePos = currentPos;
                            } else {
                                minRangePos = currentPos;
                            }
                            break;
                        }
                    }
                } else {
                    return currentPos;
                }
            }
        }
        return currentPos;
    }
