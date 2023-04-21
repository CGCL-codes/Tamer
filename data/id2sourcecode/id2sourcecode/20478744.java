    public jShell(String shellCommand) {
        Process child = null;
        try {
            child = Runtime.getRuntime().exec(shellCommand);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (child == null) {
            return;
        }
        final InputStream inputStream = child.getInputStream();
        final BufferedReader brOut = new BufferedReader(new InputStreamReader(inputStream));
        tOut = new Thread() {

            String line;

            int lineNumber = 0;

            public void run() {
                try {
                    while ((line = brOut.readLine()) != null) {
                        System.out.println(lineNumber + ". " + line);
                        lineNumber++;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        final InputStream errorStream = child.getErrorStream();
        final BufferedReader brErr = new BufferedReader(new InputStreamReader(errorStream));
        tErr = new Thread() {

            String line;

            int lineNumber = 0;

            public void run() {
                try {
                    while ((line = brErr.readLine()) != null) {
                        System.out.println(lineNumber + ". " + line);
                        lineNumber++;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        final OutputStream outputStream = child.getOutputStream();
        tIn = new Thread() {

            String line;

            public void run() {
                try {
                    while (true) {
                        outputStream.write((reader.readLine() + "\n").getBytes());
                        outputStream.flush();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
    }
