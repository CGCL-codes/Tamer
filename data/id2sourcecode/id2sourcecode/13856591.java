        public void lifetimeQuery() throws UtilityException, MessageAttributeException, MessageHeaderParsingException, MessageAttributeParsingException, IOException {
            try {
                DatagramSocket socket = new DatagramSocket();
                socket.connect(InetAddress.getByName(stunServer), port);
                socket.setSoTimeout(timeout);
                MessageHeader sendMH = new MessageHeader(MessageHeader.MessageHeaderType.BindingRequest);
                sendMH.generateTransactionID();
                ChangeRequest changeRequest = new ChangeRequest();
                ResponseAddress responseAddress = new ResponseAddress();
                responseAddress.setAddress(ma.getAddress());
                responseAddress.setPort(ma.getPort());
                sendMH.addMessageAttribute(changeRequest);
                sendMH.addMessageAttribute(responseAddress);
                byte[] data = sendMH.getBytes();
                DatagramPacket send = new DatagramPacket(data, data.length, InetAddress.getByName(stunServer), port);
                socket.send(send);
                LOGGER.debug("Binding Request sent.");
                MessageHeader receiveMH = new MessageHeader();
                while (!(receiveMH.equalTransactionID(sendMH))) {
                    DatagramPacket receive = new DatagramPacket(new byte[200], 200);
                    initialSocket.receive(receive);
                    receiveMH = MessageHeader.parseHeader(receive.getData());
                    receiveMH.parseAttributes(receive.getData());
                }
                ErrorCode ec = (ErrorCode) receiveMH.getMessageAttribute(MessageAttribute.MessageAttributeType.ErrorCode);
                if (ec != null) {
                    LOGGER.debug("Message header contains errorcode message attribute.");
                    return;
                }
                LOGGER.debug("Binding Response received.");
                if (upperBinarySearchLifetime == (lowerBinarySearchLifetime + 1)) {
                    LOGGER.debug("BindingLifetimeTest completed. UDP binding lifetime: " + binarySearchLifetime + ".");
                    completed = true;
                    return;
                }
                lifetime = binarySearchLifetime;
                LOGGER.debug("Lifetime update: " + lifetime + ".");
                lowerBinarySearchLifetime = binarySearchLifetime;
                binarySearchLifetime = (upperBinarySearchLifetime + lowerBinarySearchLifetime) / 2;
                if (binarySearchLifetime > 0) {
                    BindingLifetimeTask task = new BindingLifetimeTask();
                    timer.schedule(task, binarySearchLifetime);
                    LOGGER.debug("Timer scheduled: " + binarySearchLifetime + ".");
                } else {
                    completed = true;
                }
            } catch (SocketTimeoutException ste) {
                LOGGER.debug("Read operation at query socket timeout.");
                if (upperBinarySearchLifetime == (lowerBinarySearchLifetime + 1)) {
                    LOGGER.debug("BindingLifetimeTest completed. UDP binding lifetime: " + binarySearchLifetime + ".");
                    completed = true;
                    return;
                }
                upperBinarySearchLifetime = binarySearchLifetime;
                binarySearchLifetime = (upperBinarySearchLifetime + lowerBinarySearchLifetime) / 2;
                if (binarySearchLifetime > 0) {
                    if (bindingCommunicationInitialSocket()) {
                        return;
                    }
                    BindingLifetimeTask task = new BindingLifetimeTask();
                    timer.schedule(task, binarySearchLifetime);
                    LOGGER.debug("Timer scheduled: " + binarySearchLifetime + ".");
                } else {
                    completed = true;
                }
            }
        }
