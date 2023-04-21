    @Override
    protected void printStack(ThreadReference thread, PrintWriter writer, DebuggingContext dc) throws CommandException {
        if (!com.bluemarsh.jswat.console.Main.emulateJDB()) {
            super.printStack(thread, writer, dc);
            return;
        }
        StringBuilder sb = new StringBuilder(1024);
        if (!arg.isEmpty()) {
            sb.append(NbBundle.getMessage(getClass(), "CTL_where_header", thread.name()));
            sb.append('\n');
        }
        List<StackFrame> stack = getStack(thread);
        for (int i = dc.getFrame(), nFrames = stack.size(); i < nFrames; i++) {
            sb.append("  [");
            sb.append(i + 1);
            sb.append("] ");
            appendFrameDescriptor(stack.get(i).location(), sb);
            sb.append("\n");
        }
        int len = sb.length(), end = len - 1;
        String result;
        if ("all".equals(arg) && len > 0 && sb.charAt(end) == '\n') {
            result = sb.substring(0, end);
        } else {
            result = sb.toString();
        }
        writer.print(result);
    }
