    public void newConnection(String fullNick, IRCConnection origin, Socket s) {
        try {
            BufferedReader input = new BufferedReader(new InputStreamReader(s.getInputStream()));
            DataOutputStream output = new DataOutputStream(s.getOutputStream());
            String nick;
            output.writeBytes("User name:\n");
            String userName = input.readLine();
            output.writeBytes("Password:\n");
            String password = input.readLine();
            while (true) {
                output.writeBytes("PartyLine nick:\n");
                nick = input.readLine();
                if (server.isValidNick(nick)) {
                    break;
                }
                output.writeBytes("Nick already in use.\n");
            }
            server.addUser(new PartyLine2User(fullNick, nick, s, input, output, server));
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
