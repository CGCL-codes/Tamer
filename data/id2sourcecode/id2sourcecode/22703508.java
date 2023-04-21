    public static void main(String[] argv) {
        if (argv.length != 1) {
            System.out.println("ClientGUI usage:");
            System.out.println("  java ClientGUI [server config file]");
            System.exit(1);
        }
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                createGUI();
            }
        });
        String configFilename = argv[0];
        log("Working on " + configFilename);
        client = new ClientRMI(configFilename);
        getChannels();
        statusField.setText("Server playing: " + client.currentServerChannel());
        chanList.setSelectedValue(client.currentServerChannel(), true);
        playerButton.setEnabled(true);
    }
