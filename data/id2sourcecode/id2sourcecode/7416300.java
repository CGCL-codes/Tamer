    private static void showThread() {
        write("Thread ");
        write(VM_Thread.getCurrentThread().getIndex());
        write(": ");
    }
