    private void write_and_read_tuple_packet(TuplePacket tuplePacket) throws IOException, ProtocolException, ClassNotFoundException {
        System.out.println("Write tuple packet:\n" + tuplePacket.toString());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        TupleOpState tupleOpState = new TupleOpState();
        tupleOpState.setDoRead(false);
        tupleOpState.setTuplePacket(tuplePacket);
        tupleOpState.enter(null, new TransmissionChannel(new IMCMarshaler(out)));
        ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
        TupleOpState tupleOpState2 = new TupleOpState();
        tupleOpState2.setDoRead(true);
        tupleOpState2.enter(null, new TransmissionChannel(new IMCUnMarshaler(in)));
        TuplePacket read = tupleOpState2.getTuplePacket();
        System.out.println("Read tuple packet:\n" + read.toString());
        assertEquals(tuplePacket, read);
    }
