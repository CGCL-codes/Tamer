    private void writeColumnHeaders() {
        try {
            timingsWriter.write("Class, ");
            timingsWriter.write("Method, ");
            timingsWriter.write("Thread, ");
            timingsWriter.write("Test Outcome, ");
            timingsWriter.write("Time (milliseconds), ");
            timingsWriter.write("Memory Used (bytes), ");
            timingsWriter.write("Concurrency level, ");
            timingsWriter.write("Test Size\n");
            timingsWriter.flush();
        } catch (IOException e) {
            throw new RuntimeException("Unable to write out column headers: " + e, e);
        }
    }
