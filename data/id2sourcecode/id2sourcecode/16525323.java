    public void testMultipleBreakpoints() throws Exception {
        writeToDebuggerProxy("<breakpoint file=\"\" line=\"44\" threadId=\"2\"/>");
        Thread.sleep(2000);
        assertNotNull(getTarget().getLastSuspensionPoint());
        assertEquals(44, getTarget().getLastSuspensionPoint().getLine());
        new Thread() {

            public void run() {
                try {
                    Thread.sleep(2000);
                    writeToDebuggerProxy("<breakpoint file=\"\" line=\"55\" threadId=\"2\"/>");
                    writeToDebuggerProxy("<threads><thread id=\"1\" status=\"sleep\"/></threads>");
                } catch (Exception ex) {
                    fail();
                }
            }
        }.start();
        ThreadInfo[] threadInfos = getProxy().readThreads();
        assertEquals(1, threadInfos.length);
        Thread.sleep(1000);
        assertEquals(55, getTarget().getLastSuspensionPoint().getLine());
    }
