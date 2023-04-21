    public void initialize(Properties props) {
        getChannel("position");
        TaskManager taskManager = AppContext.getTaskManager();
        for (int i = 0; i < 5; i++) {
            GameAgent agent = new GameAgent();
            agent.setPosition(new Position((float) (Math.random() * 200), (float) (Math.random() * 200), 0, (float) (Math.random() * 360)));
            agent.setSpeed(10);
            taskManager.schedulePeriodicTask(agent, 0, 200);
        }
    }
