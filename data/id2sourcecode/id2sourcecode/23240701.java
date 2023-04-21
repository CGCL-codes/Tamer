    void writeDeadlocks() throws IOException {
        final List<ThreadInformations> deadlockedThreads = getDeadLockedThreads();
        if (!deadlockedThreads.isEmpty()) {
            write("<div class='severe'>#Threads_deadlocks#");
            String separator = " ";
            for (final ThreadInformations thread : deadlockedThreads) {
                writer.write(separator);
                writer.write(htmlEncode(thread.getName()));
                separator = ", ";
            }
            write("</div>");
        }
    }
