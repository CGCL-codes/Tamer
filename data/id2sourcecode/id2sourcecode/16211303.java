    public ShortAddrVariableValue(String name, String comment, String cvName, boolean readOnly, boolean infoOnly, boolean writeOnly, boolean opsOnly, int cvNum, String mask, Vector<CvValue> v, JLabel status, String stdname) {
        super(name, comment, cvName, readOnly, infoOnly, writeOnly, opsOnly, cvNum, mask, 1, 127, v, status, stdname);
        firstFreeSpace = 0;
        setModifiedCV(19);
        setModifiedCV(29);
    }
