    public final void writeNext(String[] nextLine) {
        try {
            writer.writeNext(nextLine);
            writer.flush();
        } catch (IOException e) {
            SysLog.error(e);
        }
    }
