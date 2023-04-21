        public synchronized void run() {
            while (true) {
                try {
                    while (!processChannel) wait();
                } catch (InterruptedException e) {
                }
                try {
                    rt = Runtime.getRuntime();
                    tzap = rt.exec(conf.get("tzap") + " -c " + confParser.configFilename() + " " + confParser.getChannel(channelNo).channelName());
                    sleep(timeout);
                    dvbStream = rt.exec(conf.get("dvbstream") + " " + confParser.getChannel(channelNo).videoPID() + " " + confParser.getChannel(channelNo).audioPID());
                    wait();
                } catch (Exception e) {
                    System.out.println("RemoteDVB.TzapListner>> Tzap died: " + e);
                    e.printStackTrace();
                }
            }
        }
