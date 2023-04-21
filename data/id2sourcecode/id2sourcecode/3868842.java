    @Override
    public void run() {
        Protocol protocol = new Protocol(feederManager.nameIdPair, Protocol.VERSION, feederManager.getEnvImpl());
        try {
            configureChannel();
            protocol = checkProtocol(protocol);
            checkFeeder(protocol);
            sendFileList(protocol);
            sendRequestedFiles(protocol);
            dbBackup.endBackup();
            dbBackup = null;
        } catch (ClosedByInterruptException e) {
            LoggerUtils.fine(logger, feederManager.getEnvImpl(), "Ignoring ClosedByInterruptException normal shutdown");
        } catch (IOException e) {
            LoggerUtils.warning(logger, feederManager.getEnvImpl(), " IO Exception: " + e.getMessage());
        } catch (ProtocolException e) {
            LoggerUtils.severe(logger, feederManager.getEnvImpl(), " Protocol Exception: " + e.getMessage());
        } catch (Exception e) {
            throw new EnvironmentFailureException(feederManager.getEnvImpl(), EnvironmentFailureReason.UNCAUGHT_EXCEPTION, e);
        } finally {
            try {
                namedChannel.getChannel().close();
            } catch (IOException e) {
                LoggerUtils.warning(logger, feederManager.getEnvImpl(), "Log File feeder io exception on " + "channel close: " + e.getMessage());
            }
            shutdown();
            if (dbBackup != null) {
                if (feederManager.shutdown.get()) {
                    dbBackup.endBackup();
                } else {
                    feederManager.new Lease(clientId, feederManager.leaseDuration, dbBackup);
                    LoggerUtils.info(logger, feederManager.getEnvImpl(), "Lease created for node: " + clientId);
                }
            }
            LoggerUtils.info(logger, feederManager.getEnvImpl(), "Log file feeder for client: " + clientId + " exited");
        }
    }
