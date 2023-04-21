    public boolean writeCredential() throws IOException, InterruptedException {
        String line = null;
        if (credentialCounter == 0 && client.getLogin() != null && client.getLogin().length() > 0) {
            line = client.getLogin();
            client.setLogin(null);
            SAWTerminal.println("");
        } else if (credentialCounter == 1 && client.getPassword() != null && client.getPassword().length() > 0) {
            line = client.getPassword();
            client.setPassword(null);
            SAWTerminal.println("");
        } else {
            line = SAWTerminal.readLine(false);
        }
        if (line == null) {
            System.exit(0);
        }
        sha256Digester.update(sha256Digester.digest(line.getBytes("UTF-8")));
        sha256Digester.update(remoteNonce);
        connection.getAuthenticationWriter().write(new String(Base64.encodeBase64(sha256Digester.digest(localNonce)), "UTF-8") + "\n");
        SAWTerminal.print("SAW>SAWCLIENT:Information sent!");
        connection.getAuthenticationWriter().flush();
        credentialCounter++;
        return true;
    }
