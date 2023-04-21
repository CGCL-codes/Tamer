    public void writeEventUnit(EventUnit unit) throws IOException {
        File file = getFile();
        if (file.length() > getMaxFileSize()) {
            renameFile();
        }
        ILineWriter out = getOutput();
        Event event = unit.getEvent();
        if (unit.getAction().equals(EventUnit.END)) {
            out.writeLine(unit.toString() + "," + unit.getThreadId() + ", time=" + event.getProcessingTime());
        } else if (unit.getAction().equals(EventUnit.SYSTEM)) {
            Event evt = unit.getEvent();
            out.writeLine(evt.getClassName() + "," + evt.getMethodName() + "," + unit.getEncodedData() + "," + unit.getMetricValue());
        } else {
            out.writeLine(unit.toString() + "," + unit.getThreadId());
        }
    }
