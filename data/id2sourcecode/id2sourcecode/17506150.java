    public void EXECUTE(MaverickString result, ConstantString command) {
        try {
            if (result != null) {
                result.clear();
                getChannel(SCREEN_CHANNEL).pushWriter(new StringWriter(result));
            }
            Program program = factory.getCommand(command);
            MaverickString[] args = new MaverickString[0];
            program.run(this, args);
        } catch (MaverickException e) {
            handleError(e, getStatus());
        }
        if (result != null) {
            try {
                getChannel(SCREEN_CHANNEL).popWriter();
            } catch (MaverickException mve) {
                handleError(mve, getStatus());
            }
            result.CHANGE(result, ConstantString.LINE_SEPARATOR, ConstantString.AM, ConstantString.ZERO, ConstantString.ZERO);
        }
    }
