    public boolean doFTTSelfTest() {
        try {
            if (fttConnected) {
                sendLoopbackReq();
                byte[] data = fttDeviceManager.getChannelStatus();
                if (data == null) return false;
                sendMaskData(data);
                return true;
            }
        } catch (IOException e) {
            Environment.getEnvironment().unhandledException(e);
        }
        return false;
    }
