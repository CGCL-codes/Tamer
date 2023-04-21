    public void commandEntered(String consoleInput) {
        if (consoleInput.compareTo("exit") == 0) {
            if (this.client.isConnected()) {
                this.console.write("Still connected to server. Disconnect first");
                return;
            }
            console.stop();
        } else if (consoleInput.startsWith("ss7 connect")) {
            if (client.isConnected()) {
                console.write("Already connected");
                return;
            }
            String[] commands = consoleInput.split(" ");
            if (commands.length == 2) {
                try {
                    client.connect(new InetSocketAddress(address, port));
                    console.setPrefix(Shell.CLI_PREFIX + "(local)" + Shell.CLI_POSTFIX);
                } catch (IOException e) {
                    console.write(e.getMessage());
                }
            } else if (commands.length == 4) {
                address = commands[2];
                port = Integer.parseInt(commands[3]);
                try {
                    client.connect(new InetSocketAddress(address, port));
                    this.console.setPrefix(Shell.CLI_PREFIX + "(" + address + ":" + port + ")" + Shell.CLI_POSTFIX);
                } catch (IOException e) {
                    console.write(e.getMessage());
                }
            } else {
                console.write("Invalid command.");
            }
        } else if (consoleInput.startsWith("ss7 disconnect")) {
            if (!client.isConnected()) {
                console.write("Already disconnected");
                return;
            }
            sendMessage("disconnect");
        } else {
            sendMessage(consoleInput);
        }
    }
