    public String toString() {
        String scEvSt = "";
        String value = ((getWritable() == AttrWriteType._READ || getWritable() == AttrWriteType._READ_WITH_WRITE || getWritable() == AttrWriteType._READ_WRITE) ? "read value :  " + valueToString(0) : "") + ((getWritable() == AttrWriteType._WRITE || getWritable() == AttrWriteType._READ_WITH_WRITE || getWritable() == AttrWriteType._READ_WRITE) ? "\t " + "write value : " + valueToString(1) : "");
        scEvSt = "Source : \t" + getAttribute_complete_name() + "\r\n" + "TimeSt : \t" + getTimeStamp() + "\r\n" + "Value  : \t" + value + "\r\n";
        return scEvSt;
    }
