    private void captureData(String hardwareAddr, SystemPrintStream out, SystemPrintStream err) {
        if (hardwareAddr == null) {
            IOUtils.writeLine("ERROR: Invalid hardware address: " + hardwareAddr, out);
            IOUtils.newLine(out);
        }
        try {
            RegisteredHardware registeredHardware = serverContext.getHardwareManager().getRegisteredHardware(hardwareAddr);
            if (registeredHardware instanceof Sensor) {
                WorkUnit<List<HardwareData>> captureDataWorkUnit = new CaptureSensorDataWorkUnit((Sensor) registeredHardware);
                try {
                    List<HardwareData> hardwareDataList = serverContext.getWorkQueue().addWork(captureDataWorkUnit).get();
                    IOUtils.twoSpaces(out);
                    IOUtils.writeLine("Hardware address: " + hardwareAddr, out);
                    for (HardwareData data : hardwareDataList) {
                        IOUtils.twoSpaces(out);
                        IOUtils.twoSpaces(out);
                        IOUtils.writeLine("CH-" + data.getChannel() + ", data: " + data.getDataString() + ", timestamp: " + data.getTimestamp().toString(), out);
                    }
                    IOUtils.newLine(out);
                } catch (InterruptedException ie) {
                    IOUtils.writeLine("ERROR: Error while capturing data: " + ie.getMessage(), out);
                    ie.printStackTrace(err);
                    IOUtils.newLine(out);
                } catch (ExecutionException ee) {
                    IOUtils.writeLine("ERROR: Error while capturing data: " + ee.getMessage(), out);
                    ee.printStackTrace(err);
                    IOUtils.newLine(out);
                }
            } else {
                IOUtils.writeLine("Hardware must be a sensor!", out);
                IOUtils.newLine(out);
            }
        } catch (NoSuchHardwareException nshe) {
            IOUtils.write("No such hardware exists", out);
            IOUtils.newLine(out);
        }
    }
