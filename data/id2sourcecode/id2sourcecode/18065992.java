    protected void printStack(ThreadReference thread, PrintWriter writer, DebuggingContext dc) throws CommandException {
        List<StackFrame> stack = getStack(thread);
        boolean threadIsCurrent = false;
        ThreadReference currThrd = dc.getThread();
        if (currThrd != null && currThrd.equals(thread)) {
            threadIsCurrent = true;
        }
        StringBuilder sb = new StringBuilder(256);
        sb.append(getMessage("CTL_where_header", thread.name()));
        sb.append('\n');
        int nFrames = stack.size();
        if (nFrames == 0) {
            sb.append(getMessage("CTL_where_emptyStack"));
            sb.append('\n');
        }
        for (int i = 0; i < nFrames; i++) {
            Location loc = stack.get(i).location();
            if (threadIsCurrent) {
                if (dc.getFrame() == i) {
                    sb.append("* [");
                } else {
                    sb.append("  [");
                }
            } else {
                sb.append("  [");
            }
            sb.append(i);
            sb.append("] ");
            appendFrameDescriptor(loc, sb);
            long pc = loc.codeIndex();
            if (pc != -1) {
                sb.append(", pc = ");
                sb.append(pc);
            }
            sb.append('\n');
        }
        writer.print(sb.toString());
    }
