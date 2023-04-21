    @Override
    public void run() {
        try {
            while (true) {
                Task task = Utilities.getTaskQueue().take();
                if (task.info[0] != null) {
                    if (delayTime > 0) {
                        sleep(delayTime);
                    }
                    int taskType = (Integer) task.info[0];
                    Thread thread = (Thread) task.info[1];
                    String dateTimeStamp = (String) task.info[2];
                    String logPath = (String) task.info[3];
                    Object output = task.info[4];
                    if (taskType == LOG) {
                        if (Utilities.loggingMode == Utilities.LOGGING_BASIC || Utilities.loggingMode == Utilities.LOGGING_FULL) {
                            if (logPath.equals(Utilities.LOG_OUTPUT)) {
                                System.out.println(output);
                            } else if (logPath.equals(Utilities.LOG_ERROR)) {
                                System.err.println(output);
                            }
                        }
                        if (Utilities.loggingMode == Utilities.LOGGING_FILES_ONLY || Utilities.loggingMode == Utilities.LOGGING_FULL) {
                            try {
                                if (parcialMessages.containsKey(thread.getId() + logPath)) {
                                    output = (String) parcialMessages.get(thread.getId() + logPath) + output;
                                    parcialMessages.remove(thread.getId() + logPath);
                                }
                                if (!writers.containsKey(logPath)) {
                                    addWriter(logPath);
                                }
                                OutputStreamWriter writer = writers.get(logPath);
                                writer.write(dateTimeStamp + ": " + output + " (" + thread.getName() + ")" + System.getProperty("line.separator"));
                                writer.flush();
                            } catch (IOException ioe) {
                                ioe.printStackTrace();
                            }
                        }
                    } else if (taskType == LOG_PARCIAL) {
                        if (Utilities.loggingMode == Utilities.LOGGING_BASIC || Utilities.loggingMode == Utilities.LOGGING_FULL) {
                            if (logPath.equals(Utilities.LOG_OUTPUT)) {
                                System.out.print(output);
                            } else if (logPath.equals(Utilities.LOG_ERROR)) {
                                System.err.print(output);
                            }
                        }
                        if (Utilities.loggingMode == Utilities.LOGGING_FILES_ONLY || Utilities.loggingMode == Utilities.LOGGING_FULL) {
                            if (parcialMessages.containsKey(thread.getId() + logPath)) {
                                output = (String) parcialMessages.get(thread.getId() + logPath) + output;
                            }
                            parcialMessages.put(thread.getId() + logPath, output);
                        }
                    }
                }
                task.complete();
            }
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }
    }
