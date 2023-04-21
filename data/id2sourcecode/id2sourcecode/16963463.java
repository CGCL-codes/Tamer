        public void onOutputAvailable(IStreamReader reader) {
            int read = reader.read(buffer);
            try {
                while (read > 0) {
                    write(buffer, read);
                    read = reader.read(buffer);
                }
                flush();
            } catch (IOException ex) {
                Shell4EclipsePlugin.log(IStatus.ERROR, "Failed writing to the Console View", ex);
            }
        }
