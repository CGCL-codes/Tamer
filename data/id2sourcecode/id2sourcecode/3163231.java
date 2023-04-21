    private void appendList(List<StacktraceElement> list, LuaThread thread) {
        while (thread != null) {
            int top = thread.callFrameTop;
            for (int i = top - 1; i >= 0; i--) {
                LuaCallFrame frame = thread.callFrameStack[i];
                int pc = frame.pc - 1;
                LuaClosure closure = frame.closure;
                JavaFunction javaFunction = frame.javaFunction;
                if (closure != null) {
                    list.add(new LuaStacktraceElement(pc, closure.prototype));
                } else if (javaFunction != null) {
                    list.add(new JavaStacktraceElement(javaFunction));
                }
            }
            thread = thread.parent;
        }
    }
