    public static void main(String[] args) {
        StatisticsManager.getInstance().startEvaluation(NUMBER_TRANSACTIONS);
        ClientStarter starter;
        int numberParticipants;
        Random randomGenerator = new Random();
        for (int i = 0; i < NUMBER_TRANSACTIONS; i++) {
            numberParticipants = randomGenerator.nextInt(NUMBER_PARTICIPANTS_MAX + 1);
            if (numberParticipants < NUMBER_PARTICIPANTS_MIN) {
                numberParticipants = NUMBER_PARTICIPANTS_MIN;
            }
            starter = new ClientStarter(i + 1, NUMBER_TRANSACTIONS, USE_ABSTRACTSERVICE, numberParticipants, COMPENSATION_FAILURE_PROBABILITY);
            starter.start();
            try {
                Thread.sleep(TIME_SLEEP);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
