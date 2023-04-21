    protected void getBus(SessionThread objThread) throws Exception {
        String strOutBuffer = "";
        String strIdBuffer = "";
        int intCount = objBus.getGroupCount();
        int intIndex;
        strOutBuffer = Integer.toString(objBus.getId()) + " " + Declare.strDevDescription + " ";
        for (intIndex = 0; intIndex < intCount; intIndex++) {
            strIdBuffer = objBus.getGroup(intIndex).getIdentifier();
            if (!strIdBuffer.equals(Declare.strDevDescription)) {
                strOutBuffer += strIdBuffer + " ";
            }
        }
        objThread.writeAck(Declare.intInfoMin, strOutBuffer);
    }
