    public void activateAccelerometer() {
        try {
            final javax.microedition.sensor.SensorInfo[] si = javax.microedition.sensor.SensorManager.findSensors("acceleration", javax.microedition.sensor.SensorInfo.CONTEXT_TYPE_USER);
            final String url = si[0].getUrl();
            this.con = Connector.open(url);
            final javax.microedition.sensor.SensorConnection sensor = (javax.microedition.sensor.SensorConnection) this.con;
            sensor.setDataListener(new javax.microedition.sensor.DataListener() {

                public void dataReceived(final javax.microedition.sensor.SensorConnection sensor, final javax.microedition.sensor.Data[] data, final boolean isDataLost) {
                    try {
                        double x = 0;
                        double y = 0;
                        double z = 0;
                        switch(data[0].getChannelInfo().getDataType()) {
                            case javax.microedition.sensor.ChannelInfo.TYPE_INT:
                                x = (double) data[0].getIntValues()[0];
                                break;
                            case javax.microedition.sensor.ChannelInfo.TYPE_DOUBLE:
                                x = data[0].getDoubleValues()[0];
                                break;
                        }
                        switch(data[1].getChannelInfo().getDataType()) {
                            case javax.microedition.sensor.ChannelInfo.TYPE_INT:
                                y = (double) data[1].getIntValues()[0];
                                break;
                            case javax.microedition.sensor.ChannelInfo.TYPE_DOUBLE:
                                y = data[1].getDoubleValues()[0];
                                break;
                        }
                        switch(data[2].getChannelInfo().getDataType()) {
                            case javax.microedition.sensor.ChannelInfo.TYPE_INT:
                                z = (double) data[2].getIntValues()[0];
                                break;
                            case javax.microedition.sensor.ChannelInfo.TYPE_DOUBLE:
                                z = data[2].getDoubleValues()[0];
                                break;
                        }
                        if (canvas.isAutoChangeOrientation()) {
                            final int oldTransform = canvas.transform;
                            if (x < -500) {
                                canvas.transform = Sprite.TRANS_ROT270;
                            } else if (x > 500) {
                                canvas.transform = Sprite.TRANS_ROT90;
                            } else if (y < -500) {
                                canvas.transform = Sprite.TRANS_ROT180;
                            } else if (y > 500) {
                                canvas.transform = Sprite.TRANS_NONE;
                            }
                            if (oldTransform != canvas.transform) {
                                canvas.onDeviceRotated();
                            }
                        }
                        if (canvas.isUseAccelerometer()) {
                            canvas.onAccelerometerChange(x, y, z);
                        }
                    } catch (Exception e) {
                    }
                }
            }, 1);
        } catch (Throwable t) {
            throw new RuntimeException(t.getMessage());
        }
    }
