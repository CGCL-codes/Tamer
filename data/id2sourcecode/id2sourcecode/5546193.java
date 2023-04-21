    private void createLogFile(File logfile, int pageCount) throws IOException {
        final String METHOD = "createLogFile(File logfile,int pageCount)";
        if (TRC_LOGGER.isDebugMidEnabled()) {
            TRC_LOGGER.debugMid(METHOD + "|entry,logfile=" + logfile + ",pageCount=" + pageCount);
        }
        int groupCount = (pageCount / GROUP_PAGES) + 1;
        this.pageCount = groupCount * GROUP_PAGES;
        int groupSize = GROUP_PAGES * PAGE_SIZE;
        long fileSize = groupCount * groupSize;
        raf = new RandomAccessFile(logfile, "rw");
        if (fileSize != raf.length()) {
            raf.setLength(groupCount);
        }
        groups = new LogGroup[groupCount];
        for (int i = 0; i < groupCount; i++) {
            LogGroup group = new LogGroup();
            group.mbb = raf.getChannel().map(MapMode.READ_WRITE, groupSize * i, groupSize);
            group.pages = new BitSet(GROUP_PAGES);
            groups[i] = group;
        }
        if (TRC_LOGGER.isDebugMidEnabled()) {
            TRC_LOGGER.debugMid(METHOD + "|exit");
        }
    }
