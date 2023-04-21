    public boolean runTest() throws Exception {
        VHash val1 = new VHash();
        val1.put("key1", "foo");
        val1.put("key2", "bar");
        val1.put("key3", 42);
        VHash val2 = new VHash();
        val2.put("key1", "foo");
        val2.put("key2", "bar");
        val2.put("key3", 42);
        VHash val3 = new VHash();
        val3.put("key1", "baz");
        val3.put("key2", 3.1415f);
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
