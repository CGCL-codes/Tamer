            public void run() {
                try {
                    Thread.sleep(2000);
                    writeToDebuggerProxy("<breakpoint file=\"\" line=\"55\" threadId=\"2\"/>");
                    writeToDebuggerProxy("<threads><thread id=\"1\" status=\"sleep\"/></threads>");
                } catch (Exception ex) {
                    fail();
                }
            }
