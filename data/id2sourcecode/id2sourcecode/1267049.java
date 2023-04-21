    public static int run(String runId, long timeLimit, long memoryLimit, File input, File output) {
        timeWaste = 0;
        memoryWaste = 0;
        status = System_Error;
        process = null;
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {

            public void run() {
                process.destroy();
                status = Time_Limit_Exceeded;
            }
        };
        Runtime runtime = Runtime.getRuntime();
        long before = runtime.freeMemory();
        long after = before;
        try {
            process = runtime.exec(RUN + runId + "\\Main.exe");
            after = runtime.freeMemory();
            memoryWaste = before - after;
            if (memoryWaste > memoryLimit) {
                process.destroy();
                return Memory_Limit_Exceeded;
            }
            String stdIn = StreamHandler.read(input);
            StreamHandler.write(process.getOutputStream(), stdIn);
            Date startTime = new Date();
            timer.schedule(task, timeLimit);
            String userOut = getUserOut();
            process.waitFor();
            int exitValue = process.exitValue();
            if (exitValue != 1 && exitValue != 0) {
                task.cancel();
                process.destroy();
                return Runtime_Error;
            }
            timeWaste = timeLimit;
            if (status == System_Error) {
                task.cancel();
                Date endTime = new Date();
                timeWaste = endTime.getTime() - startTime.getTime();
                int result = Matcher.matchDirectly(output, userOut);
                status = result;
            }
        } catch (Exception e) {
            log.error("----judging error in:" + runId + "----");
            e.printStackTrace();
            if (process.exitValue() != 0) {
                process.destroy();
            }
            return System_Error;
        }
        process.destroy();
        return status;
    }
