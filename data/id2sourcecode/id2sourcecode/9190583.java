    public boolean check(int count) {
        if (hasInput()) {
            if (cli.processCommand((String) inLines.removeFirst(), outLines)) {
                outLines.add(prompt);
            } else close();
        }
        if (hasOutput()) {
            if (!writeSelected) {
                log.finest("cli: DEBUG: Registering for WRITE.");
                selectionKey.interestOps(SelectionKey.OP_WRITE);
                writeSelected = true;
            }
        } else {
            if (writeSelected) {
                log.finest("cli: DEBUG: Registering for READ.");
                selectionKey.interestOps(SelectionKey.OP_READ);
                writeSelected = false;
            }
        }
        if (getChannel().socket().isClosed()) {
            System.out.println("Socket closed.");
            return (false);
        }
        return (!(doClose && (!hasOutput())));
    }
