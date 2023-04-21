    @Override
    public void run() {
        try {
            ByteBuffer buffer = ByteBuffer.allocate(1024 * 10);
            File[] studentFiles;
            if (type == RAGEConst.RAPTOR_QUESTION) {
                studentFiles = RageLib.getRapFiles(file);
            } else if (type == RAGEConst.PROCESSING_QUESTION) {
                studentFiles = RageLib.getJarFiles(file);
            } else {
                studentFiles = null;
            }
            LOGGER.info("Found " + studentFiles.length + " files to grade in " + "directory " + file.getCanonicalPath());
            boolean exclusionsAvailable = false;
            LOGGER.debug("Looping through " + gevent.getQuestions().size() + " Questions in GradedEvent");
            for (Question question : gevent.getQuestions()) {
                LOGGER.debug("Question: " + question.getName());
                Result questionResult = new Result(question, gevent.getPartialCredit());
                LOGGER.debug("Looping through Student Files");
                for (File f : studentFiles) {
                    LOGGER.debug("Student File: " + f.getName());
                    String questionName = RageLib.getQuestionNameFromFilename(f.getName());
                    LOGGER.debug("Question Name from Student File: " + questionName);
                    if (questionName.equalsIgnoreCase(question.getName())) {
                        LOGGER.debug("Grading Test Cases");
                        for (int i = 0; i < question.getTestCases().size(); i++) {
                            LOGGER.debug("Running Test Case " + i);
                            Response response = new Response();
                            response.setResult(true);
                            TestCase testCase = question.getTestCases().get(i);
                            if (testCase.getExcludes().size() > 0) {
                                exclusionsAvailable = true;
                            }
                            File tempIn = File.createTempFile("rage", ".tmp");
                            File tempOut = File.createTempFile("rage", ".tmp", f.getParentFile());
                            LOGGER.debug("Temp Input File: " + tempIn.getCanonicalPath());
                            LOGGER.debug("Temp Output File: " + tempOut.getCanonicalPath());
                            List<String> cmd = new ArrayList<String>();
                            if (type == RAGEConst.RAPTOR_QUESTION) {
                                LOGGER.debug("Writing test inputs to temp input file");
                                FileOutputStream inFile = new FileOutputStream(tempIn);
                                FileChannel inChannel = inFile.getChannel();
                                inChannel.lock();
                                for (int j = 0; j < testCase.getInputs().size(); j++) {
                                    buffer.put(testCase.getInputs().get(j).getBytes());
                                    buffer.put(System.getProperty("line.separator").getBytes());
                                    buffer.flip();
                                    inChannel.write(buffer);
                                    buffer.clear();
                                }
                                inFile.close();
                                inChannel.close();
                                LOGGER.debug("Building RAPTOR command");
                                cmd.add(node.get("RaptorExecutable", RAGEConst.DEFAULT_RAPTOR_EXECUTABLE));
                                cmd.add("\"" + f.getCanonicalPath() + "\"");
                                cmd.add("/run");
                                cmd.add("\"" + tempIn.getCanonicalPath() + "\"");
                                cmd.add("\"" + tempOut.getCanonicalPath() + "\"");
                            } else if (type == RAGEConst.PROCESSING_QUESTION) {
                                launcher.directory(f.getParentFile());
                                cmd.add(node.get("RunmeExecutable", "/usr/local/bin/runme"));
                                cmd.add(question.getName());
                                for (int j = 0; j < testCase.getInputs().size(); j++) {
                                    cmd.add(testCase.getInputs().get(j));
                                }
                                cmd.add(">" + tempOut.getName());
                            } else {
                                LOGGER.error("ERROR:  Unsupported Option");
                            }
                            launcher.command(cmd);
                            String callCommand = new String();
                            for (int j = 0; j < cmd.size(); j++) {
                                callCommand = callCommand.concat(cmd.get(j) + " ");
                            }
                            LOGGER.debug("Command: " + callCommand);
                            Process p = launcher.start();
                            Long startTimeInNanoSec = System.nanoTime();
                            Long delayInNanoSec;
                            if (node.getBoolean("InfiniteLoopDetection", true)) {
                                try {
                                    delayInNanoSec = Long.parseLong(node.get("Threshold", "10")) * 1000000000;
                                } catch (NumberFormatException e) {
                                    LOGGER.error("ERROR: Invalid Threshold " + "value.  Defaulting to 10");
                                    delayInNanoSec = new Long(10 * 1000000000);
                                }
                                boolean timeFlag = true;
                                while (timeFlag) {
                                    try {
                                        int val = p.exitValue();
                                        timeFlag = false;
                                        LOGGER.debug("Exit Value: " + val);
                                    } catch (IllegalThreadStateException e) {
                                        Long elapsedTime = System.nanoTime() - startTimeInNanoSec;
                                        if (elapsedTime > delayInNanoSec) {
                                            LOGGER.warn("ERROR: Threshold time " + "exceeded.");
                                            p.destroy();
                                            timeFlag = false;
                                        }
                                        Thread.sleep(50);
                                    }
                                }
                            } else {
                                p.waitFor();
                            }
                            File newTemp = null;
                            BufferedReader inFile;
                            LOGGER.debug("Read the results from the user and store");
                            try {
                                inFile = new BufferedReader(new FileReader(tempOut));
                                LOGGER.debug("Output File: " + tempOut.getCanonicalPath());
                            } catch (FileNotFoundException ex) {
                                LOGGER.warn("ERROR: The file is in use by another " + "process.");
                                newTemp = File.createTempFile("rage", ".tmp");
                                LOGGER.debug("New Temp: " + newTemp.getCanonicalPath());
                                inFile = new BufferedReader(new FileReader(newTemp));
                            }
                            String line = null;
                            while ((line = inFile.readLine()) != null) {
                                LOGGER.debug("Response: " + line);
                                response.addAnswer(line);
                            }
                            LOGGER.debug("Responses Received: " + response.getAnswers().size());
                            inFile.close();
                            response.setResult(RageLib.gradeTestCase(testCase, response, question.getVerbatim(), exclusionsAvailable));
                            if (response.getResult()) {
                                response.setPointsEarned(testCase.getValue());
                                LOGGER.debug("Points awarded: " + response.getPointsEarned());
                            }
                            LOGGER.debug("Adding " + testCase.getValue().toPlainString() + " points to Question");
                            LOGGER.debug("Deleting temp files");
                            tempIn.delete();
                            tempOut.delete();
                            if (newTemp != null) {
                                newTemp.delete();
                            }
                            LOGGER.debug("Adding Response to Question Result");
                            if (questionResult == null) {
                                LOGGER.error("questionResult is null");
                            } else if (response == null) {
                                LOGGER.error("response is null");
                            } else {
                                LOGGER.debug("questionResult and response are " + "both not null");
                            }
                            questionResult.addResponse(response);
                            LOGGER.debug("Response Added");
                        }
                    }
                }
                questionResult.calculateScore();
                LOGGER.info("Points earned for Question (" + questionResult.getQuestion().getName() + "): " + questionResult.getScore());
                report.addResult(questionResult);
            }
        } catch (IOException ex) {
            LOGGER.error("IO Error: " + ex.getLocalizedMessage());
        } catch (InterruptedException ex) {
            LOGGER.warn("Thread interrupted");
        } catch (Exception ex) {
            LOGGER.error("Unknown Exception: " + ex.getLocalizedMessage());
        } catch (ThreadDeath td) {
            throw td;
        }
    }
