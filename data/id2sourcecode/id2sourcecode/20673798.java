    private static synchronized void out(Type lvl, String description, String details) {
        cleanup_room.enter();
        System.out.flush();
        try {
            long thread_id = ThreadId.getCurrentId();
            Date timestamp = new Date();
            if (trace_to_screen || lvl.value > trace.value) {
                System.out.println(lvl + "[" + thread_id + "] " + date_format.format(timestamp) + LS + "Description: " + description + LS + details + LS + "-------------------------------------");
            }
            for (int t = 0; t < html_loggers.size(); t++) {
                ((HTMLLogger) html_loggers.get(t)).out(thread_id, lvl, description, details, timestamp);
            }
            if (lvl.value > trace.value && html_out != null) {
                html_out.write(lvl.getHTMLForLog(thread_id, description, details, timestamp));
            }
        } finally {
            System.out.flush();
            cleanup_room.exit();
        }
    }
