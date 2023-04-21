    public void run() {
        while (!socket.isClosed()) {
            try {
                endpoint.run();
                Channel channel = endpoint.accept();
                if (channel != null) addChannel(channel);
                synchronized (clientHandles) {
                    for (Iterator itor = clientHandles.iterator(); itor.hasNext(); ) {
                        try {
                            MultiplexedClientHandle handle = (MultiplexedClientHandle) itor.next();
                            if (handle.writeBuffer != null && handle.writeBuffer.length > 0) {
                                int written = handle.channel.write(handle.writeBuffer, 0, handle.writeBuffer.length);
                                if (written > 0) {
                                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                    baos.write(handle.writeBuffer, written, handle.writeBuffer.length - written);
                                    handle.writeBuffer = baos.toByteArray();
                                }
                            }
                            int read = handle.channel.read(temp, 0, temp.length);
                            if (read > 0) {
                                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                if (handle.readBuffer != null && handle.readBuffer.length > 0) baos.write(handle.readBuffer);
                                baos.write(temp, 0, read);
                                handle.readBuffer = baos.toByteArray();
                                if (handle.currentLength == -1) {
                                    if (handle.readBuffer.length >= 4) {
                                        ByteArrayInputStream bais = new ByteArrayInputStream(handle.readBuffer);
                                        DataInputStream dis = new DataInputStream(bais);
                                        handle.currentLength = dis.readInt();
                                        if (handle.readBuffer.length == 4) {
                                            handle.readBuffer = null;
                                        } else {
                                            baos = new ByteArrayOutputStream();
                                            baos.write(handle.readBuffer, 4, handle.readBuffer.length - 4);
                                            handle.readBuffer = baos.toByteArray();
                                        }
                                    }
                                }
                                if (handle.currentLength != -1) {
                                    if (handle.readBuffer.length >= handle.currentLength) {
                                        ByteArrayInputStream bais = new ByteArrayInputStream(handle.readBuffer, 0, handle.currentLength);
                                        SerialInputStream sis = new SerialInputStream(bais);
                                        Object o = sis.readSerialObject();
                                        this.fireReceivedEvent(handle, (SerialObject) o);
                                        if (handle.readBuffer.length == handle.currentLength) {
                                            handle.readBuffer = null;
                                        } else {
                                            baos = new ByteArrayOutputStream();
                                            baos.write(handle.readBuffer, handle.currentLength, handle.readBuffer.length - handle.currentLength);
                                            handle.readBuffer = baos.toByteArray();
                                        }
                                        handle.currentLength = -1;
                                    }
                                }
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
