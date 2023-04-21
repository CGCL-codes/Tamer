    @Override
    protected int findMinimumCounts() {
        int chminX = limits.getMinimumX();
        int chmaxX = limits.getMaximumX();
        int chminY = limits.getMinimumY();
        int chmaxY = limits.getMaximumY();
        int minCounts = 0;
        chminX = getChannelMin(chminX);
        chminY = getChannelMin(chminY);
        chmaxX = getChannelMax(chmaxX, size.getSizeX());
        chmaxY = getChannelMax(chmaxY, size.getSizeY());
        for (int i = chminX; i <= chmaxX; i++) {
            for (int j = chminY; j <= chmaxY; j++) {
                if (counts2d[i][j] < minCounts) {
                    minCounts = (int) counts2d[i][j];
                }
            }
        }
        return minCounts;
    }
