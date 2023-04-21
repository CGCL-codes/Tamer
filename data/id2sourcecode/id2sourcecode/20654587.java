    @Override
    public String toString() {
        String scEvSt = "";
        final String value = (getWritable() == AttrWriteType._READ || getWritable() == AttrWriteType._READ_WITH_WRITE || getWritable() == AttrWriteType._READ_WRITE ? "read value :  " + valueToString(0) : "") + (getWritable() == AttrWriteType._WRITE || getWritable() == AttrWriteType._READ_WITH_WRITE || getWritable() == AttrWriteType._READ_WRITE ? "\t " + "write value : " + valueToString(1) : "");
        scEvSt = getAttribute_complete_name() + "[timestamp: " + DATE_FORMAT.format(new Date(getTimeStamp())) + " - value: " + value + "]";
        return scEvSt;
    }
