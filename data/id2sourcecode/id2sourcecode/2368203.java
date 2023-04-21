        public void run() {
            try {
                while ((readBytes = inputStream.read(sBuffer)) != -1 && !cancelled) {
                    content.write(sBuffer, 0, readBytes);
                }
            } catch (IOException e) {
                failed = true;
            }
        }
