    protected void getInit(SessionThread objThread, String strDevGrp, String strDevAddr) throws Exception {
        DeviceGroupInterface objDgi = objBus.getGroup(strDevGrp);
        String strOutBuffer = Integer.toString(objBus.getId()) + " " + Declare.strDevDescription + " " + strDevGrp + " ";
        strOutBuffer += objDgi.info(strDevAddr);
        objThread.writeAck(Declare.intInfoMin, strOutBuffer);
    }
