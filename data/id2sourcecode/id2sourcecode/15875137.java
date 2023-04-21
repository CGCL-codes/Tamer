    public boolean connect(String deviceName) {
        try {
            if (deviceName.equals("Test Output Device")) {
                dmxDevice = new TestOutputDevice(maxChannels);
            } else if (deviceName.equals("USBDMX.com Device")) {
                dmxDevice = new USBDMXInterface();
            } else if (deviceName.equals("Art-Net Device")) {
                dmxDevice = new ArtNetDmxInterface();
            } else return false;
        } catch (Throwable e) {
            dmxDevice = DummyDMXDevice.Singleton;
            connector.deviceDisconnected();
            connector.raiseError(e, "There was an error connecting to the " + deviceName + " device.  If you do not have a " + deviceName + " device installed, you can ignore this message.  If you do have the device connected, please email this error to me at sinorm@gmail.com.\n\nThe dummy DMX driver has been loaded, the program will continue to operate in disconnected mode");
            e.printStackTrace();
            return false;
        }
        try {
            dmxDevice.setValues(channelValues.getChannels());
        } catch (IDMXDeviceException e) {
            writeError(e);
            return false;
        }
        return true;
    }
