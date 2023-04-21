    private void writeThreadGroupInfo(int iIndent, ThreadGroup tg, DefaultListModel dlm) {
        dlm.addElement(pad(iIndent) + "group: " + tg.getName());
        int iThreadCount = tg.activeCount();
        int iGroupCount = tg.activeGroupCount();
        Thread threads[] = new Thread[iThreadCount];
        tg.enumerate(threads, false);
        writeThreadInfo(iIndent + 3, threads, tg, dlm);
        ThreadGroup groups[] = new ThreadGroup[iGroupCount];
        tg.enumerate(groups, false);
        for (int iLoop = 0; (iLoop < groups.length) && (null != groups[iLoop]); iLoop++) {
            writeThreadGroupInfo(iIndent + 3, groups[iLoop], dlm);
        }
    }
