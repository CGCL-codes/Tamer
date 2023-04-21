    private void write_and_read_tuple(Tuple tuple, boolean expectedEqual) throws IOException, ProtocolException, ClassNotFoundException {
        System.out.println("Write tuple: " + tuple.toString());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        tupleState.setDoRead(false);
        tupleState.setTuple(tuple);
        tupleState.enter(null, new TransmissionChannel(new IMCMarshaler(out)));
        ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
        TupleState tupleState2 = new TupleState();
        tupleState2.setDoRead(true);
        tupleState2.enter(null, new TransmissionChannel(new IMCUnMarshaler(in)));
        Tuple read = tupleState2.getTuple();
        System.out.println("Read tuple: " + read.toString());
        assertEquals(tuple.getTupleId(), read.getTupleId());
        assertEquals(tuple, read);
    }
