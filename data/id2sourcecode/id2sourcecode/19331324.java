    public void run() {
        try {
            ParticipantImpl participant = new ParticipantImpl(GUIDPrefixFactory.newGuidPrefix(), 0, null, null);
            EntityId topicId = new EntityId(new EntityId_t(new byte[] { 0, 1, 2 }, EntityKindEnum.USER_DEF_TOPIC));
            TopicImpl topic = (TopicImpl) participant.create_topic("test_topic", "topicId", null, null);
            participant.enable();
            new LTMemory((long) 1024 * 1024 * 10, (long) 1024 * 1024 * 10).enter(new Runnable() {

                public void run() {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                    RTPSHeader header = new RTPSHeader(GUIDPrefixFactory.newGuidPrefix());
                    EntityId readerId = new EntityId(new EntityId_t(new byte[] { 0, 1, 2 }, EntityKindEnum.USER_DEF_READER_NO_KEY));
                    EntityId writerId = new EntityId(new EntityId_t(new byte[] { 0, 1, 2 }, EntityKindEnum.USER_DEF_WRITER_NO_KEY));
                    SerializedData data = new SerializedData("helloxxx".getBytes());
                    SequenceNumber sn1 = new SequenceNumber(new SequenceNumber_t(1));
                    SequenceNumber sn2 = new SequenceNumber(new SequenceNumber_t(2));
                    Data nkd1 = new Data(readerId, writerId, sn1, null, null, null, null, data);
                    Data nkd2 = new Data(readerId, writerId, sn2, null, null, null, null, data);
                    Message msg1 = new Message(header, new Submessage[] { nkd1 });
                    Message msg2 = new Message(header, new Submessage[] { nkd2 });
                    byte[] dataPack = new byte[52];
                    CDROutputPacket packet = new CDROutputPacket(dataPack, dataPack.length, false);
                    packet.setEndianess(true);
                    try {
                        DatagramSocket socket = new DatagramSocket();
                        DatagramPacket UDPpacket = new DatagramPacket(dataPack, 44);
                        UDPpacket.setAddress(InetAddress.getByName("225.4.5.6"));
                        UDPpacket.setPort(5555);
                        msg1.write(packet);
                        socket.send(UDPpacket);
                        packet.setCursorPosition(0);
                        msg2.write(packet);
                        socket.send(UDPpacket);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            Thread.sleep(1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
