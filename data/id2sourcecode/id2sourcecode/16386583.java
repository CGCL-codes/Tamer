    protected String getSupplementDetailString(SessionLogEntry entry) {
        StringWriter writer = new StringWriter();
        if (shouldPrintDate()) {
            writer.write(getDateString(entry.getDate()));
            writer.write("--");
        }
        if (shouldPrintSession() && (entry.getSession() != null)) {
            writer.write(this.getSessionString(entry.getSession()));
            writer.write("--");
        }
        if (shouldPrintConnection() && (entry.getConnection() != null)) {
            writer.write(this.getConnectionString(entry.getConnection()));
            writer.write("--");
        }
        if (shouldPrintThread()) {
            writer.write(this.getThreadString(entry.getThread()));
            writer.write("--");
        }
        return writer.toString();
    }
