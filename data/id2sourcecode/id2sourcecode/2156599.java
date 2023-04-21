    protected void processSysOpsChanges() throws InterruptedException {
        SelectionKey key = null;
        SysOpsChange sysOpsChange = null;
        while (!sysOpsChangeQueue.isEmpty()) {
            int size = sysOpsChangeQueue.size();
            int loop = size > container.getQueueSize() ? container.getQueueSize() : size;
            for (int i = 0; i < loop; i++) {
                sysOpsChange = (SysOpsChange) sysOpsChangeQueue.take();
                if (sysOpsChange.getChannel() instanceof SocketChannel) {
                    key = ((SocketChannel) sysOpsChange.getChannel()).keyFor(selector);
                    container.sendEvent(new ConnectionManagerEvent(EventCatagory.TRACE, SystemEventType.GENERAL, "processSysOpsChanges() - interestOps(" + sysOpsChange.getInterestOps() + ") socketChannel(" + ((Channel) sysOpsChange.getChannel()).toString() + ")", container.getName()));
                } else if (sysOpsChange.getChannel() instanceof DatagramChannel) {
                    key = ((DatagramChannel) sysOpsChange.getChannel()).keyFor(selector);
                } else {
                    container.sendEvent(new ConnectionManagerEvent(EventCatagory.WARN, SystemEventType.GENERAL, "processSysOpsChanges()- channel not socket or datagram.", container.getName()));
                }
                if (key.isValid()) {
                    key.interestOps(key.interestOps() | sysOpsChange.getInterestOps());
                } else {
                    container.sendEvent(new ConnectionManagerEvent(EventCatagory.TRACE, SystemEventType.GENERAL, "processSysOpsChanges().keyIsInvalid - interestOps(" + sysOpsChange.getInterestOps() + ") socketChannel(" + ((Channel) sysOpsChange.getChannel()).toString() + ")", container.getName()));
                }
            }
        }
    }
