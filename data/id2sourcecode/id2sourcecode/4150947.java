        ClientGroup(int connectPort, String... users) {
            clients = new HashMap<String, DummyClient>();
            for (String user : users) {
                DummyClient client = new DummyClient(user);
                clients.put(user, client);
                client.connect(connectPort);
                client.login();
            }
        }
