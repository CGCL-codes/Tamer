    public void run() {
        String line;
        running = true;
        try {
            while (running) {
                line = queue.getNextMessage();
                if (line != null) {
                    libairc.debug("Outgoing", line);
                    out.write(line + "\n");
                    out.flush();
                    sleep(1000);
                } else {
                    sleep(500);
                }
            }
        } catch (InterruptedException iEx) {
            libairc.exception("Outgoing thread: wait()", iEx);
        } catch (IOException ioEx) {
            libairc.exception("Outgoing thread: write()", ioEx);
        }
        try {
            socket.close();
        } catch (IOException ioEx) {
            libairc.exception("Closing outgoing socket", ioEx);
        }
    }
