    @Before
    public void setup() {
        signalProcessor = new TestSignalProcessor();
        device = new TestDevice();
        device.setSignalProcessor(signalProcessor);
        createStates();
        for (EEGChannelState state : states) {
            device.addChannelState(state);
        }
        readListener = new EEGReadListener() {

            public void readEventPerformed(EEGReadEvent e) {
                setValues(e.getChannels());
            }
        };
        statusListener = new EEGDeviceStatusListener() {

            public void statusChanged(EEGDeviceStatusEvent e) {
                setStatus(e.getNewStatus());
            }
        };
        device.addDeviceStatusListener(statusListener);
        device.addEEGReadListener(readListener);
    }
