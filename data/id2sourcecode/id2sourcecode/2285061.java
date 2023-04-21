    public MessageDisplay(JFrame parent, String message, String value) {
        super(parent, true);
        try {
            this.parent = parent;
            gba = new GBA();
            messageDisplayFont = new Font("serif", Font.PLAIN, 12);
            if (message.startsWith("Result set is DEAD")) {
                message = "No data to retrieve for Plate Number " + value;
            } else if (message.startsWith("Incorrect syntax near")) {
                message = value + " is an invalid Plate Number - Code B1";
            } else if (message.startsWith("Invalid column name")) {
                message = value + " is an invalid Plate Number - Code C1";
            } else if (message.startsWith("Connection failed")) {
                message = "Could not connect to database.";
            } else if (message.startsWith("Login failed")) {
                message = "Could not login to database.";
            } else if (message.startsWith("SELECT permission denied")) {
                message = "Database access error. Contact support.";
            } else if (message.startsWith("EXECUTE permission denied")) {
                message = "Database access error. Contact support.";
            } else if (message.startsWith("divide by zero")) {
                message = "Numerical Exception. Contact support.";
            } else if (message.startsWith("Null")) {
                message = "Null on Plate Number " + value + "?! Contact support.";
            } else if (message.startsWith("Thread write failed")) {
                message = "Error writing data to socket. Contact support.";
            } else if (message.startsWith("Server user id")) {
                message = "Invalid login for database.";
            } else if (message.startsWith("Subquery returned more than 1 value")) {
                message = "Query exception. Contact support.";
            }
            messageLabel = new JLabel(message);
            messageLabel.setFont(messageDisplayFont);
            messageLabel.addKeyListener(new EventListener());
            closeButton = new JButton("Close TIGR MultiViewer Message");
            closeButton.addActionListener(new EventListener());
            contentPane.setLayout(new GridBagLayout());
            gba.add(contentPane, messageLabel, 0, 0, 3, 1, 1, 1, GBA.B, GBA.C);
            gba.add(contentPane, closeButton, 1, 1, 1, 1, 0, 0, GBA.NONE, GBA.C);
            pack();
            setResizable(true);
            setTitle("TIGR MultiViewer Message");
            setLocation(300, 300);
            show();
        } catch (Exception e) {
            System.out.println("Exception (MessageDisplay.const()): " + e);
        }
    }
