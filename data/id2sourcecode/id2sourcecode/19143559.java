    private void testReadSyncData(String uri, Object[] dataBuff, long bufferingPeriod, boolean isTimestampIncluded, boolean isUncertaintyIncluded, boolean isValidityIncluded) throws IOException {
        SensorConnection conn = (SensorConnection) Connector.open(uri);
        assertTrue(conn.getState() == SensorConnection.STATE_OPENED);
        TestChannelDevice channelDevice = (TestChannelDevice) (((Sensor) conn).getChannelDevice(0));
        assertTrue(channelDevice != null);
        channelDevice.setTestData(dataBuff);
        Data[] arrData = conn.getData(dataBuff.length, bufferingPeriod, isTimestampIncluded, isUncertaintyIncluded, isValidityIncluded);
        assertTrue(arrData != null);
        assertTrue(arrData.length > 0);
        double[] doubleResults = null;
        int[] intResults = null;
        Object[] objResults = null;
        int resultsLength = 0;
        int dataType = conn.getSensorInfo().getChannelInfos()[0].getDataType();
        switch(dataType) {
            case ChannelInfo.TYPE_INT:
                intResults = arrData[0].getIntValues();
                assertTrue(intResults != null);
                resultsLength = intResults.length;
                break;
            case ChannelInfo.TYPE_DOUBLE:
                doubleResults = arrData[0].getDoubleValues();
                assertTrue(doubleResults != null);
                resultsLength = doubleResults.length;
                break;
            case ChannelInfo.TYPE_OBJECT:
                objResults = arrData[0].getObjectValues();
                assertTrue(objResults != null);
                resultsLength = objResults.length;
                break;
        }
        assertTrue(resultsLength == dataBuff.length);
        for (int i = 0; i < resultsLength; i++) {
            switch(dataType) {
                case ChannelInfo.TYPE_INT:
                    assertTrue(intResults[i] == ((Integer) dataBuff[i]).intValue());
                    break;
                case ChannelInfo.TYPE_DOUBLE:
                    assertTrue(doubleResults[i] == ((Double) dataBuff[i]).doubleValue());
                    break;
                case ChannelInfo.TYPE_OBJECT:
                    break;
            }
            if (isTimestampIncluded) {
                try {
                    arrData[0].getTimestamp(i);
                    assertTrue(true);
                } catch (Throwable ex) {
                    fail("Unexpected exception " + ex);
                }
            } else {
                try {
                    arrData[0].getTimestamp(i);
                    fail("No IllegalStateException is throwed");
                } catch (IllegalStateException ex) {
                    assertTrue(true);
                } catch (Throwable ex) {
                    fail("Unexpected exception " + ex);
                }
            }
            if (isUncertaintyIncluded) {
                try {
                    arrData[0].getUncertainty(i);
                    assertTrue(true);
                } catch (Throwable ex) {
                    fail("Unexpected exception " + ex);
                }
            } else {
                try {
                    arrData[0].getUncertainty(i);
                    fail("No IllegalStateException is throwed");
                } catch (IllegalStateException ex) {
                    assertTrue(true);
                } catch (Throwable ex) {
                    fail("Unexpected exception " + ex);
                }
            }
            if (isValidityIncluded) {
                try {
                    arrData[0].isValid(i);
                    assertTrue(true);
                } catch (Throwable ex) {
                    fail("Unexpected exception " + ex);
                }
            } else {
                try {
                    arrData[0].isValid(i);
                    fail("No IllegalStateException is throwed");
                } catch (IllegalStateException ex) {
                    assertTrue(true);
                } catch (Throwable ex) {
                    fail("Unexpected exception " + ex);
                }
            }
        }
        conn.close();
    }
