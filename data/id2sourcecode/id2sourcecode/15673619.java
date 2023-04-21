    protected synchronized void write() {
        if (entries.isEmpty()) {
            return;
        }
        try {
            ensureOpen();
            int l = entries.size();
            for (int i = 0; i < l; i++) {
                Entry entry = (Entry) entries.remove(0);
                writer.print(entry.date);
                writer.print(entry.level);
                writer.print(entry.threadId);
                writer.println(entry.message);
                if (entry.exception != null) entry.exception.printStackTrace(writer);
            }
            writer.flush();
        } catch (Exception x) {
            int size = entries.size();
            if (size > 1000) {
                System.err.println("Error writing log file " + this + ": " + x);
                System.err.println("Discarding " + size + " log entries.");
                entries.clear();
            }
        }
    }
