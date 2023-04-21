    private void writeConnection(ConnectionInformations connection) throws IOException {
        write("<td align='right'>");
        writeTextWithStackTrace(dateTimeFormat.format(connection.getOpeningDate()), connection.getOpeningStackTrace());
        write("</td><td>");
        final Thread thread = threadsById.get(connection.getThreadId());
        if (thread == null) {
            write("&nbsp;");
        } else {
            final StackTraceElement[] stackTrace = stackTracesByThread.get(thread);
            writeTextWithStackTrace(thread.getName(), stackTrace != null ? Arrays.asList(stackTrace) : null);
        }
        write("</td>");
    }
