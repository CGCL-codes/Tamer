        void out(long thread_id, Type lvl, String description, String details, Date timestamp) {
            if (!done && socket.isConnected()) {
                try {
                    output.write(lvl.getHTMLForLog(thread_id, description, details, timestamp).getBytes());
                } catch (Exception e) {
                    done = true;
                }
            } else {
                done = true;
            }
        }
