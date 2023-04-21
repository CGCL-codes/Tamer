    public Listener(Database db, IrcConnection irc, UIHandler uihandler) {
        crosswordMessage = "";
        tea = new TEADecryptor();
        this.irc = irc;
        this.uihandler = uihandler;
        this.altnick = db.altnick;
        this.host = db.host;
        this.password = db.passwd;
        this.username = db.username;
        this.realname = db.realname;
        this.port = db.port;
        if (db.usehttp) {
            this.pollmode = false;
            uihandler.getConsole().writeInfo("using HTTP proxy server to connect");
        } else this.pollmode = db.usepoll;
        this.polltime = db.polltime;
        this.showinput = db.showinput;
        channels = db.getChannels();
        whois = new Hashtable();
        needupdate = false;
        nicktried = false;
    }
