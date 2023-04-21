    private void interpretCommands() {
        SelectionKey key;
        User thisUser;
        int bytesRead;
        ArrayList<String> commands;
        Iterator<SelectionKey> keyIterator = this.userSelector.selectedKeys().iterator();
        while (keyIterator.hasNext()) {
            key = keyIterator.next();
            keyIterator.remove();
            thisUser = (User) key.attachment();
            Util.echo("Storing input from user: '" + thisUser.getUsername() + "'", this);
            try {
                bytesRead = thisUser.getChannel().read(readBuffer);
                if (bytesRead == -1) {
                    Util.echo("Disconnecting user '" + thisUser.getUsername() + "': no bytes to read.", this);
                    thisUser.disconnect();
                    return;
                } else if (bytesRead >= this.maxInputLength) {
                    readBuffer.clear();
                    Comm.writeToChannel(thisUser.getChannel(), uiAlert.display("Input too long. Disconnecting..."));
                    thisUser.disconnect();
                    return;
                }
                thisUser.inputBuffer.append(Comm.bytesToStr(readBuffer));
                commands = this.parseCommands(key);
                for (String command : commands) {
                    command = Util.trimWhiteSpace(command);
                    Util.echo("Command: '" + command + "'", this);
                    if (!thisUser.getCurrentLayer().interpretCommand(command)) {
                        Util.echo("Command was not recognized. User: '" + thisUser.getUsername() + "' - uiLayer: " + thisUser.getCurrentLayer().name + " - Command: '" + command + "'", this);
                        thisUser.send("Huh?\n");
                    }
                }
            } catch (IOException e) {
                Util.echo("Error in handling user input. Moving on...", this);
            }
        }
    }
