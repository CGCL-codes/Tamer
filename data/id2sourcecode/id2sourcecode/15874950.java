    public void writeThreadInfo(long threadId, String threadName, int priority, String groupName, long currentTimeMillis) {
        try {
            out.writeByte(THREAD);
            out.writeLong(threadId);
            out.writeInt(priority);
            out.writeUTF(threadName);
            out.writeUTF(groupName);
            out.writeLong(currentTimeMillis);
        } catch (IOException e) {
            throw new JRatException("writeMethodDisabled failed", e);
        }
    }
