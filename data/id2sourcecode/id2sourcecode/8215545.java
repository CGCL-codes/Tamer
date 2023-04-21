    @Override
    public Object sendMessage(JCsyncAbstractOperation op, boolean blocking) throws Exception {
        try {
            this.request_response_locker.writeLock().lock();
            if (getAssignedTopic(op.getObjectID()) == null) {
                throw ObjectNotExistsException.instance();
            }
            AbstractConsistencyManager acm = getConsistencyManager(op.getObjectID());
            if (acm == null) {
                acm = this.dcManager;
            }
            short type = op.getOperationType();
            if ((type & OP_REQ_GENERIC) == OP_REQ_GENERIC) {
                acm.beforeRequestSend(op, blocking);
                this.pubsubLayer.getCustomAlgorith().registerSharedObjectName(op.getObjectID());
                Transaction t = this.pubsubLayer.getCustomAlgorith().nextPublishTransaction(op.getObjectID(), op.getOperationType());
                op.setReqestID(t.getID());
                log.trace("(sendMessage) - operation sent (blocking=[" + blocking + "]) operation: " + op.toString());
                this.messagesToSend.put(new MessageToSend(op, t));
                this.request_response_locker.writeLock().unlock();
                Object result = acm.afterRequestSend(op, blocking);
                return result;
            } else if ((type & OP_INDICATION_GENERIC) == OP_INDICATION_GENERIC) {
                acm.beforeRequestSend(op, blocking);
                this.pubsubLayer.getCustomAlgorith().registerSharedObjectName(op.getObjectID());
                Transaction t = this.pubsubLayer.getCustomAlgorith().nextPublishTransaction(op.getObjectID(), op.getOperationType());
                op.setReqestID(t.getID());
                log.trace("(sendMessage) - operation sent (blocking=[" + blocking + "]) operation: " + op.toString());
                this.messagesToSend.put(new MessageToSend(op, t));
                this.request_response_locker.writeLock().unlock();
                Object result = acm.afterRequestSend(op, blocking);
                return result;
            } else {
                log.error("Unhandled operation type: " + op.toString());
            }
        } catch (Exception e) {
            throw e;
        } finally {
            if (this.request_response_locker.writeLock().getHoldCount() > 0 && this.request_response_locker.writeLock().isHeldByCurrentThread()) {
                this.request_response_locker.writeLock().unlock();
            }
        }
        return null;
    }
