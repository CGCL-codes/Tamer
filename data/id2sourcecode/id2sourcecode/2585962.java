    private void getScores() throws IOException {
        URL url = new URL("http://www.deskteam.net/sysc4504/gameServer.jsp?player=" + getPlayerName() + "&score=" + getCurrentScore());
        InputStream in = url.openStream();
        byte[] buffer = new byte[1096];
        int receivedBytes;
        String pageContent = "";
        while ((receivedBytes = in.read(buffer)) != -1) {
            pageContent += new String(buffer);
        }
        in.close();
        updateAllPlayersScore((parseWord(pageContent)).replace(";", "\n").replace(":", "\t"));
    }
