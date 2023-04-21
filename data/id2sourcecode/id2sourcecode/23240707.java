    void writeThreadWithStackTrace(ThreadInformations threadInformations) throws IOException {
        final List<StackTraceElement> stackTrace = threadInformations.getStackTrace();
        final String encodedName = htmlEncode(threadInformations.getName());
        if (stackTrace != null && !stackTrace.isEmpty()) {
            writeln("<a class='tooltip'>");
            writeln("<em>");
            writer.write(encodedName);
            writeln("<br/>");
            for (final StackTraceElement stackTraceElement : stackTrace) {
                write(htmlEncode(stackTraceElement.toString()));
                writeln("<br/>");
            }
            writeln("</em>");
            writer.write(encodedName);
            writeln("</a>");
        } else {
            writer.write(encodedName);
        }
    }
