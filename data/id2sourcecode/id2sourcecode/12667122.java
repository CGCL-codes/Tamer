        @SuppressWarnings("synthetic-access")
        public void execute(TestConsole console, String[] cmd) {
            Class<?>[] params = this.method.getParameterTypes();
            Object[] args = new Object[params.length];
            if (params.length > cmd.length - 1) {
                console.result.setText("Invalid number of command arguments");
                return;
            }
            for (int i = 0; i < params.length; i++) {
                if (params[i] == String.class) args[i] = cmd[i + 1]; else if (params[i] == int.class) {
                    try {
                        args[i] = Integer.parseInt(cmd[i + 1]);
                    } catch (NumberFormatException ex) {
                        console.result.setText("Value " + cmd[i + 1] + " is not a number");
                        return;
                    }
                } else {
                    console.logger.warn("Unable to parse class " + params[i]);
                    args[i] = null;
                }
            }
            try {
                this.method.invoke(console, args);
            } catch (Exception ex) {
                console.logger.error("Unable to call method", ex);
            }
        }
