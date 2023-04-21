    public void write(String level, long thread_id, String description, String details) {
        try {
            if (dont_log) return;
            buildOutputFile();
            output.write(level + " [" + thread_id + "] " + date_format.format(new Date()) + "\n" + "Description: " + description + "\n" + details + "\n" + "-------------------------------------\n");
            output.flush();
            output.close();
            setExceptionModeOff();
        } catch (IOException ioe) {
            setExceptionModeOn(ioe);
        }
    }
