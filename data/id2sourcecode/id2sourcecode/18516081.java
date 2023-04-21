    public boolean runTest() throws Exception {
        VString val1 = new VString("foo");
        VString val2 = new VString("foo");
        VString val3 = new VString("bar");
        if (!val1.equalsSameType(val2)) {
            setErrorMessage("Failed foo==foo");
            return false;
        }
        if (val1.equalsSameType(val3)) {
            setErrorMessage("Failed foo!=bar");
            return false;
        }
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        DataOutputStream dbuf = new DataOutputStream(buf);
        val1.writeToStream(dbuf);
        DataInputStream bufin = new DataInputStream(new ByteArrayInputStream(buf.toByteArray()));
        Value val4 = Value.readFromStream(bufin);
        if (!val1.equalsSameType(val4)) {
            setErrorMessage("Failed foo==foo after read and write");
            return false;
        }
        if (new VString("256").toInteger() != 256) {
            setErrorMessage("Failed integer conversion");
            return false;
        }
        if (new VString("256").toLong() != 256l) {
            setErrorMessage("Failed long conversion");
            return false;
        }
        if (new VString("3.1415").toFloat() != 3.1415f) {
            setErrorMessage("Failed float conversion");
            return false;
        }
        if (new VString("3.1415").toDouble() != 3.1415d) {
            setErrorMessage("Failed double conversion");
            return false;
        }
        if (new VString("false").toBoolean() != false) {
            setErrorMessage("Failed boolean conversion");
            return false;
        }
        if (new VString("true").toBoolean() != true) {
            setErrorMessage("Failed boolean conversion");
            return false;
        }
        if (!new VString("foobarbaz").toString().equals("foobarbaz")) {
            setErrorMessage("Failed string conversion");
            return false;
        }
        return true;
    }
