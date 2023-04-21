    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent evt) {
        answered = false;
        String request = (String) evt.getMessage();
        String response;
        response = LocalExecDefaultResult.NoStatus.status + " " + LocalExecDefaultResult.NoStatus.result;
        boolean isLocallyShutdown = false;
        ExecuteWatchdog watchdog = null;
        try {
            if (request.length() == 0) {
                response = LocalExecDefaultResult.NoCommand.status + " " + LocalExecDefaultResult.NoCommand.result;
            } else {
                String[] args = request.split(" ");
                int cpt = 0;
                long tempDelay;
                try {
                    tempDelay = Long.parseLong(args[0]);
                    cpt++;
                } catch (NumberFormatException e) {
                    tempDelay = delay;
                }
                if (tempDelay < 0) {
                    isShutdown = true;
                    logger.warn("Shutdown order received");
                    isLocallyShutdown = isShutdown(evt.getChannel());
                    try {
                        Thread.sleep(-tempDelay);
                    } catch (InterruptedException e) {
                    }
                    Thread thread = new GGLEThreadShutdown(factory);
                    thread.start();
                    return;
                }
                String binary = args[cpt++];
                File exec = new File(binary);
                if (exec.isAbsolute()) {
                    if (!exec.canExecute()) {
                        logger.error("Exec command is not executable: " + request);
                        response = LocalExecDefaultResult.NotExecutable.status + " " + LocalExecDefaultResult.NotExecutable.result;
                        return;
                    }
                }
                CommandLine commandLine = new CommandLine(binary);
                for (; cpt < args.length; cpt++) {
                    commandLine.addArgument(args[cpt]);
                }
                DefaultExecutor defaultExecutor = new DefaultExecutor();
                ByteArrayOutputStream outputStream;
                outputStream = new ByteArrayOutputStream();
                PumpStreamHandler pumpStreamHandler = new PumpStreamHandler(outputStream);
                defaultExecutor.setStreamHandler(pumpStreamHandler);
                int[] correctValues = { 0, 1 };
                defaultExecutor.setExitValues(correctValues);
                if (tempDelay > 0) {
                    watchdog = new ExecuteWatchdog(tempDelay);
                    defaultExecutor.setWatchdog(watchdog);
                }
                int status = -1;
                try {
                    status = defaultExecutor.execute(commandLine);
                } catch (ExecuteException e) {
                    if (e.getExitValue() == -559038737) {
                        try {
                            Thread.sleep(LocalExecDefaultResult.RETRYINMS);
                        } catch (InterruptedException e1) {
                        }
                        try {
                            status = defaultExecutor.execute(commandLine);
                        } catch (ExecuteException e1) {
                            pumpStreamHandler.stop();
                            logger.error("Exception: " + e.getMessage() + " Exec in error with " + commandLine.toString());
                            response = LocalExecDefaultResult.BadExecution.status + " " + LocalExecDefaultResult.BadExecution.result;
                            try {
                                outputStream.close();
                            } catch (IOException e2) {
                            }
                            return;
                        } catch (IOException e1) {
                            pumpStreamHandler.stop();
                            logger.error("Exception: " + e.getMessage() + " Exec in error with " + commandLine.toString());
                            response = LocalExecDefaultResult.BadExecution.status + " " + LocalExecDefaultResult.BadExecution.result;
                            try {
                                outputStream.close();
                            } catch (IOException e2) {
                            }
                            return;
                        }
                    } else {
                        pumpStreamHandler.stop();
                        logger.error("Exception: " + e.getMessage() + " Exec in error with " + commandLine.toString());
                        response = LocalExecDefaultResult.BadExecution.status + " " + LocalExecDefaultResult.BadExecution.result;
                        try {
                            outputStream.close();
                        } catch (IOException e2) {
                        }
                        return;
                    }
                } catch (IOException e) {
                    pumpStreamHandler.stop();
                    logger.error("Exception: " + e.getMessage() + " Exec in error with " + commandLine.toString());
                    response = LocalExecDefaultResult.BadExecution.status + " " + LocalExecDefaultResult.BadExecution.result;
                    try {
                        outputStream.close();
                    } catch (IOException e2) {
                    }
                    return;
                }
                pumpStreamHandler.stop();
                if (defaultExecutor.isFailure(status) && watchdog != null && watchdog.killedProcess()) {
                    logger.error("Exec is in Time Out");
                    response = LocalExecDefaultResult.TimeOutExecution.status + " " + LocalExecDefaultResult.TimeOutExecution.result;
                    try {
                        outputStream.close();
                    } catch (IOException e2) {
                    }
                } else {
                    response = status + " " + outputStream.toString();
                    try {
                        outputStream.close();
                    } catch (IOException e2) {
                    }
                }
            }
        } finally {
            if (isLocallyShutdown) {
                return;
            }
            evt.getChannel().write(response + "\n");
            answered = true;
            if (watchdog != null) {
                watchdog.stop();
            }
            logger.info("End of Command: " + request + " : " + response);
            evt.getChannel().write(LocalExecDefaultResult.ENDOFCOMMAND);
        }
    }
