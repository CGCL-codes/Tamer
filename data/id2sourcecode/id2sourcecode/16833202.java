    private void writeThreadWarning(ThreadInfo info, ThreadInfo[] tinfos, long lastBlockStart) throws IOException {
        String msg = dateFormat.format(new Date()) + " - thread " + info.getThreadId() + " \"" + info.getThreadName() + "\" is blocked more than " + (info.getBlockedTime() - lastBlockStart) + "ms!";
        logger.debug(msg);
        output.write("\n" + msg + "\n");
        output.write(info.toString() + "");
        output.write("blocked by:\n");
        long blockedBy = info.getLockOwnerId();
        for (ThreadInfo i : tinfos) {
            if (i != null && i.getThreadId() == blockedBy) {
                output.write(i.toString());
                break;
            }
        }
    }
