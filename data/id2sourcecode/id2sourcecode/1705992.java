    public void run() {
        while (true) {
            Properties currChannelProperties = readConfiguration();
            int numThreadChannels = Integer.parseInt((String) currChannelProperties.get(EgaChannelLabel.getNumThreadChannels()));
            long channelPollingTime = Long.parseLong((String) currChannelProperties.get(EgaChannelLabel.getChannelPollingTime()));
            eventsToSend.addAll(pollingDB());
            executorService = Executors.newFixedThreadPool(numThreadChannels);
            Iterator it = eventsToSend.iterator();
            List<Future<KpeopleEvent>> futures = new ArrayList<Future<KpeopleEvent>>();
            while (it.hasNext()) {
                KpeopleEvent currEvent = (KpeopleEvent) it.next();
                futures.add(executorService.submit(new EgaChannel(currEvent)));
            }
            eventsToSend.clear();
            try {
                Thread.sleep(channelPollingTime);
                synchronized (this) {
                    if (stopped) {
                        break;
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
