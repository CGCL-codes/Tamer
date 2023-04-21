    private static void dumpThreads(IndentWriter writer) {
        final ThreadMXBean threadBean = ManagementFactory.getThreadMXBean();
        long[] allThreadIds = threadBean.getAllThreadIds();
        writer.println("Threads " + allThreadIds.length);
        writer.indent();
        List<ThreadInfo> threadInfos = new ArrayList<ThreadInfo>(allThreadIds.length);
        for (int i = 0; i < allThreadIds.length; i++) {
            ThreadInfo info = threadBean.getThreadInfo(allThreadIds[i], 32);
            if (info != null) threadInfos.add(info);
        }
        if (!disable_getThreadCpuTime) {
            Collections.sort(threadInfos, new Comparator<ThreadInfo>() {

                public int compare(ThreadInfo o1, ThreadInfo o2) {
                    long diff = threadBean.getThreadCpuTime(o2.getThreadId()) - threadBean.getThreadCpuTime(o1.getThreadId());
                    if (diff == 0) {
                        return o1.getThreadName().compareToIgnoreCase(o2.getThreadName());
                    }
                    return diff > 0 ? 1 : -1;
                }
            });
        }
        for (int i = 0; i < threadInfos.size(); i++) {
            try {
                ThreadInfo threadInfo = threadInfos.get(i);
                long lCpuTime = disable_getThreadCpuTime ? -1 : threadBean.getThreadCpuTime(threadInfo.getThreadId());
                if (lCpuTime == 0) break;
                String sState;
                switch(threadInfo.getThreadState()) {
                    case BLOCKED:
                        sState = "Blocked";
                        break;
                    case RUNNABLE:
                        sState = "Runnable";
                        break;
                    case NEW:
                        sState = "New";
                        break;
                    case TERMINATED:
                        sState = "Terminated";
                        break;
                    case TIMED_WAITING:
                        sState = "Timed Waiting";
                        break;
                    case WAITING:
                        sState = "Waiting";
                        break;
                    default:
                        sState = "" + threadInfo.getThreadState();
                        break;
                }
                String sName = threadInfo.getThreadName();
                String sLockName = threadInfo.getLockName();
                writer.println(sName + ": " + sState + ", " + (lCpuTime / 1000000) + "ms CPU, " + "B/W: " + threadInfo.getBlockedCount() + "/" + threadInfo.getWaitedCount() + (sLockName == null ? "" : "; Locked by " + sLockName + "/" + threadInfo.getLockOwnerName()));
                writer.indent();
                try {
                    StackTraceElement[] stackTrace = threadInfo.getStackTrace();
                    for (int j = 0; j < stackTrace.length; j++) {
                        writer.println(stackTrace[j].toString());
                    }
                } finally {
                    writer.exdent();
                }
            } catch (Exception e) {
            }
        }
        writer.exdent();
    }
