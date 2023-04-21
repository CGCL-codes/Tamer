    private void sendViaNewChannel(final DPWSContextImpl context, final DPWSContextImpl receivingContext, final OutMessage message, final String uri) throws DPWSException {
        try {
            final PipedInputStream stream = new PipedInputStream();
            final PipedOutputStream outStream = new PipedOutputStream(stream);
            final Channel channel;
            try {
                channel = getTransport().createChannel(uri);
            } catch (Exception e) {
                throw new DPWSException("Couldn't create channel.", e);
            }
            Thread writeThread = new Thread(new Runnable() {

                public void run() {
                    try {
                        final XMLSerializer ser = new XMLSerializer(outStream, message.getEncoding());
                        message.getSerializer().writeMessage(message, ser, context);
                        outStream.close();
                    } catch (Exception e) {
                        throw new DPWSRuntimeException("Couldn't write stream.", e);
                    }
                }

                ;
            });
            Thread readThread = new Thread(new Runnable() {

                public void run() {
                    try {
                        final XMLStreamReader reader = STAXUtils.createXMLStreamReader(stream, message.getEncoding(), context);
                        final InMessage inMessage = new InMessage(reader, uri);
                        inMessage.setEncoding(message.getEncoding());
                        channel.receive(receivingContext, inMessage);
                        reader.close();
                        stream.close();
                    } catch (Exception e) {
                        throw new DPWSRuntimeException("Couldn't read stream.", e);
                    }
                }

                ;
            });
            writeThread.start();
            readThread.start();
            try {
                writeThread.join();
            } catch (InterruptedException e) {
            }
        } catch (IOException e) {
            throw new DPWSRuntimeException("Couldn't create stream.", e);
        }
    }
