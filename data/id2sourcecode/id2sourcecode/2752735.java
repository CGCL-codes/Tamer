    public void testString() throws ProtocolException, IOException, ClassNotFoundException {
        System.out.println("*** testString");
        Tuple t1 = new Tuple("foo\n\n");
        write_and_read_tuple(t1, true);
        write_and_read_tuple(new Tuple(t1 + " bar"), true);
    }
