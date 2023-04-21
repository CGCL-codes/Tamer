    public void write(ByteBuffer buf) {
        PersistenceUtil.write(buf, command);
        buf.putInt(pid);
        buf.putInt(uid);
        buf.putInt(state_policy_cpu_priority);
        PersistenceUtil.write(buf, threadName);
    }
