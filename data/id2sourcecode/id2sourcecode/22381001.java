    @Test
    public void testSimpleRead() throws Exception {
        RdOperationExternal read = new RdOperationExternal();
        read.query = new Template("SELECT * FROM ?subject ?predicate ?object.");
        read.kind = ReadType.READ;
        read.space = Util.toURI(SOME_SPACE);
        read.securityInfo = new SecurityInfo(CERTIFICATE_HANS);
        write(read, new RdDMEntry(), null, true);
    }
