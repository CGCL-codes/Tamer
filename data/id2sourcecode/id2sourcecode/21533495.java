    public void handleStepError(Exception exception) throws XAwareException {
        if (exception instanceof XAwareEarlyTerminationException) {
            if (exception instanceof XAwareExitException) {
                handleExit((XAwareExitException) exception);
                return;
            }
            if (exception instanceof XAwareResponseException) {
                handleResponse((XAwareResponseException) exception);
                return;
            }
            finishNode(true);
            throw (XAwareEarlyTerminationException) exception;
        }
        XAwareException xawareException = updateCurrentError(exception);
        IScriptNode currentNode = finishNode(false);
        if (currentNode == null) {
            throw xawareException;
        }
        ITransactionContext parentTxContext = null;
        if (!callStack.isEmpty()) {
            parentTxContext = peek().getTransactionContext();
        }
        boolean errorHandled = currentNode.handleError(xawareException, parentTxContext);
        if (!errorHandled) {
            try {
                if (currentNode.startsNewChannelScope()) {
                    currentNode.getChannelScope().complete(false);
                }
            } catch (RuntimeException e) {
                String errMsg = "Failed to close channel: " + e;
                logger.severe(errMsg, CLASS_NAME, "handleStepError");
                throw new XAwareException(errMsg, xawareException);
            } finally {
                try {
                    if (currentNode.startsNewOutputStream()) {
                        currentNode.getOutputStreamer().closeStream();
                    }
                } catch (RuntimeException e) {
                    String errMsg = "Failed to close OutputStream: " + e;
                    logger.severe(errMsg, CLASS_NAME, "handleStepError");
                    throw new XAwareException(errMsg, xawareException);
                }
            }
            throw xawareException;
        }
        push(currentNode);
    }
