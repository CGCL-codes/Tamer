    public IScriptNode finishNode(boolean success) throws XAwareException {
        if (callStack.isEmpty()) {
            return null;
        }
        IScriptNode finishedNode = callStack.pop();
        if (logger.isDebugEnabled()) {
            logger.debug(dumpStack("pop"), CLASS_NAME, "finishNode");
        }
        try {
            finishedNode.complete(success);
        } finally {
            try {
                if (finishedNode.startsNewChannelScope() && success) {
                    finishedNode.getChannelScope().complete(success);
                }
            } catch (RuntimeException e) {
                String errMsg = "Failed to close channel: " + e;
                logger.severe(errMsg, CLASS_NAME, "finishNode");
                throw new XAwareException(errMsg, e);
            } finally {
                try {
                    if (finishedNode.startsNewOutputStream()) {
                        finishedNode.getOutputStreamer().closeStream();
                    }
                } catch (RuntimeException e) {
                    String errMsg = "Failed to close OutputStream: " + e;
                    logger.severe(errMsg, CLASS_NAME, "finishNode");
                    throw new XAwareException(errMsg, e);
                } finally {
                    try {
                        if (finishedNode.getContext().getScriptRoot() == finishedNode.getElement()) {
                            finishedNode.getContext().endPassProcessing();
                        }
                    } finally {
                        if (finishedNode.startsNewTransactionScope()) {
                            ITransactionContext transactionContext = finishedNode.getTransactionContext();
                            boolean processingComplete = this.processingComplete();
                            if (!processingComplete) {
                                logger.finer("completing transaction for " + transactionContext.getTransactionName() + " with success = " + success, CLASS_NAME, "finishNode");
                            }
                            transactionContext.complete(success, processingComplete);
                        }
                    }
                }
            }
        }
        return finishedNode;
    }
