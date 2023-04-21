    @Override
    public void run() {
        try {
            ByteBuffer buffer = ByteBuffer.allocate(1024 * 10);
            Iterator testCaseIterator = question.getTestCases().iterator();
            while (testCaseIterator.hasNext()) {
                TestCase testCase = (TestCase) testCaseIterator.next();
                File tempIn = File.createTempFile("rage", ".tmp");
                File tempOut = File.createTempFile("rage", ".tmp");
                FileOutputStream outFile = new FileOutputStream(tempIn);
                FileChannel outChannel = outFile.getChannel();
                outChannel.lock();
                for (int i = 0; i < testCase.getInputs().size(); i++) {
                    buffer.put(testCase.getInputs().get(i).getBytes());
                    buffer.put(System.getProperty("line.separator").getBytes());
                    buffer.flip();
                    outChannel.write(buffer);
                    buffer.clear();
                }
                outFile.close();
                outChannel.close();
                List<String> cmd = new ArrayList<String>();
                if (type == RAGEConst.RAPTOR_QUESTION) {
                    cmd.add(node.get("RaptorExecutable", RAGEConst.DEFAULT_RAPTOR_EXECUTABLE));
                    cmd.add("\"" + testFile.getCanonicalPath() + "\"");
                    cmd.add("/run");
                    cmd.add("\"" + tempIn.getCanonicalPath() + "\"");
                    cmd.add("\"" + tempOut.getCanonicalPath() + "\"");
                } else if (type == RAGEConst.PROCESSING_QUESTION) {
                    cmd.add(node.get("RunmeExecutable", RAGEConst.DEFAULT_PROCESSING_RUNNER));
                    cmd.add(question.getName());
                    for (int i = 0; i < testCase.getInputs().size(); i++) {
                        cmd.add(testCase.getInputs().get(i));
                    }
                    cmd.add(">" + tempOut.getCanonicalPath());
                } else {
                    LOGGER.error("Unsupported Question Type");
                    throw new UnsupportedOperationException();
                }
                LOGGER.debug("Command: " + cmd);
                processBuilder.command(cmd);
                Process p = processBuilder.start();
                Long startTimeInNanoSec = System.nanoTime();
                Long delayInNanoSec;
                if (node.getBoolean("InfiniteLoopDetection", true)) {
                    LOGGER.debug("Infinite loop detection is enabled");
                    try {
                        delayInNanoSec = Long.parseLong(node.get("Threshold", "10")) * 1000000000;
                    } catch (NumberFormatException e) {
                        LOGGER.warn("Invalid Threshold value.  Defaulting to 10");
                        delayInNanoSec = new Long(10 * 1000000000);
                    }
                    boolean timeFlag = true;
                    while (timeFlag) {
                        try {
                            int val = p.exitValue();
                            timeFlag = false;
                        } catch (IllegalThreadStateException e) {
                            Long elapsedTime = System.nanoTime() - startTimeInNanoSec;
                            if (elapsedTime > delayInNanoSec) {
                                LOGGER.warn("Threshold time exceeded.");
                                p.destroy();
                                timeFlag = false;
                            }
                            Thread.sleep(50);
                        }
                    }
                } else {
                    LOGGER.debug("Infinite loop detection is not enabled");
                    p.waitFor();
                }
                File newTemp = null;
                BufferedReader inFile;
                try {
                    inFile = new BufferedReader(new FileReader(tempOut));
                } catch (FileNotFoundException ex) {
                    LOGGER.error("The file is in use by another process.");
                    newTemp = File.createTempFile("rage", ".tmp");
                    inFile = new BufferedReader(new FileReader(newTemp));
                }
                String line = null;
                while ((line = inFile.readLine()) != null) {
                    testCase.addOutput(line);
                }
                inFile.close();
                tempIn.delete();
                tempOut.delete();
                if (newTemp != null) {
                    newTemp.delete();
                }
            }
        } catch (IOException ex) {
            LOGGER.error("Exception: " + ex.getLocalizedMessage());
        } catch (InterruptedException ex) {
            LOGGER.warn("Thread interrupted");
        } catch (ThreadDeath td) {
            throw td;
        }
    }
