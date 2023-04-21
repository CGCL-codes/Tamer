    public void testEmptyTS() throws ProtocolException, IOException, ClassNotFoundException {
        System.out.println("*** testEmptyTS");
        Tuple t1 = new Tuple(new TupleSpaceVector());
        write_and_read_tuple(t1, true);
    }
