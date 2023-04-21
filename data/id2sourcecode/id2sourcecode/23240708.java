    void writeExecutedMethod(ThreadInformations threadInformations) throws IOException {
        final String executedMethod = threadInformations.getExecutedMethod();
        if (executedMethod != null && executedMethod.length() != 0) {
            write(htmlEncode(executedMethod));
        } else {
            write("&nbsp;");
        }
    }
