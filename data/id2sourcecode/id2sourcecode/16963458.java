        @Override
        public void run() {
            IOConsoleInputStream inputStream = getInputStream();
            byte[] buffer = new byte[1024];
            try {
                for (; process != null; ) {
                    int read = inputStream.read(buffer);
                    if (read > 0) {
                        process.writeStdIn(buffer, read);
                    }
                }
            } catch (IOException ex) {
                Shell4EclipsePlugin.log(IStatus.ERROR, "Failed to write subprocess's STDIN", ex);
            }
        }
