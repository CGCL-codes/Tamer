    public void testLlrf() {
        Channel rate = ChannelFactory.defaultFactory().getChannel("Test_LLRF:FCM13:HBInt_Rate");
        ValueListener l1 = addMonitor("Test_LLRF:FCM13:Fwd_WfA");
        ValueListener l2 = addMonitor("Test_LLRF:FCM13:Rfl_WfA");
        ValueListener l3 = addMonitor("Test_LLRF:FCM13:Field_WfA");
        waitForMonitor();
        l1.debugPrint();
        l2.debugPrint();
        l3.debugPrint();
    }
