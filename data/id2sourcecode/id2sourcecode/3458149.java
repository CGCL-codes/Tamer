    @Override
    public void run() {
        while (false == disconnectClient) {
            try {
                String word = fromClient.readUTF();
                Server.logln("Client " + clientName + " submited the word \"" + word + "\"");
                if (submitted.contains(word)) {
                    toClient.writeUTF("Word rejected: already submitted");
                    continue;
                }
                int wordScore = computeScore(word);
                submitted.add(word);
                score += wordScore;
                String msg = "Word accepted, score: " + score;
                toClient.writeUTF(msg);
                Server.logln("Client " + clientName + ": " + msg);
                Server.server.updateScore(row, score);
            } catch (IOException ex) {
                Server.log(ex);
                disconnectClient = true;
            }
        }
        disconnect();
    }
