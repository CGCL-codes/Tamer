    public int getChannelDatatype(String chName) {
        Debugger.debug(Debugger.TRACE, "============chanName=" + chName);
        for (int i = 0; i < dataIdVec.size(); i++) {
            Debugger.debug(Debugger.TRACE, "chanName=" + chName + " SmdName=" + dataIdVec.elementAt(i));
            if (dataIdVec.elementAt(i).equals(chName)) {
                return dataIdDatatypeVec.elementAt(i).intValue();
            }
        }
        return -1;
    }
