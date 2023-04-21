    @Override
    void doJob() {
        long[] tids = null;
        ThreadInfo[] tinfos = null;
        try {
            tids = threadBean.getAllThreadIds();
            tinfos = threadBean.getThreadInfo(tids, true, true);
        } catch (java.lang.reflect.UndeclaredThrowableException ex) {
            if (ex.getCause() != null && ex.getCause() instanceof java.rmi.ConnectException) {
                logger.warn("remote VM was probably stopped");
                stop();
            } else {
                logger.error("undeclared exception", ex);
            }
            return;
        }
        Set<Long> blockedTids = new HashSet<Long>();
        long tid;
        BlockedThreadInfo blockInfo;
        boolean flushOutput = false;
        for (ThreadInfo info : tinfos) {
            if (info != null && info.getThreadState() == Thread.State.BLOCKED) {
                tid = info.getThreadId();
                blockedTids.add(tid);
                blockInfo = blockedThreadsMap.get(tid);
                if (blockInfo == null) {
                    blockInfo = new BlockedThreadInfo(info);
                    blockedThreadsMap.put(tid, blockInfo);
                } else {
                    if (blockInfo.getBlockedCount() != info.getBlockedCount()) {
                        blockInfo.setBlockedCount(info.getBlockedCount());
                        blockInfo.setBlockedTime(info.getBlockedTime());
                        blockInfo.setWarned(false);
                    } else {
                        if (info.getBlockedTime() - blockInfo.getBlockedTime() >= warningTime && (!blockInfo.warnedAlready())) {
                            try {
                                if (!flushOutput) output.write(delimiter);
                                writeThreadWarning(info, tinfos, blockInfo.getBlockedTime());
                            } catch (IOException ex) {
                                logger.error("exception while store blocked thread stat", ex);
                            } finally {
                                flushOutput = true;
                                blockInfo.setWarned(true);
                            }
                        }
                    }
                }
            }
        }
        Set<Long> copy = new HashSet<Long>(blockedThreadsMap.keySet());
        for (Long id : copy) {
            if (!blockedTids.contains(id)) blockedThreadsMap.remove(id);
        }
        if (flushOutput) {
            try {
                output.flush();
            } catch (IOException ex) {
                logger.error("exception while flushing output", ex);
            }
        }
    }
