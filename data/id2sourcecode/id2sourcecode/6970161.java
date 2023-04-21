    public static void turnOnBluetooth() {
        LocalDevice ld = null;
        ServiceRecord sr = null;
        try {
            if (serverUrl == null) init();
            System.out.println(serverUrl);
            synchronized (serverUrl) {
                if (bluetoothIsRunningAndIsDiscoverable) return;
                ld = LocalDevice.getLocalDevice();
                System.out.println(ld);
                System.out.println(ld.getFriendlyName() + "," + ld.getBluetoothAddress());
                ld.setDiscoverable(DiscoveryAgent.GIAC);
                StreamConnectionNotifier notifier = (StreamConnectionNotifier) Connector.open(serverUrl);
                sr = ld.getRecord(notifier);
                System.out.println(sr);
                System.out.println(notifier);
                MLE midlet = MLE.midlet;
                DataElement base = new DataElement(DataElement.DATSEQ);
                for (int i = 0; i < midlet.httpMLPServerNames.length; i++) {
                    DataElement plat = new DataElement(DataElement.DATSEQ);
                    plat.addElement(new DataElement(DataElement.STRING, "p" + midlet.httpMLPServerNames[i]));
                    plat.addElement(new DataElement(DataElement.STRING, "u" + HelperRMSStoreMLibera.getUsername(midlet.httpMLPServerNames[i])));
                    base.addElement(plat);
                }
                sr.setAttributeValue(SERVER_RECORD_MLE, base);
                ld.updateRecord(sr);
                int channel = getChannel(sr, 1);
                System.out.println("channel:" + channel);
                serverAddress = "btspp://" + ld.getBluetoothAddress() + ":" + channel;
                thread = new MyServerThread(notifier);
                System.out.println(thread);
                thread.start();
                bluetoothIsRunningAndIsDiscoverable = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
