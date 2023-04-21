    public static void main(String[] args) throws Exception {
        OSIDataLinkDevice[] devices = OSIDataLinkDevice.getDevices();
        AirPcapDevice airDev = null;
        for (int ii = 0; ii < devices.length; ++ii) if (devices[ii] instanceof AirPcapDevice) {
            airDev = (AirPcapDevice) devices[ii];
            break;
        }
        if (airDev == null) {
            System.err.println("No AirPcap devices found!");
            return;
        }
        if (args.length > 0) {
            airDev.setChannel(Integer.parseInt(args[0]));
        }
        System.out.println("" + airDev + " channel " + airDev.getChannel());
        airDev.addPacketListener(new AirPcapListener());
        try {
            airDev.startCapture();
            System.in.read();
        } finally {
            airDev.stopCapture();
        }
    }
