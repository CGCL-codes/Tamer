    protected void dataRequest(final DHTTransportUDPContactImpl originator, final DHTUDPPacketData req) {
        stats.dataReceived();
        byte packet_type = req.getPacketType();
        if (packet_type == DHTUDPPacketData.PT_READ_REPLY) {
            transferQueue queue = lookupTransferQueue(read_transfers, req.getConnectionId());
            if (queue != null) {
                queue.add(req);
            }
        } else if (packet_type == DHTUDPPacketData.PT_WRITE_REPLY) {
            transferQueue queue = lookupTransferQueue(write_transfers, req.getConnectionId());
            if (queue != null) {
                queue.add(req);
            }
        } else {
            byte[] transfer_key = req.getTransferKey();
            if (packet_type == DHTUDPPacketData.PT_READ_REQUEST) {
                try {
                    handleTransferRequest(originator, req.getConnectionId(), transfer_key, req.getRequestKey(), null, req.getStartPosition(), req.getLength(), false, false);
                } catch (DHTTransportException e) {
                    logger.log(e);
                }
            } else {
                transferQueue old_queue = lookupTransferQueue(read_transfers, req.getConnectionId());
                if (old_queue != null) {
                    old_queue.add(req);
                } else {
                    final DHTTransportTransferHandler handler = (DHTTransportTransferHandler) transfer_handlers.get(new HashWrapper(transfer_key));
                    if (handler == null) {
                        logger.log("No transfer handler registered for key '" + ByteFormatter.encodeString(transfer_key) + "'");
                    } else {
                        try {
                            final transferQueue new_queue = new transferQueue(read_transfers, req.getConnectionId());
                            new_queue.add(req);
                            new AEThread("DHTTransportUDP:writeQueueProcessor", true) {

                                public void runSupport() {
                                    try {
                                        byte[] write_data = runTransferQueue(new_queue, new DHTTransportProgressListener() {

                                            public void reportSize(long size) {
                                                if (XFER_TRACE) {
                                                    System.out.println("writeXfer: size=" + size);
                                                }
                                            }

                                            public void reportActivity(String str) {
                                                if (XFER_TRACE) {
                                                    System.out.println("writeXfer: act=" + str);
                                                }
                                            }

                                            public void reportCompleteness(int percent) {
                                                if (XFER_TRACE) {
                                                    System.out.println("writeXfer: %=" + percent);
                                                }
                                            }
                                        }, originator, req.getTransferKey(), req.getRequestKey(), 60000, false);
                                        if (write_data != null) {
                                            if (req.getStartPosition() != 0 || req.getLength() != req.getTotalLength()) {
                                                sendWriteReply(req.getConnectionId(), originator, req.getTransferKey(), req.getRequestKey(), 0, req.getTotalLength());
                                            }
                                            byte[] reply_data = handler.handleWrite(originator, req.getRequestKey(), write_data);
                                            if (reply_data != null) {
                                                writeTransfer(new DHTTransportProgressListener() {

                                                    public void reportSize(long size) {
                                                        if (XFER_TRACE) {
                                                            System.out.println("writeXferReply: size=" + size);
                                                        }
                                                    }

                                                    public void reportActivity(String str) {
                                                        if (XFER_TRACE) {
                                                            System.out.println("writeXferReply: act=" + str);
                                                        }
                                                    }

                                                    public void reportCompleteness(int percent) {
                                                        if (XFER_TRACE) {
                                                            System.out.println("writeXferReply: %=" + percent);
                                                        }
                                                    }
                                                }, originator, req.getTransferKey(), req.getRequestKey(), reply_data, WRITE_REPLY_TIMEOUT);
                                            }
                                        }
                                    } catch (DHTTransportException e) {
                                        logger.log("Failed to process transfer queue: " + Debug.getNestedExceptionMessage(e));
                                    }
                                }
                            }.start();
                            sendWriteReply(req.getConnectionId(), originator, req.getTransferKey(), req.getRequestKey(), req.getStartPosition(), req.getLength());
                        } catch (DHTTransportException e) {
                            logger.log("Faild to create transfer queue");
                            logger.log(e);
                        }
                    }
                }
            }
        }
    }
