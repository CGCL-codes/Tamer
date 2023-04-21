    public String dumpState() {
        Writer writer = new StringWriter();
        ValuePrettyPrinter printer = new ValuePrettyPrinter(Value.createDeepCopy(interpreter.globalValue()), writer, "Global state");
        try {
            printer.run();
            printer = new ValuePrettyPrinter(Value.createDeepCopy(ExecutionThread.currentThread().state().root()), writer, "Session state");
            printer.run();
        } catch (IOException e) {
        }
        return writer.toString();
    }
