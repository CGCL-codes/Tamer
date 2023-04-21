    private void connectHScannerPVs() {
        ChannelFactory cf = ChannelFactory.defaultFactory();
        ch_posh = cf.getChannel("Ring_Diag:ELS01:PosX");
        ch_h = cf.getChannel("Ring_Diag:ELS01:SignalX");
        ch_h.connectAndWait();
        ch_posh.connectAndWait();
    }
