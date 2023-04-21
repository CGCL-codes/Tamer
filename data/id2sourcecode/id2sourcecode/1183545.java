    @Override
    public void validateMap(@NotNull final MapModel<G, A, R> mapModel, @NotNull final ErrorCollector<G, A, R> errorCollector) {
        final String[] command = getCommand(mapModel, errorCollector);
        if (command == null) {
            return;
        }
        final Process process;
        try {
            process = Runtime.getRuntime().exec(command);
        } catch (final IOException ex) {
            errorCollector.collect(new MapCheckerScriptMissingError<G, A, R>(mapModel, command[0], ex.getMessage()));
            return;
        }
        @Nullable final String output;
        try {
            try {
                process.getOutputStream().close();
            } catch (final IOException ex) {
                errorCollector.collect(new MapCheckerScriptFailureError<G, A, R>(mapModel, command[0], ex.getMessage() + " (closing stdin)"));
                return;
            }
            output = runProcess(process, errorCollector, mapModel, command[0]);
        } finally {
            process.destroy();
        }
        if (output != null) {
            parseOutput(output, errorCollector, mapModel, command[0]);
        }
    }
