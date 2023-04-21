    public void execute(SystemInputStream in, SystemPrintStream out, SystemPrintStream err, String[] args, Map env) throws Exception {
        boolean detailed = false;
        if (args.length == 1) if (args[0].equals("-D")) detailed = true;
        List<RegisteredHardware> registeredHardwareList = workQueue.addWork(getRegisteredHardwareListWorkUnit).get();
        IOUtils.writeLine("Registered hardware:", out);
        IOUtils.writeLine("Format: [HW address, HW desc, HW class, Setup desc, Polling interval, Preferred unit]", out);
        IOUtils.newLine(out);
        for (RegisteredHardware registeredHardware : registeredHardwareList) {
            IOUtils.twoSpaces(out);
            IOUtils.write(registeredHardware.getHardwareAddr() + ", ", out);
            IOUtils.write(registeredHardware.getAppHardwareDescription() + ", ", out);
            IOUtils.write(registeredHardware.getHardwareClassname() + ", ", out);
            IOUtils.write(registeredHardware.getHardwareSetupDescription(), out);
            if (registeredHardware instanceof Sensor) {
                Sensor sensor = (Sensor) registeredHardware;
                IOUtils.write(", " + sensor.getPollingInterval().toString(), out);
                if (sensor instanceof ValueSensor) {
                    ValueSensor vSensor = (ValueSensor) sensor;
                    IOUtils.write(", " + vSensor.getPreferredDataUnit().toString(), out);
                }
            }
            if (detailed) {
                if (registeredHardware instanceof HomenetHardware) {
                    HomenetHardware hw = (HomenetHardware) registeredHardware;
                    IOUtils.newLine(out);
                    for (int i = 0; i < hw.getNumChannels(); i++) {
                        IOUtils.twoSpaces(out);
                        IOUtils.twoSpaces(out);
                        IOUtils.writeLine("CH-" + i + ": " + hw.getChannelDescription(i), out);
                    }
                }
            } else {
                IOUtils.newLine(out);
            }
        }
        IOUtils.newLine(out);
    }
