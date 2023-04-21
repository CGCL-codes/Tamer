    InputThread(PircBot bot, Socket socket, BufferedReader breader, BufferedWriter bwriter) {
        _bot = bot;
        _socket = socket;
        _breader = breader;
        _bwriter = bwriter;
        this.setName(this.getClass() + "-Thread");
    }
