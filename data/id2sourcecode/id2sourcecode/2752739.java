    public void testTupleWriteRead() throws ProtocolException, IOException, ClassNotFoundException {
        Tuple t1 = new Tuple(Integer.class, new KString("hello"), new KString());
        Tuple t2 = new Tuple(new KInteger(10), new KString("foo"), new Boolean(false));
        Tuple t3 = new Tuple(t1, t2);
        Tuple t4 = new Tuple(Tuple.class, t1);
        write_and_read_tuple(t1, true);
        write_and_read_tuple(t2, true);
        write_and_read_tuple(t3, true);
        write_and_read_tuple(t4, true);
        TupleSpaceVector tupleSpace = new TupleSpaceVector();
        tupleSpace.add(t1);
        tupleSpace.add(t2);
        tupleSpace.add(t3);
        tupleSpace.add(t4);
        Tuple complex = new Tuple(tupleSpace);
        write_and_read_tuple(complex, true);
        Tuple wretrieved = new Tuple(new String("foo"));
        wretrieved.setHandleRetrieved(true);
        wretrieved.addRetrieved(t1.getTupleId());
        wretrieved.addRetrieved(t2.getTupleId());
        write_and_read_tuple(wretrieved, true);
    }
