    private void executeStatus(ByteBuffer bb, DataOutputStream os) throws JdwpException, IOException {
        ThreadId tid = (ThreadId) idMan.readObjectId(bb);
        Thread thread = tid.getThread();
        int threadStatus = VMVirtualMachine.getThreadStatus(thread);
        int suspendStatus = JdwpConstants.SuspendStatus.SUSPENDED;
        os.writeInt(threadStatus);
        os.writeInt(suspendStatus);
    }
