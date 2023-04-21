    private static void writeThreadIdToLog(VM_Thread t) {
        char[] buf = VM_Thread.grabDumpBuffer();
        int len = t.dump(buf);
        Log.write(buf, len);
        VM_Thread.releaseDumpBuffer();
    }
