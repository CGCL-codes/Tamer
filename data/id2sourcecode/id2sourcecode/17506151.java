    public void EXECUTE(MaverickString result, Program program, ConstantString name, MaverickString[] args) {
        if (result != null) {
            result.clear();
            try {
                getChannel(SCREEN_CHANNEL).pushWriter(new StringWriter(result));
            } catch (MaverickException mve) {
                handleError(mve, getStatus());
            }
        }
        while (program != null) {
            boolean stackCommon = getProperty(PROP_STACK_COMMON, DEFAULT_STACK_COMMON);
            ProgramEntry pe = new ProgramEntry(factory, program, name, args, System.currentTimeMillis());
            pe.setPrompt(defaultPrompt);
            if (!stackCommon && peekCommand() != null) {
                pe.setCommon(peekCommand().getCommon());
            } else {
                pe.setCommon(factory.getCommon());
            }
            pushCommand(pe);
            try {
                program.run(this, args);
                program = null;
            } catch (ChainException ce) {
                program = ce.getProgram();
                name = factory.getConstant(program.getClass().getName());
                args = new MaverickString[0];
            } catch (StopException se) {
                String message = se.getMessage();
                if (message != null && message.length() > 0) {
                    handleError(se, getStatus());
                }
                program = null;
            } catch (MaverickException e) {
                handleError(e, getStatus());
                program = null;
            }
            popCommand();
        }
        if (result != null) {
            try {
                getChannel(SCREEN_CHANNEL).popWriter();
            } catch (MaverickException e) {
                handleError(e, getStatus());
            }
            result.CHANGE(result, ConstantString.LINE_SEPARATOR, ConstantString.AM, ConstantString.ZERO, ConstantString.ZERO);
        }
    }
