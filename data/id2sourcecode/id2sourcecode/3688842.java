    public void run() {
        try {
            request = new Connectionstate_Request(connected_Server.getChannelid(), socket.getLocalPort());
            packet = new DatagramPacket(request.getByteArray(), request.getByteArray().length, connected_Server.getControlEndpoint());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        while (!stop) {
            try {
                received = false;
                synchronized (this) {
                    try {
                        wait(EIBNETIP_Constants.HEARTBEAT_REQUEST_TIMEOUT * 1000);
                    } catch (InterruptedException ex) {
                        received = true;
                    }
                }
                int nr = 0;
                long time = 0;
                while (received == false && nr < 3) {
                    socket.send(packet);
                    synchronized (this) {
                        try {
                            time = System.currentTimeMillis();
                            wait(EIBNETIP_Constants.CONNECTIONSTATE_REQUEST_TIMEOUT * 1000);
                        } catch (InterruptedException ex) {
                        }
                        if (System.currentTimeMillis() - time <= EIBNETIP_Constants.CONNECTIONSTATE_REQUEST_TIMEOUT * 1000) received = true;
                    }
                    nr++;
                }
                if (nr == 3) {
                    tunnel.disconnect("host unreachable");
                    stop = true;
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
