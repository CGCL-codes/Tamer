    public static void main(String[] args) {
        GUI gUI = new GUI();
        Controller.getInstance().setMainPanel(gUI);
        InitialDataFilter initialDataFilter = new InitialDataFilter();
        CompoundFilterRawData rawDataFilter = new CompoundFilterRawData();
        initialDataFilter.addListener(rawDataFilter);
        CompoundFilter1 filter1 = new CompoundFilter1();
        initialDataFilter.addListener(filter1);
        CompoundFilter2 filter2 = new CompoundFilter2();
        initialDataFilter.addListener(filter2);
        initialDataFilter.addListener(new TimeStampsFilter());
        rawDataFilter.addListener(new DataListener() {

            public void dataReceived(Data data) {
                Controller.getInstance().appendInitialData(data.getValue());
            }
        });
        filter1.addListener(new DataListener() {

            public void dataReceived(Data data) {
                Controller.getInstance().appendCompoundFilterData1(data.getValue());
            }
        });
        filter2.addListener(new DataListener() {

            public void dataReceived(Data data) {
                Controller.getInstance().appendCompoundFilterData2(data.getValue());
            }
        });
        EEGAcquisitionController.getInstance().getChannelSampleGenerator().addSampleListener(initialDataFilter, new int[] { 2 });
    }
