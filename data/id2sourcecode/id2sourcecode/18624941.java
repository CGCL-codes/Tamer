    private void executeName(ByteBuffer bb, DataOutputStream os) throws JdwpException, IOException {
        ThreadId tid = (ThreadId) idMan.readObjectId(bb);
        Thread thread = tid.getThread();
        JdwpString.writeString(os, thread.getName());
    }
