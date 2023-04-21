    public void doSomething() {
        if (!this.isRegisteredAtCoordinator()) {
            try {
                this.registerAtCoordinator();
            } catch (ParticipantException e) {
                System.out.println("ConcreteService2 exception: " + e.getLocalizedMessage());
            }
        }
        Random randomGenerator = new Random();
        int r = randomGenerator.nextInt(PROCESSINGTIME_MAX + 1);
        long sleepTimeMilli = 1000;
        if (r < PROCESSINGTIME_MIN) {
            sleepTimeMilli *= PROCESSINGTIME_MIN;
        } else {
            sleepTimeMilli *= r;
        }
        try {
            Thread.sleep(sleepTimeMilli);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        StatisticsManager.getInstance().addFinishedService(true);
    }
