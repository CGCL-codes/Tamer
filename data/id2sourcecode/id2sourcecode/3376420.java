    @Override
    public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e) {
        logger.debug("Local Server Channel Closed: {} {}", (localChannelReference != null ? localChannelReference : "no LocalChannelReference"), (session.getRunner() != null ? session.getRunner().toShortString() : "no runner"));
        DbTaskRunner runner = session.getRunner();
        boolean mustFinalize = true;
        if (localChannelReference != null && localChannelReference.getFutureRequest().isDone()) {
        } else {
            if (localChannelReference != null) {
                R66Future fvr = localChannelReference.getFutureValidRequest();
                try {
                    fvr.await();
                } catch (InterruptedException e1) {
                }
                if (fvr.isDone()) {
                    if (!fvr.isSuccess()) {
                        if (fvr.getResult().code == ErrorCode.ServerOverloaded) {
                            mustFinalize = false;
                        }
                    }
                }
                logger.debug("Must Finalize: " + mustFinalize);
                if (mustFinalize) {
                    session.newState(ERROR);
                    R66Result finalValue = new R66Result(new OpenR66ProtocolSystemException("Finalize too early at close time"), session, true, ErrorCode.FinalOp, runner);
                    try {
                        tryFinalizeRequest(finalValue);
                    } catch (OpenR66Exception e2) {
                    }
                }
            }
        }
        if (mustFinalize && runner != null) {
            if (runner.isSelfRequested()) {
                R66Future transfer = localChannelReference.getFutureRequest();
                R66Result result = transfer.getResult();
                if (transfer.isDone() && transfer.isSuccess()) {
                    logger.info("TRANSFER REQUESTED RESULT:\n    SUCCESS\n    " + (result != null ? result.toString() : "no result"));
                } else {
                    logger.error("TRANSFER REQUESTED RESULT:\n    FAILURE\n    " + (result != null ? result.toString() : "no result"));
                }
            }
        }
        session.setStatus(50);
        session.newState(CLOSEDCHANNEL);
        session.clear();
        session.setStatus(51);
        if (localChannelReference != null) {
            if (localChannelReference.getDbSession() != null) {
                localChannelReference.getDbSession().endUseConnection();
                logger.debug("End Use Connection");
            }
            NetworkTransaction.removeNetworkChannel(localChannelReference.getNetworkChannel(), e.getChannel());
            if (runner != null && runner.isSelfRequested() && localChannelReference.getNetworkChannelObject() != null && localChannelReference.getNetworkChannelObject().count <= 0) {
                NetworkTransaction.removeClient(runner.getRequester(), localChannelReference.getNetworkChannelObject());
            }
            session.setStatus(52);
            Configuration.configuration.getLocalTransaction().remove(e.getChannel());
        } else {
            logger.error("Local Server Channel Closed but no LocalChannelReference: " + e.getChannel().getId());
        }
        if (mustFinalize && localChannelReference != null && (!localChannelReference.getFutureRequest().isDone())) {
            R66Result finalValue = new R66Result(new OpenR66ProtocolSystemException("Finalize too early at close time"), session, true, ErrorCode.FinalOp, runner);
            localChannelReference.invalidateRequest(finalValue);
            ClientRunner clientRunner = localChannelReference.getClientRunner();
            if (clientRunner != null) {
                try {
                    Thread.sleep(Configuration.WAITFORNETOP);
                } catch (InterruptedException e1) {
                }
                clientRunner.interrupt();
            }
        }
    }
