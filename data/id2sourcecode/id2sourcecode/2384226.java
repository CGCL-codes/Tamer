    @Override
    public void run() {
        Logger.getLogger("fslogger").info("UDP Sender started for file " + filename);
        FileChannel fc;
        MappedByteBuffer bb;
        try {
            fc = new FileInputStream(FileServer.getInstance().getRoot() + filename).getChannel();
            bb = fc.map(MapMode.READ_ONLY, 0, filesize);
            lastTrunk = 0;
            DatagramPacket d = new DatagramPacket(new byte[0], 0, InetAddress.getByName(multicastAddress), port);
            while (true) {
                byte[] dataArray = new byte[UDP_DATA_LENGTH];
                int dataLength;
                ByteBuffer dataBuf = ByteBuffer.allocate(UDP_DATA_LENGTH + 8);
                dataBuf.putLong(nextTrunkToSend);
                if (bb.remaining() < UDP_DATA_LENGTH) {
                    dataLength = bb.remaining() + 8;
                    d.setLength(dataLength);
                    bb.get(dataArray, 0, bb.remaining());
                    bb.clear();
                    nextTrunkToSend = 0;
                } else {
                    bb.get(dataArray, 0, UDP_DATA_LENGTH);
                    nextTrunkToSend++;
                }
                dataBuf.put(dataArray);
                d.setData(dataBuf.array());
                ms.send(d);
                synchronized (UdpManager.getInstance()) {
                    if (nextTrunkToSend == lastTrunk) {
                        UdpManager.getInstance().removeTransmission(filename);
                        bb.clear();
                        fc.close();
                        Logger.getLogger("fslogger").info("UDP Sender finished for file " + filename);
                        break;
                    }
                }
            }
        } catch (FileNotFoundException e) {
            Logger.getLogger("fslogger").warning(filename + "was not found for client : " + e.getCause());
        } catch (IOException e) {
            Logger.getLogger("fslogger").warning("error during mapping or sending file " + filename + " : " + e.getCause());
        }
    }
