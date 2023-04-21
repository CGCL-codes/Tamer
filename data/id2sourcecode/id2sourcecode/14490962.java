    public static void handleSystemProperties() {
        String s;
        s = System.getProperty("scheduler.quanta");
        if (s != null) {
            try {
                jq_InterrupterThread.QUANTA = Integer.parseInt(s);
                Debug.writeln("Scheduler quanta is ", jq_InterrupterThread.QUANTA);
            } catch (NumberFormatException x) {
                Debug.writeln("Bad scheduler.quanta, ignoring.");
            }
        }
        s = System.getProperty("scheduler.transfer");
        if (s != null) {
            try {
                jq_NativeThread.TRANSFER_THRESHOLD = Float.parseFloat(s);
                Debug.write("Scheduler transfer threshold is ");
                System.err.println(jq_NativeThread.TRANSFER_THRESHOLD);
            } catch (NumberFormatException x) {
                Debug.writeln("Bad scheduler.transfer, ignoring.");
            }
        }
        s = System.getProperty("scheduler.stack");
        if (s != null) {
            try {
                jq_Thread.INITIAL_STACK_SIZE = Integer.parseInt(s);
                Debug.writeln("Thread stack size is ", jq_Thread.INITIAL_STACK_SIZE);
            } catch (NumberFormatException x) {
                Debug.writeln("Bad scheduler.stack, ignoring.");
            }
        }
    }
