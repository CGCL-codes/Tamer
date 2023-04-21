    public void control() {
        this.set = this.createCommandSet();
        this.context = this.createContext(reader, writer);
        this.running = true;
        this.controllingThread = Thread.currentThread();
        try {
            while (running) {
                try {
                    if (this.controller != null) {
                        if (this.controller == this) {
                            try {
                                String[] commandInfo = this.captureCommand(this.getPrompt());
                                if (commandInfo == null) continue;
                                if (commandInfo.length == 0 || (commandInfo.length > 0 && "".equals(commandInfo[0]))) continue;
                                Object results = this.execute(commandInfo);
                                if (results != null) {
                                    this.handleExecutionResults(results);
                                }
                            } catch (UserInterfaceException uie) {
                                this.running = false;
                                return;
                            }
                        } else this.controller.control();
                    } else ThreadUtil.sleep(500);
                } catch (CommandLinePromptControlException e) {
                    this.controller = e.getController() == null ? this : e.getController();
                } catch (Exception e) {
                    if (!(e instanceof InterruptedException)) {
                        throw new RuntimeException(e);
                    }
                }
            }
        } finally {
            this.cleanup();
        }
    }
