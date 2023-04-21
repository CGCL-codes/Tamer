    private void acceptClient() {
        try {
            removeUnusedAgents();
            Socket cliSock;
            try {
                cliSock = srvSock.accept();
            } catch (IOException e) {
                throw new ExceptionAccept();
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(cliSock.getInputStream()));
            String agentIdentifier = br.readLine();
            if (!identifierValid(agentIdentifier) || identifierAlreadyExists(agentIdentifier)) {
                cliSock.getOutputStream().write("KO => Login already exists!".getBytes());
                cliSock.close();
            } else {
                if (agentIdentifier.startsWith("pilote")) {
                    Debug.message("Connexion du pilote \"" + agentIdentifier + "\"");
                    Pilote tmpPilote = new Pilote(this, cliSock, agentIdentifier);
                    tmpPilote.start();
                    pilotes.add(tmpPilote);
                } else {
                    Debug.message("Connexion du client \"" + agentIdentifier + "\"");
                    Client cli = new Client(this, cliSock, agentIdentifier);
                    cli.start();
                    clients.add(cli);
                }
            }
        } catch (ExceptionAccept e1) {
        } catch (IOException e2) {
            Debug.error(e2.getLocalizedMessage());
        }
    }
