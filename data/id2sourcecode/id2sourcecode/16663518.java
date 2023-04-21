    public boolean runTest() throws Exception {
        VFloat val1 = new VFloat(1.1234f);
        VFloat val2 = new VFloat(1.1234f);
        VFloat val3 = new VFloat(2.2345f);
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
        if (new VFloat(1.1234f).toInteger() != 1) {
            setErrorMessage("Failed integer conversion");
            return false;
        }
        if (new VFloat(1.1234f).toLong() != 1l) {
            setErrorMessage("Failed long conversion");
            return false;
        }
        if (!new VFloat(1.1234f).toString().equals("1.1234")) {
            setErrorMessage("Failed string conversion");
            return false;
        }
        return true;
    }
