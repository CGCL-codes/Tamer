    private void loadControllers() {
        ControllerEnvironment environ = ControllerEnvironment.getDefaultEnvironment();
        controllers = new ArrayList<RSController>();
        for (Controller controller : environ.getControllers()) {
            RSController rsController = new RSController(controller);
            if (rsController.getChannelCount() >= 4) {
                controllers.add(rsController);
            }
        }
    }
