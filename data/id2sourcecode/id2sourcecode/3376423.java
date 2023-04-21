    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
        if (localChannelReference != null && localChannelReference.getFutureRequest().isDone()) {
            return;
        }
        OpenR66Exception exception = OpenR66ExceptionTrappedFactory.getExceptionFromTrappedException(e.getChannel(), e);
        ErrorCode code = null;
        if (exception != null) {
            session.newState(ERROR);
            boolean isAnswered = false;
            if (exception instanceof OpenR66ProtocolShutdownException) {
                logger.warn("Shutdown order received and going from: " + session.getAuth().getUser());
                if (localChannelReference != null) {
                    R66Result finalValue = new R66Result(exception, session, true, ErrorCode.Shutdown, session.getRunner());
                    try {
                        tryFinalizeRequest(finalValue);
                    } catch (OpenR66RunnerErrorException e2) {
                    } catch (OpenR66ProtocolSystemException e2) {
                    }
                    if (!localChannelReference.getFutureRequest().isDone()) {
                        try {
                            session.setFinalizeTransfer(false, finalValue);
                        } catch (OpenR66RunnerErrorException e1) {
                            localChannelReference.invalidateRequest(finalValue);
                        } catch (OpenR66ProtocolSystemException e1) {
                            localChannelReference.invalidateRequest(finalValue);
                        }
                    }
                }
                Thread thread = new Thread(new ChannelUtils(), "R66 Shutdown Thread");
                thread.setDaemon(true);
                thread.start();
                session.setStatus(54);
                return;
            } else {
                if (localChannelReference != null && localChannelReference.getFutureRequest() != null) {
                    if (localChannelReference.getFutureRequest().isDone()) {
                        R66Result result = localChannelReference.getFutureRequest().getResult();
                        if (result != null) {
                            isAnswered = result.isAnswered;
                        }
                    }
                }
                if (exception instanceof OpenR66ProtocolNoConnectionException) {
                    code = ErrorCode.ConnectionImpossible;
                    DbTaskRunner runner = session.getRunner();
                    if (runner != null) {
                        runner.stopOrCancelRunner(code);
                    }
                } else if (exception instanceof OpenR66ProtocolBusinessCancelException) {
                    code = ErrorCode.CanceledTransfer;
                    DbTaskRunner runner = session.getRunner();
                    if (runner != null) {
                        runner.stopOrCancelRunner(code);
                    }
                } else if (exception instanceof OpenR66ProtocolBusinessStopException) {
                    code = ErrorCode.StoppedTransfer;
                    DbTaskRunner runner = session.getRunner();
                    if (runner != null) {
                        runner.stopOrCancelRunner(code);
                    }
                } else if (exception instanceof OpenR66ProtocolBusinessQueryAlreadyFinishedException) {
                    code = ErrorCode.QueryAlreadyFinished;
                    try {
                        tryFinalizeRequest(new R66Result(session, true, code, session.getRunner()));
                        return;
                    } catch (OpenR66RunnerErrorException e1) {
                    } catch (OpenR66ProtocolSystemException e1) {
                    }
                } else if (exception instanceof OpenR66ProtocolBusinessQueryStillRunningException) {
                    code = ErrorCode.QueryStillRunning;
                    logger.error("Will close channel since ", exception);
                    Channels.close(e.getChannel());
                    session.setStatus(56);
                    return;
                } else if (exception instanceof OpenR66ProtocolBusinessRemoteFileNotFoundException) {
                    code = ErrorCode.FileNotFound;
                } else if (exception instanceof OpenR66RunnerException) {
                    code = ErrorCode.ExternalOp;
                } else if (exception instanceof OpenR66ProtocolNotAuthenticatedException) {
                    code = ErrorCode.BadAuthent;
                } else if (exception instanceof OpenR66ProtocolNetworkException) {
                    code = ErrorCode.Disconnection;
                    DbTaskRunner runner = session.getRunner();
                    if (runner != null) {
                        R66Result finalValue = new R66Result(new OpenR66ProtocolSystemException("Finalize too early at close time"), session, true, code, session.getRunner());
                        try {
                            tryFinalizeRequest(finalValue);
                        } catch (OpenR66Exception e2) {
                        }
                    }
                } else if (exception instanceof OpenR66ProtocolRemoteShutdownException) {
                    code = ErrorCode.RemoteShutdown;
                    DbTaskRunner runner = session.getRunner();
                    if (runner != null) {
                        runner.stopOrCancelRunner(code);
                    }
                } else {
                    DbTaskRunner runner = session.getRunner();
                    if (runner != null) {
                        switch(runner.getErrorInfo()) {
                            case InitOk:
                            case PostProcessingOk:
                            case PreProcessingOk:
                            case Running:
                            case TransferOk:
                                code = ErrorCode.Internal;
                            default:
                                code = runner.getErrorInfo();
                        }
                    } else {
                        code = ErrorCode.Internal;
                    }
                }
                if ((!isAnswered) && (!(exception instanceof OpenR66ProtocolBusinessNoWriteBackException)) && (!(exception instanceof OpenR66ProtocolNoConnectionException))) {
                    if (code == null || code == ErrorCode.Internal) {
                        code = ErrorCode.RemoteError;
                    }
                    final ErrorPacket errorPacket = new ErrorPacket(exception.getMessage(), code.getCode(), ErrorPacket.FORWARDCLOSECODE);
                    try {
                        ChannelUtils.writeAbstractLocalPacket(localChannelReference, errorPacket).awaitUninterruptibly();
                    } catch (OpenR66ProtocolPacketException e1) {
                    }
                }
                R66Result finalValue = new R66Result(exception, session, true, code, session.getRunner());
                try {
                    session.setFinalizeTransfer(false, finalValue);
                    if (localChannelReference != null) localChannelReference.invalidateRequest(finalValue);
                } catch (OpenR66RunnerErrorException e1) {
                    if (localChannelReference != null) localChannelReference.invalidateRequest(finalValue);
                } catch (OpenR66ProtocolSystemException e1) {
                    if (localChannelReference != null) localChannelReference.invalidateRequest(finalValue);
                }
            }
            if (exception instanceof OpenR66ProtocolBusinessNoWriteBackException) {
                logger.error("Will close channel {}", exception.getMessage());
                Channels.close(e.getChannel());
                session.setStatus(56);
                return;
            } else if (exception instanceof OpenR66ProtocolNoConnectionException) {
                logger.error("Will close channel {}", exception.getMessage());
                Channels.close(e.getChannel());
                session.setStatus(57);
                return;
            }
            ChannelUtils.close(e.getChannel());
            session.setStatus(58);
        } else {
            session.setStatus(59);
            return;
        }
    }
