    public final synchronized void connect(String hostname, int port, String password) throws IOException, IrcException, NickAlreadyInUseException {
        _server = hostname;
        _port = port;
        _password = password;
        if (isConnected()) {
            throw new IOException("The SpiritBot is already connected to an IRC server.  Disconnect first.");
        }
        this.removeAllChannels();
        Socket socket = new Socket(hostname, port);
        this.log("*** Connected to server....");
        _inetAddress = socket.getLocalAddress();
        InputStreamReader inputStreamReader = null;
        OutputStreamWriter outputStreamWriter = null;
        if (getEncoding() != null) {
            inputStreamReader = new InputStreamReader(socket.getInputStream(), getEncoding());
            outputStreamWriter = new OutputStreamWriter(socket.getOutputStream(), getEncoding());
        } else {
            inputStreamReader = new InputStreamReader(socket.getInputStream());
            outputStreamWriter = new OutputStreamWriter(socket.getOutputStream());
        }
        BufferedReader breader = new BufferedReader(inputStreamReader);
        BufferedWriter bwriter = new BufferedWriter(outputStreamWriter);
        if (password != null && !password.equals("")) {
            OutputThread.sendRawLine(this, bwriter, "PASS " + password);
        }
        String nick = this.getName();
        OutputThread.sendRawLine(this, bwriter, "NICK " + nick);
        OutputThread.sendRawLine(this, bwriter, "USER " + this.getLogin() + " 8 * :" + this.getVersion());
        _inputThread = new InputThread(this, socket, breader, bwriter);
        String line = null;
        int tries = 1;
        while ((line = breader.readLine()) != null) {
            this.handleLine(line);
            int firstSpace = line.indexOf(" ");
            int secondSpace = line.indexOf(" ", firstSpace + 1);
            if (secondSpace >= 0) {
                String code = line.substring(firstSpace + 1, secondSpace);
                if (code.equals("004")) {
                    break;
                } else if (code.equals("433")) {
                    if (_autoNickChange) {
                        tries++;
                        nick = getName() + tries;
                        OutputThread.sendRawLine(this, bwriter, "NICK " + nick);
                    } else {
                        socket.close();
                        _inputThread = null;
                        throw new NickAlreadyInUseException(line);
                    }
                } else if (code.startsWith("5") || code.startsWith("4")) {
                    socket.close();
                    _inputThread = null;
                    throw new IrcException("Could not log into the IRC server: " + line);
                }
            }
            this.setNick(nick);
        }
        this.log("*** Logged onto server.");
        socket.setSoTimeout(5 * 60 * 1000);
        _inputThread.start();
        if (_outputThread == null) {
            _outputThread = new OutputThread(this, _outQueue);
            _outputThread.start();
        }
        this.onConnect();
    }
