    private void executeAllThreads(ByteBuffer bb, DataOutputStream os) throws JdwpException, IOException {
        ThreadGroup jdwpGroup = Thread.currentThread().getThreadGroup();
        ThreadGroup root = getRootThreadGroup(jdwpGroup);
        int numThreads = root.activeCount();
        Thread allThreads[] = new Thread[numThreads];
        root.enumerate(allThreads);
        numThreads = 0;
        for (int i = 0; i < allThreads.length; i++) {
            Thread thread = allThreads[i];
            if (thread == null) break;
            if (!thread.getThreadGroup().equals(jdwpGroup)) numThreads++;
        }
        os.writeInt(numThreads);
        for (int i = 0; i < allThreads.length; i++) {
            Thread thread = allThreads[i];
            if (thread == null) break;
            if (!thread.getThreadGroup().equals(jdwpGroup)) idMan.getObjectId(thread).write(os);
        }
    }
