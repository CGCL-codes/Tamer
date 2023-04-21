    @Test
    public void retrieveTemperatureSensorTest() {
        Collection<RegisteredHardware> hwList = persistenceLayer.retrieveRegisteredHardware();
        if (hwList != null) {
            System.out.println("Hardware list IS NOT null!");
            System.out.println("List size: " + hwList.size());
            for (RegisteredHardware hw : hwList) {
                System.out.println("Hardware:");
                System.out.println("  Hardware addr: " + hw.getHardwareAddr());
                System.out.println("  App desc: " + hw.getAppHardwareDescription());
                System.out.println("  Setup desc: " + hw.getHardwareSetupDescription());
                if (hw instanceof HomenetHardware) {
                    System.out.println("  # I/O channels: " + ((HomenetHardware) hw).getNumChannels());
                    for (int i = 0; i < ((HomenetHardware) hw).getNumChannels(); i++) System.out.println("    CH-" + i + ": " + ((HomenetHardware) hw).getChannelDescription(i));
                }
                if (hw instanceof Sensor) System.out.println("  Polling interval: " + ((Sensor) hw).getPollingInterval().toString());
            }
        } else {
            System.out.println("Hardware list IS null.");
        }
    }
