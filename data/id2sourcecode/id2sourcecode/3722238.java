    public void run() {
        try {
            eh = new EventHeap(eh, null);
            lastE = null;
            retVal = null;
            Event callEvent = new Event();
            callEvent.setFieldValue(Event.TIMETOLIVE, new Integer(opn_expire));
            callEvent.setFieldValue(Event.EVENTTYPE, svcName);
            callEvent.addField(ICrafterConstants.EVENTCLASS, ICrafterConstants.OPERATION_EVENT_CLASS);
            callEvent.addField(ICrafterConstants.HAS_RETURN, new Boolean(hasReturn));
            boolean dataparams = false;
            if (opName == null) {
                Utils.warning("EHCallObject", "OpName is null!");
                return;
            }
            int callID = (int) (Math.random() * Integer.MAX_VALUE);
            callEvent.addField(ICrafterConstants.OPERATION_NAME, opName);
            callEvent.addField(ICrafterConstants.CALLID, new Integer(callID));
            if (params != null) {
                for (int i = 0; i < params.length; i++) {
                    Class[] ifs = params[i].getClass().getInterfaces();
                    boolean isrdata = false;
                    for (int j = 0; j < ifs.length; j++) {
                        if (ifs[j].getName().equals("iwork.icrafter.system.RData")) isrdata = true;
                    }
                    if (isrdata) {
                        Utils.debug("EHCallObject", i + ": RData parameter");
                        callEvent.addField("RData" + i, new Boolean(true));
                        callEvent.addField(ICrafterConstants.PARAM + i, "");
                        dataparams = true;
                        continue;
                    }
                    callEvent.addField(ICrafterConstants.PARAM + i, params[i]);
                }
            }
            Utils.debug("EHCallObject", "Putting callevent: " + callEvent.toString());
            eh.putEvent(callEvent);
            if (dataparams) {
                Event retEventTemplate = new Event();
                retEventTemplate.setFieldValue(Event.EVENTTYPE, svcName);
                retEventTemplate.addField(ICrafterConstants.EVENTCLASS, "DataReturnClass");
                retEventTemplate.addField(ICrafterConstants.CALLID, new Integer(callID));
                Utils.debug("EHCallObject", "Waiting for data ports ... ");
                Event rcdEvent = eh.waitToRemoveEvent(retEventTemplate);
                for (int i = 0; i < params.length; i++) {
                    Class[] ifs = params[i].getClass().getInterfaces();
                    boolean isrdata = false;
                    for (int j = 0; j < ifs.length; j++) {
                        if (ifs[j].getName().equals("iwork.icrafter.system.RData")) isrdata = true;
                    }
                    if (!isrdata) continue;
                    String rDataRet = (String) rcdEvent.getPostValue("RDataRet" + i);
                    int sep = rDataRet.indexOf(';');
                    String machineName = rDataRet.substring(0, sep);
                    int portNum = (new Integer(rDataRet.substring(sep + 1))).intValue();
                    Socket s = new Socket(machineName, portNum);
                    InputStream is = ((RData) params[i]).getDataStream();
                    OutputStream os = s.getOutputStream();
                    byte[] b = new byte[1000];
                    int len = -1;
                    while ((len = is.read(b)) != -1) os.write(b, 0, len);
                    is.close();
                    os.close();
                }
            }
            if (!hasReturn) return;
            Event retEventTemplate = new Event();
            retEventTemplate.setFieldValue(Event.EVENTTYPE, svcName);
            retEventTemplate.addField(ICrafterConstants.EVENTCLASS, ICrafterConstants.RETURN_EVENT_CLASS);
            retEventTemplate.addField(ICrafterConstants.CALLID, new Integer(callID));
            Utils.debug("EHCallObject", "Waiting for return... ");
            Event rcdEvent = eh.waitToRemoveEvent(retEventTemplate);
            String retCode = (String) rcdEvent.getPostValue(ICrafterConstants.RETURN_CODE);
            if (retCode.equals(ICrafterConstants.NO_ERROR_RETURN)) {
                Utils.debug("EHCallObject", "Return event " + "received! " + rcdEvent);
                retVal = (Serializable) rcdEvent.getPostValue(ICrafterConstants.RETURN_VALUE);
                return;
            } else if (retCode.equals(ICrafterConstants.NULL_RETURN)) {
                retVal = null;
                return;
            } else if (retCode.equals(ICrafterConstants.ERROR_RETURN)) {
                String err = (String) rcdEvent.getPostValue(ICrafterConstants.RETURN_ERROR_MESSAGE);
                throw new ICrafterRemoteException(err);
            } else if (retCode.equals(ICrafterConstants.RDATA_RETURN)) {
                String rDataRet = (String) rcdEvent.getPostValue(ICrafterConstants.RETURN_VALUE);
                int sep = rDataRet.indexOf(';');
                String machineName = rDataRet.substring(0, sep);
                int portNum = (new Integer(rDataRet.substring(sep + 1))).intValue();
                Socket s = new Socket(machineName, portNum);
                InputStream is = s.getInputStream();
                StreamData tmp = new StreamData(is);
                retVal = new ByteData(tmp.getDataBytes());
                s.close();
                return;
            } else {
                throw new ICrafterRemoteException("Unknown error code in return event");
            }
        } catch (Exception e) {
            e.printStackTrace();
            lastE = e;
        }
    }
