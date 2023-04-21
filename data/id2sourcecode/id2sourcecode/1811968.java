    private void drain(final InputStream stream, final PrintWriter pw) {
        this.thread = new Thread() {

            public void run() {
                InputStreamReader reader = new InputStreamReader(stream);
                BufferedReader buffer = new BufferedReader(reader);
                try {
                    while (true) {
                        String nextLine = buffer.readLine();
                        if (nextLine == null) {
                            break;
                        } else {
                            if (pw != null) {
                                pw.println(nextLine);
                            }
                        }
                    }
                } catch (IOException eIO) {
                }
                if (pw != null) {
                    pw.flush();
                }
            }
        };
        this.thread.start();
    }
