    private Thread createWriterThread() {
        Thread writer = new Thread("Writer") {

            public void run() {
                try {
                    while (true) {
                        RobotAction action = jobs.next();
                        out.writeObject(action);
                        out.flush();
                    }
                } catch (Exception e) {
                    System.out.println("Connection to " + studentName + " closed (" + e + ')');
                    setTitle(getTitle() + " - disconnected");
                }
            }
        };
        writer.start();
        return writer;
    }
