        ReaperThread(Writer writer, InputStream is) {
            super("reaper thread");
            this.writer = writer;
            this.is = is;
        }
