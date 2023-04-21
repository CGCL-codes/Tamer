    public String executeR(Map<String, String> commandLine) throws IOException {
        List<String> command = new ArrayList<String>();
        command.add("R");
        command.add("--no-save");
        command.add("--no-restore");
        command.add("--no-readline");
        for (String s : commandLine.keySet()) {
            command.add(s + "=" + commandLine.get(s));
        }
        final Process process = new ProcessBuilder(command).start();
        final InputStream rDriverInput = getInputStreamForRScript(RProcessorBase.class, getRDriverName());
        final StringBuffer rExecutionOutput = new StringBuffer();
        final IOException[] exceptionArray = new IOException[2];
        Thread input = new Thread() {

            public void run() {
                try {
                    int read;
                    while ((read = rDriverInput.read()) != -1) {
                        process.getOutputStream().write(read);
                    }
                    process.getOutputStream().close();
                } catch (IOException iop) {
                    exceptionArray[0] = iop;
                    throw new RuntimeException("Problem with reading R Script, or writing to process", iop);
                }
            }
        };
        input.setDaemon(true);
        input.start();
        Thread output = new Thread() {

            public void run() {
                try {
                    rExecutionOutput.append(readStringFromInputStream(process.getInputStream()));
                } catch (IOException iop) {
                    exceptionArray[1] = iop;
                    throw new RuntimeException("Problem with reading result from R process", iop);
                }
            }
        };
        output.setDaemon(true);
        output.start();
        try {
            process.waitFor();
        } catch (InterruptedException inexp) {
            Thread.currentThread().interrupt();
        }
        process.getInputStream().close();
        process.getErrorStream().close();
        if (exceptionArray[0] != null) {
            throw exceptionArray[0];
        }
        if (exceptionArray[1] != null) {
            throw exceptionArray[1];
        }
        return rExecutionOutput.toString();
    }
