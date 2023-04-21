    public int[] readDataMinMaxInterval() {
        int[] tempRet = new int[this.getDataLayout().getChannelCount()];
        for (int k = 0; k < tempRet.length; k++) {
            tempRet[k] = readDataTraceMinMaxInterval(k);
        }
        return tempRet;
    }
