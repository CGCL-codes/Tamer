    protected void checkConnections(List pvList) {
        connectionMap.clear();
        boolean sync = Channel.getSyncRequest();
        Channel.setSyncRequest(true);
        Iterator itr1 = pvList.iterator();
        while (itr1.hasNext()) {
            String name = (String) itr1.next();
            Channel chan = ChannelFactory.defaultFactory().getChannel(name);
            connectionMap.put(name, new Boolean(false));
            chan.addConnectionListener(this);
            chan.requestConnection();
        }
        int i = 0;
        int nDisconnects = connectionMap.size();
        while (nDisconnects > 0 && i < 5) {
            try {
                Thread.currentThread().sleep(400);
            } catch (InterruptedException e) {
                System.out.println("Sleep interrupted during connection check");
                System.err.println(e.getMessage());
                e.printStackTrace();
            }
            nDisconnects = 0;
            Collection vals = connectionMap.values();
            Iterator itr = vals.iterator();
            while (itr.hasNext()) {
                Boolean tf = (Boolean) itr.next();
                if (!(tf.booleanValue())) nDisconnects++;
            }
            i++;
        }
        if (nDisconnects > 0) {
            Toolkit.getDefaultToolkit().beep();
            theDoc.myWindow().textArea.append((new Integer(nDisconnects)).toString() + " PVs were not able to connect");
            System.out.println(nDisconnects + " PVs were not able to connect");
        } else {
            String text = "All  channesl connected";
            theDoc.myWindow().textArea.append(text);
            System.out.println(text);
        }
        Channel.setSyncRequest(sync);
    }
