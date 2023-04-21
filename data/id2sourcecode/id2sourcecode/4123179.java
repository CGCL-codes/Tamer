    public String toString() {
        String snapStr = "";
        String value = ((writable == AttrWriteType._READ || writable == AttrWriteType._READ_WITH_WRITE || writable == AttrWriteType._READ_WRITE) ? "read value :  " + valueToString(0) : "") + ((writable == AttrWriteType._WRITE || writable == AttrWriteType._READ_WITH_WRITE || writable == AttrWriteType._READ_WRITE) ? "\t " + "write value : " + valueToString(1) : "");
        snapStr = "attribute ID   : \t" + getId_att() + "\r\n" + "attribute Name : \t" + getAttribute_complete_name() + "\r\n" + "attribute value : \t" + value + "\r\n";
        return snapStr;
    }
