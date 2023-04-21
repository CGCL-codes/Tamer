    protected void setSteerField(boolean isSetToSTX) {
        if (isSetToSTX) {
            Channel stXChan = theDoc.correctorH.getChannel(MagnetMainSupply.FIELD_SET_HANDLE);
            try {
                stXChan.putVal(stXFieldVal);
            } catch (ConnectionException e) {
                e.printStackTrace();
            } catch (PutException e) {
                e.printStackTrace();
            }
        } else {
            Channel stYChan = theDoc.correctorV.getChannel(MagnetMainSupply.FIELD_SET_HANDLE);
            try {
                stYChan.putVal(stYFieldVal);
            } catch (ConnectionException e) {
                e.printStackTrace();
            } catch (PutException e) {
                e.printStackTrace();
            }
        }
    }
