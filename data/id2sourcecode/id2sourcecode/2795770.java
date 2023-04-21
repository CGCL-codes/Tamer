    public int doProcessing(EventWaiter waiter, int max_bytes) {
        int num_bytes_allowed = main_handler.getCurrentNumBytesAllowed();
        if (num_bytes_allowed < 1) {
            return 0;
        }
        if (max_bytes > 0 && max_bytes < num_bytes_allowed) {
            num_bytes_allowed = max_bytes;
        }
        if (pending_actions != null) {
            try {
                connections_mon.enter();
                for (int i = 0; i < pending_actions.size(); i++) {
                    Object[] entry = (Object[]) pending_actions.get(i);
                    NetworkConnectionBase connection = (NetworkConnectionBase) entry[1];
                    if (entry[0] == ADD_ACTION) {
                        active_connections.add(connection);
                    } else {
                        active_connections.remove(connection);
                        idle_connections.remove(connection);
                    }
                }
                pending_actions = null;
            } finally {
                connections_mon.exit();
            }
        }
        long now = SystemTime.getSteppedMonotonousTime();
        if (now - last_idle_check > MOVE_TO_IDLE_TIME) {
            last_idle_check = now;
            connectionEntry entry = idle_connections.head();
            while (entry != null) {
                NetworkConnectionBase connection = entry.connection;
                connectionEntry next = entry.next;
                if (connection.getTransportBase().isReadyForRead(waiter) == 0) {
                    idle_connections.remove(entry);
                    active_connections.addToStart(entry);
                }
                entry = next;
            }
        }
        int num_bytes_remaining = num_bytes_allowed;
        connectionEntry entry = active_connections.head();
        int num_entries = active_connections.size();
        for (int i = 0; i < num_entries && entry != null && num_bytes_remaining > 0; i++) {
            NetworkConnectionBase connection = entry.connection;
            connectionEntry next = entry.next;
            long ready = connection.getTransportBase().isReadyForRead(waiter);
            if (ready == 0) {
                int mss = connection.getMssSize();
                int allowed = num_bytes_remaining > mss ? mss : num_bytes_remaining;
                int bytes_read = 0;
                try {
                    bytes_read = connection.getIncomingMessageQueue().receiveFromTransport(allowed);
                } catch (Throwable e) {
                    if (AEDiagnostics.TRACE_CONNECTION_DROPS) {
                        if (e.getMessage() == null) {
                            Debug.out("null read exception message: ", e);
                        } else {
                            if (e.getMessage().indexOf("end of stream on socket read") == -1 && e.getMessage().indexOf("An existing connection was forcibly closed by the remote host") == -1 && e.getMessage().indexOf("Connection reset by peer") == -1 && e.getMessage().indexOf("An established connection was aborted by the software in your host machine") == -1) {
                                System.out.println("MP: read exception [" + connection.getTransportBase().getDescription() + "]: " + e.getMessage());
                            }
                        }
                    }
                    if (!(e instanceof IOException)) {
                        Debug.printStackTrace(e);
                    }
                    connection.notifyOfException(e);
                }
                num_bytes_remaining -= bytes_read;
                active_connections.moveToEnd(entry);
            } else if (ready > MOVE_TO_IDLE_TIME) {
                active_connections.remove(entry);
                idle_connections.addToEnd(entry);
            }
            entry = next;
        }
        int total_bytes_read = num_bytes_allowed - num_bytes_remaining;
        if (total_bytes_read > 0) {
            main_handler.bytesProcessed(total_bytes_read);
            return total_bytes_read;
        }
        return 0;
    }
