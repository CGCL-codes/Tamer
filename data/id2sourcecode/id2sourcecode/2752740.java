    public void testTuplePacketWriteRead() throws ProtocolException, IOException, ClassNotFoundException, KlavaMalformedPhyLocalityException {
        Tuple t1 = new Tuple(Integer.class, new KString("hello"), new KString());
        Tuple t2 = new Tuple(new KInteger(10), new KString("foo"), new Boolean(false));
        Tuple t3 = new Tuple(t1, t2);
        PhysicalLocality from = new PhysicalLocality("localhost", 9999);
        PhysicalLocality to = new PhysicalLocality("localhost", 11000);
        TuplePacket tuplePacket = new TuplePacket(from, to, TuplePacket.OUT_S, t1);
        TuplePacket tuplePacket2 = new TuplePacket(from, to, TuplePacket.OUT_S, t3);
        tuplePacket2.processName = "foo";
        write_and_read_tuple_packet(tuplePacket);
        write_and_read_tuple_packet(tuplePacket2);
    }
