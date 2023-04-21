    public TraceOutput getTraceOutput() {
        if (out == null) {
            Thread thread = Thread.currentThread();
            long threadId = thread.getId();
            int priority = thread.getPriority();
            String name = thread.getName();
            ThreadGroup group = thread.getThreadGroup();
            String groupName = (group == null) ? null : group.getName();
            long now = System.currentTimeMillis();
            LOG.info("threadId = " + threadId);
            LOG.info("name = " + name);
            LOG.info("groupName = " + groupName);
            out = new TraceOutput(context.createOutputStream("ThreadTrace.jrat"));
            out.writeThreadInfo(threadId, name, priority, groupName, now);
            context.registerShutdownListener(this);
        }
        return out;
    }
