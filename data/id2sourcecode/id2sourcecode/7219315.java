    public boolean runTest() throws Exception {
        VLong val1 = new VLong(1);
        VLong val2 = new VLong(1);
        VLong val3 = new VLong(2);
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
        if (new VLong(256l).toInteger() != 256) {
            setErrorMessage("Failed integer conversion");
            return false;
        }
        if (new VLong(256l).toLong() != 256l) {
            setErrorMessage("Failed long conversion");
            return false;
        }
        if (!new VLong(256l).toString().equals("256")) {
            setErrorMessage("Failed string conversion");
            return false;
        }
        return true;
    }
