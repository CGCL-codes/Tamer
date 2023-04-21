    public boolean runTest() throws Exception {
        VList val1 = new VList();
        val1.add("foo");
        val1.add("bar");
        val1.add(42);
        VList val2 = new VList();
        val2.add("foo");
        val2.add("bar");
        val2.add(42);
        VList val3 = new VList();
        val3.add("baz");
        val3.add(3.1415f);
        if (!val1.equalsSameType(val2)) {
            setErrorMessage("Failed 1==1");
            return false;
        }
        if (val1.equalsSameType(val3)) {
            setErrorMessage("Failed 1!=2");
            return false;
        }
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        DataOutputStream dbuf = new DataOutputStream(buf);
        val1.writeToStream(dbuf);
        DataInputStream bufin = new DataInputStream(new ByteArrayInputStream(buf.toByteArray()));
        Value val4 = Value.readFromStream(bufin);
        if (!val1.equalsSameType(val4)) {
            setErrorMessage("Failed 1==1 after read and write");
            return false;
        }
        return true;
    }
