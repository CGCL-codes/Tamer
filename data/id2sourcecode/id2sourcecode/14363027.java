    @Override
    protected void write() {
        super.write();
        writeStringNZ(32, name);
        write32(threadId);
        write32(callback_addr);
        write32(callback_arg_addr);
        write32(notifyCount);
        write32(notifyArg);
    }
