    public void doSomething() {
        String clientIdentifier = this.getClientIdentifier();
        if (!this.isRegisteredAtCoordinator()) {
            try {
                this.registerAtCoordinator();
            } catch (ParticipantException e) {
                System.out.println("ConcreteService1 exception: " + e.getLocalizedMessage());
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
        r = randomGenerator.nextInt(101);
        if (r <= FAILURE_PROBABILITY) {
            System.out.println("Concrete Service 1 - Request failed: " + clientIdentifier);
            try {
                MessageContext messageContext = MessageContext.getCurrentContext();
                Message request = messageContext.getRequestMessage();
                AttributedURI messageID = HeaderProcessing.getMessageID(request);
                this.sendFault("Internal service error. The request could not be processed correctly.", messageID);
            } catch (Exception e) {
                System.out.println("ConcreteService1 exception: " + e.getLocalizedMessage());
            }
            StatisticsManager.getInstance().addFinishedService(false);
        } else {
            StatisticsManager.getInstance().addFinishedService(true);
        }
    }
