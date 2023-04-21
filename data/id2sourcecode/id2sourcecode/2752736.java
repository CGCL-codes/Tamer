    public void testEmptyTuple() throws ProtocolException, IOException, ClassNotFoundException {
        System.out.println("*** testEmpty");
        Tuple t1 = new Tuple();
        write_and_read_tuple(t1, true);
    }
