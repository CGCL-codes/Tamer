    @Override
    public void run() {
        while (true) {
            if (this.keepReplaying && !this.paused) {
                try {
                    long nextMessageTime = this.dataInput.readLong();
                    long nextDelay = nextMessageTime - this.replayIndex;
                    if (nextDelay > 0) try {
                        Thread.sleep(nextDelay);
                    } catch (InterruptedException ie) {
                    }
                    this.replayIndex = nextMessageTime;
                } catch (EOFException eofe) {
                    this.closeConnection();
                    continue;
                } catch (IOException ioe) {
                    System.err.println(ioe.getLocalizedMessage());
                    ioe.printStackTrace(System.err);
                    System.exit(1);
                }
                int message_length = -1;
                try {
                    message_length = this.dataInput.readInt();
                } catch (IOException ioe) {
                    System.err.println(ioe.getLocalizedMessage());
                    this.stopRunning();
                    continue;
                }
                if (message_length < 1) {
                    this.stopRunning();
                    continue;
                }
                byte type = (byte) EventManager.MESSAGE_TYPE_UNKNOWN;
                try {
                    type = this.dataInput.readByte();
                } catch (IOException ioe) {
                    this.stopRunning();
                    continue;
                }
                final byte[] message = new byte[message_length - 1];
                int k = 0, read = 0;
                while (read < message.length) {
                    try {
                        k = this.dataInput.read(message, read, message.length - read);
                        if (k == -1) {
                            this.stopRunning();
                            continue;
                        }
                        if (k == 0) {
                            System.err.println("No data read...");
                            continue;
                        }
                        read += k;
                    } catch (SocketTimeoutException ste) {
                        continue;
                    } catch (IOException ioe) {
                        this.stopRunning();
                        break;
                    }
                }
                if (read < message.length) {
                    this.stopRunning();
                    continue;
                }
                this.receiveBPS.updateUnits(message.length + 5);
                this.bytesRead += (message.length + 13);
                switch(type) {
                    case EventManager.MESSAGE_TYPE_CONSOLE:
                        try {
                            this.fireServerEvent(new CommandResponseEvent(this, new String(message, "ASCII")));
                        } catch (UnsupportedEncodingException uee) {
                            this.stopRunning();
                            continue;
                        }
                        break;
                    case EventManager.MESSAGE_TYPE_LOCATION:
                        if (!this.handleLocation(message)) {
                            this.stopRunning();
                            continue;
                        }
                        break;
                    case EventManager.MESSAGE_TYPE_FINGERPRINT_MEAN_GZIP:
                        if (!this.handleFingerprint(message, EventManager.MESSAGE_TYPE_FINGERPRINT_MEAN_GZIP)) {
                            this.stopRunning();
                            continue;
                        }
                        break;
                    case EventManager.MESSAGE_TYPE_FINGERPRINT_STDEV_GZIP:
                        if (!this.handleFingerprint(message, EventManager.MESSAGE_TYPE_FINGERPRINT_STDEV_GZIP)) {
                            this.stopRunning();
                            continue;
                        }
                        break;
                    case EventManager.MESSAGE_TYPE_XML_GZIP:
                        ByteArrayOutputStream uncompressed = new ByteArrayOutputStream();
                        try {
                            GZIPInputStream unzipStream = new GZIPInputStream(new ByteArrayInputStream(message));
                            byte[] buffer = new byte[1024];
                            int readGZ = 0;
                            while ((readGZ = unzipStream.read(buffer, 0, buffer.length)) > 0) {
                                uncompressed.write(buffer, 0, readGZ);
                            }
                        } catch (IOException ioe) {
                            System.err.println("Couldn't decompress XML.");
                            break;
                        }
                        if (uncompressed.size() == 0) {
                            System.err.println("No data decompressed.");
                            break;
                        }
                        try {
                            this.handleXML(new String(uncompressed.toByteArray(), "ASCII"));
                        } catch (UnsupportedEncodingException uee) {
                            this.stopRunning();
                            continue;
                        }
                        break;
                    case EventManager.MESSAGE_TYPE_XML:
                        try {
                            this.handleXML(new String(message, "ASCII"));
                        } catch (UnsupportedEncodingException uee) {
                            this.stopRunning();
                            continue;
                        }
                        break;
                    case EventManager.MESSAGE_TYPE_STATISTICS_GZIP:
                        ByteArrayOutputStream uncompressedStat = new ByteArrayOutputStream();
                        try {
                            GZIPInputStream unzipStream = new GZIPInputStream(new ByteArrayInputStream(message));
                            byte[] buffer = new byte[1024];
                            int readGZ = 0;
                            while ((readGZ = unzipStream.read(buffer, 0, buffer.length)) > 0) {
                                uncompressedStat.write(buffer, 0, readGZ);
                            }
                        } catch (IOException ioe) {
                            System.err.println("Couldn't decompress XML.");
                            break;
                        }
                        if (uncompressedStat.size() == 0) {
                            System.err.println("No data decompressed.");
                            break;
                        }
                        if (!this.handleStatistics(uncompressedStat.toByteArray())) {
                            this.stopRunning();
                            continue;
                        }
                        break;
                    case EventManager.MESSAGE_TYPE_HUB_CONNECT:
                        if (!this.handleHubConnection(message)) {
                            this.stopRunning();
                            continue;
                        }
                        break;
                    default:
                        this.stopRunning();
                        continue;
                }
            } else {
                if (!this.paused) this.closeConnection();
                try {
                    synchronized (this) {
                        this.wait();
                    }
                } catch (InterruptedException ie) {
                }
            }
        }
    }
