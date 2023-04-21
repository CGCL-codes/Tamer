    private void connectVScannerPVs() {
        ChannelFactory cf = ChannelFactory.defaultFactory();
        ch_posv = cf.getChannel("Ring_Diag:ELS01:PosY");
        ch_v = cf.getChannel("Ring_Diag:ELS01:SignalY");
        ch_v.connectAndWait();
        ch_posv.connectAndWait();
    }
