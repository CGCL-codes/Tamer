    public String toString() {
        StringBuffer sb = ToStringHelper.init(this);
        ToStringHelper.append("ReadFrom", readFrom, sb);
        ToStringHelper.append("ReadTo", readTo, sb);
        ToStringHelper.append("Length", readTo - readFrom + 1, sb);
        ToStringHelper.append("WriteFrom", writeOffset, sb);
        ToStringHelper.append("WriteTo", writeOffset + readTo - readFrom, sb);
        return ToStringHelper.close(sb);
    }
