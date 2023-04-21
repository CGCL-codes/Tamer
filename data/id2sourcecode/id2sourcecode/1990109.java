    public static void main(String args[]) {
        if (args.length > 0) {
            int i = 0;
            String port = null;
            String username = null;
            String password = null;
            String host = null;
            while (i != args.length) {
                if (args[i].equals("-u")) {
                    username = args[i + 1];
                } else if (args[i].equals("-p")) {
                    password = args[i + 1];
                } else if (args[i].equals("-h")) {
                    host = args[i + 1];
                } else if (args[i].equals("-port")) {
                    port = args[i + 1];
                }
                i++;
            }
            if ((username != null) && (password != null) && (host != null) && (port != null)) {
                MidhedavaClient client = MidhedavaClient.get();
                try {
                    client.connect(host, Integer.parseInt(port), true);
                    client.login(username, password);
                    new j2DClient(client);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                return;
            }
        }
        System.out.println("Midhedava j2DClient\n");
        System.out.println("  games.midhedava.j2DClient -u username -p pass -h host -c character\n");
        System.out.println("Required parameters");
        System.out.println("* -h\tHost that is running Marauroa server");
        System.out.println("* -port\tport of the Marauroa server (try 32160)");
        System.out.println("* -u\tUsername to log into Marauroa server");
        System.out.println("* -p\tPassword to log into Marauroa server");
    }
