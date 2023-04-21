    private boolean enter(int requested, Thread t, boolean block) {
        QueueCell cell = null;
        int loopc = 0;
        for (; ; ) {
            loopc++;
            synchronized (LOCK) {
                ThreadInfo info = getThreadInfo(t);
                if (info != null) {
                    if (grantedMode == NONE) {
                        throw new IllegalStateException();
                    }
                    if (((info.mode == S) && (grantedMode == X)) || ((info.mode == X) && (grantedMode == S))) {
                        throw new IllegalStateException();
                    }
                    if ((info.mode == X) || (info.mode == requested)) {
                        if (info.forced) {
                            info.forced = false;
                        } else {
                            if ((requested == X) && (info.counts[S] > 0)) {
                                IllegalStateException e = new IllegalStateException("WARNING: Going from readAccess to writeAccess, see #10778: http://www.netbeans.org/issues/show_bug.cgi?id=10778 ");
                                if (beStrict) {
                                    throw e;
                                }
                                ErrorManager.getDefault().notify(e);
                            }
                            info.counts[requested]++;
                            if ((requested == S) && (info.counts[requested] == 1)) {
                                readersNo++;
                            }
                        }
                        return true;
                    } else if (canUpgrade(info.mode, requested)) {
                        IllegalStateException e = new IllegalStateException("WARNING: Going from readAccess to writeAccess, see #10778: http://www.netbeans.org/issues/show_bug.cgi?id=10778 ");
                        if (beStrict) {
                            throw e;
                        }
                        ErrorManager.getDefault().notify(e);
                        info.mode = X;
                        info.counts[requested]++;
                        info.rsnapshot = info.counts[S];
                        if (grantedMode == S) {
                            grantedMode = X;
                        } else if (grantedMode == X) {
                            throw new IllegalStateException();
                        }
                        return true;
                    } else {
                        IllegalStateException e = new IllegalStateException("WARNING: Going from readAccess to writeAccess through queue, see #10778: http://www.netbeans.org/issues/show_bug.cgi?id=10778 ");
                        if (beStrict) {
                            throw e;
                        }
                        ErrorManager.getDefault().notify(e);
                    }
                } else {
                    if (isCompatible(requested)) {
                        grantedMode = requested;
                        registeredThreads.put(t, info = new ThreadInfo(t, requested));
                        if (requested == S) {
                            readersNo++;
                        }
                        return true;
                    }
                }
                if (!block) {
                    return false;
                }
                grantedMode = CHAIN;
                cell = chain(requested, t, 0);
            }
            cell.sleep();
        }
    }
