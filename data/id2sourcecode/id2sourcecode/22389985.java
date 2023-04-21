    private void popCommand() {
        numberCommands--;
        int i = 0;
        for (i = 0; i < numberCommands; i++) {
            commands[i] = commands[i + 1];
        }
        commands[++i] = null;
    }
