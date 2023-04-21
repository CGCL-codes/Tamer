    void writeThreadsDump() throws IOException {
        final List<ThreadInformations> deadlockedThreads = getDeadLockedThreads();
        if (!deadlockedThreads.isEmpty()) {
            write("#Threads_deadlocks#");
            String separator = " ";
            for (final ThreadInformations thread : deadlockedThreads) {
                writer.write(separator);
                writer.write(thread.getName());
                separator = ", ";
            }
            writer.write("\n\n");
        }
        if (stackTraceEnabled) {
            for (final ThreadInformations threadInformations : threadInformationsList) {
                writer.write('\"');
                writer.write(threadInformations.getName());
                writer.write('\"');
                if (threadInformations.isDaemon()) {
                    writer.write(" daemon");
                }
                writer.write(" prio=");
                writer.write(String.valueOf(threadInformations.getPriority()));
                writer.write(' ');
                writer.write(String.valueOf(threadInformations.getState()));
                final List<StackTraceElement> stackTrace = threadInformations.getStackTrace();
                if (stackTrace != null && !stackTrace.isEmpty()) {
                    for (final StackTraceElement element : stackTrace) {
                        writer.write("\n\t");
                        writer.write(element.toString());
                    }
                }
                writer.write("\n\n");
            }
            writer.write("\n");
        }
    }
