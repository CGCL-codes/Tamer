    public void play() throws MalformedURLException, IOException {
        try {
            if (tmp.getStatus() == BasicPlayer.STOPPED || tmp.getStatus() == BasicPlayer.UNKNOWN) {
                con = urlLink.openConnection();
                String contentLength = con.getHeaderField("Content-Length");
                if (contentLength != null) setSongLength(Integer.parseInt(contentLength));
                this.setInput(con.getInputStream());
                getPlayer().open(this.getInput());
                getPlayer().play();
            } else if (tmp.getStatus() == BasicPlayer.PLAYING) {
                getPlayer().stop();
                this.getInput().close();
                this.play();
            } else if (tmp.getStatus() == BasicPlayer.PAUSED) {
                getPlayer().resume();
            }
        } catch (BasicPlayerException ex) {
            System.out.println(ex);
        }
    }
