    public LoginManagerImp(BufferedReader read, PrintWriter write, PlayerManager p, int capacity) {
        this.in = read;
        this.out = write;
        this.players = p;
        this.serverCapacity = capacity;
        this.loggedOn = false;
    }
