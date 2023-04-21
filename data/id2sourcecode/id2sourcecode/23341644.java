    public void execute(TestRun test) throws Exception {
        Class executorClass = test.getExecutorClass();
        Executor executor = (Executor) executorClass.newInstance();
        Duration readDuration = executor.read(test);
        Duration writeDuration = executor.write(test);
        System.err.printf("Execution of " + test.getId() + " " + test.getIterations() + " times: read=%s ms read-total=%s write=%s ms write-total=%s ms average-read=%s ms average-write=%s ms", readDuration.getOperation(), readDuration.getTotal(), writeDuration.getOperation(), writeDuration.getOperation(), readDuration.getAverage(), writeDuration.getAverage());
        System.err.println();
    }
