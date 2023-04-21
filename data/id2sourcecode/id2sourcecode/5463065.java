    public long findOtherSpotAddress() {
        long receivedAddress = 0;
        final String REQUEST_MESSAGE = "BW_Request";
        final String RESPONSE_MESSAGE = "BW_Response";
        IRadioPolicyManager radio = Spot.getInstance().getRadioPolicyManager();
        long myAddress = radio.getIEEEAddress();
        System.out.println("My address is " + Long.toString(myAddress) + ". I will send it to the other SPOT");
        System.out.println("Using channgel# " + radio.getChannelNumber());
        System.out.println("Using PanID " + radio.getPanId());
        System.out.println("Receiver is on: " + radio.isRadioReceiverOn());
        while (true) {
            System.out.println("Trying to find and communicate with other SPOT");
            if (myMode.equals(ModeOfOperation.ACTIVE)) {
                RadiogramConnection bcConn = null;
                RadiogramConnection unicastConn = null;
                try {
                    System.out.println("Starting active side");
                    System.out.println("Sending the request datagram");
                    bcConn = (RadiogramConnection) Connector.open("radiogram://broadcast:" + discoveryPort);
                    bcConn.setTimeout(5000);
                    Radiogram bcDatagram = (Radiogram) bcConn.newDatagram(bcConn.getMaximumLength());
                    bcDatagram.writeUTF(REQUEST_MESSAGE);
                    bcDatagram.writeLong(myAddress);
                    bcConn.send(bcDatagram);
                    System.out.println("Receiving the response");
                    unicastConn = (RadiogramConnection) Connector.open("radiogram://:" + discoveryPort);
                    unicastConn.setTimeout(5000);
                    Radiogram inDatagram = (Radiogram) unicastConn.newDatagram(unicastConn.getMaximumLength());
                    inDatagram.reset();
                    unicastConn.receive(inDatagram);
                    String answer = inDatagram.readUTF();
                    if (!answer.equals(RESPONSE_MESSAGE)) {
                        throw new IOException("Unexpected response received");
                    }
                    receivedAddress = inDatagram.readLong();
                    System.out.println("Done active side");
                    break;
                } catch (IOException e) {
                    System.out.println("An I/O Exception occured, retrying");
                } finally {
                    if (bcConn != null) {
                        try {
                            bcConn.close();
                        } catch (IOException e) {
                            System.out.println("Could not close bcConn");
                            e.printStackTrace();
                        }
                    }
                    if (unicastConn != null) {
                        try {
                            unicastConn.close();
                        } catch (IOException e) {
                            System.out.println("Could not close unicastConn");
                            e.printStackTrace();
                        }
                    }
                }
            } else {
                RadiogramConnection unicastConn = null;
                try {
                    System.out.println("Starting passive side");
                    System.out.println("Waiting for request");
                    unicastConn = (RadiogramConnection) Connector.open("radiogram://:" + discoveryPort);
                    unicastConn.setTimeout(5000);
                    Radiogram inDatagram = (Radiogram) unicastConn.newDatagram(unicastConn.getMaximumLength());
                    inDatagram.reset();
                    unicastConn.receive(inDatagram);
                    String question = inDatagram.readUTF();
                    if (!question.equals(REQUEST_MESSAGE)) {
                        throw new IOException("An unsupported request received");
                    }
                    receivedAddress = inDatagram.readLong();
                    System.out.println("Received good request from " + Long.toString(receivedAddress));
                    System.out.println("Sending the response to " + Long.toString(receivedAddress));
                    Radiogram outDatagram = (Radiogram) unicastConn.newDatagram(unicastConn.getMaximumLength());
                    outDatagram.reset();
                    outDatagram.setAddress(inDatagram);
                    outDatagram.writeUTF(RESPONSE_MESSAGE);
                    outDatagram.writeLong(myAddress);
                    unicastConn.send(outDatagram);
                    System.out.println("Done passive side");
                    break;
                } catch (IOException e) {
                    System.out.println("An I/O Exception occured. Retrying...");
                } finally {
                    if (unicastConn != null) {
                        try {
                            unicastConn.close();
                        } catch (IOException e) {
                            System.out.println("Could not close unicastConn");
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        return receivedAddress;
    }
