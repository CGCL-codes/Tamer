    public static void logThreadDump() {
        String inferCaller = inferCaller(5);
        logError(inferCaller);
        ScreenCapture.createScreenCapture("logThreadDump");
        logError(getThreadDump());
    }
