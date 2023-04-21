        @Override
        public void run() {
            Log.debug("STARTING " + this);
            int read = 0;
            try {
                while ((read = streamToRead.read()) != -1) {
                    if (redirection == null) {
                        sb.append((char) read);
                    } else {
                        redirection.write(read);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                sb.append('\n');
                sb.append(e.toString());
            }
            Log.debug("FINISHING " + this);
        }
