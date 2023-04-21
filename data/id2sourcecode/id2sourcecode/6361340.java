    public void checkPoing(int checkPoint, long readLength, long writeLength, long inTime) {
        long now = System.currentTimeMillis();
        if (checkPoint == CHECK_POINT_START) {
            processTimes[checkPoint] = now;
            readLengths[checkPoint] = readLength;
            writeLengths[checkPoint] = writeLength;
        } else {
            readLengths[checkPoint] = readLength - lastData(readLengths, checkPoint - 1);
            writeLengths[checkPoint] = writeLength - lastData(writeLengths, checkPoint - 1);
            if (inTime >= 0) {
                processTimes[checkPoint] = now - inTime;
            } else {
                processTimes[checkPoint] = now - lastData(processTimes, checkPoint);
            }
        }
        processTimes[checkPoint + 1] = now;
        synchronized (this) {
            this.checkPoint = checkPoint;
            notify();
        }
    }
