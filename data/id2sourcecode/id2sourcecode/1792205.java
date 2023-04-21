    public void interpretaMsg(Message msg) {
        if (msg.getCommand().toUpperCase().equals("QUIT")) {
            System.out.println("#");
            System.out.println("msg.getCommand()" + msg.getCommand());
            System.out.println("msg.getParameter()" + msg.getParameter());
            System.out.println("msg.getSource()" + msg.getSource());
            System.out.println("msg.getSourceName()" + msg.getSourceName());
            System.out.println("msg.getTarget()" + msg.getTarget());
            System.out.println("msg.getTargetName()" + msg.getTargetName());
            System.out.println("#");
        }
        if (msg.getCommand().toUpperCase().equals("001")) {
            JanelaPrincipal.getInstancia().trocaStatus("Conectado");
            Toolkit.getDefaultToolkit().beep();
        }
        if (msg.getCommand().toUpperCase().equals("PRIVMSG")) {
            if (msg.getTarget().isChannel()) {
                Channel canal = Client.getInstance().getChannels().getCanalPorNome(msg.getTargetName());
                String nickoutro = msg.getSourceName().split("!")[0];
                setChanged();
                notifyObservers(new Evento(canal, (nickoutro + " diz: " + msg.getParameter())));
                Toolkit.getDefaultToolkit().beep();
            }
            if (msg.getTarget().isUser()) {
                String nickoutro = msg.getSourceName().split("!")[0];
                User u = Client.getInstance().getUsers().getUsuarioPorNick(nickoutro);
                if (u == null) {
                    u = new User(nickoutro);
                    Client.getInstance().getUsers().adicionarUsuario(u);
                    PainelUsuario painelUsuario = new PainelUsuario(u);
                    JanelaPrincipal.getInstancia().addPainel(painelUsuario);
                }
                setChanged();
                notifyObservers(new Evento(u, (nickoutro + " diz :" + msg.getParameter() + "")));
                Toolkit.getDefaultToolkit().beep();
            }
        }
        if (msg.getCommand().toUpperCase().equals("JOIN")) {
            if (msg.getParameter().startsWith("#")) {
                if (!Client.getInstance().existeCanal(msg.getParameter())) {
                    Channel canal = new Channel(msg.getParameter());
                    PainelCanal painelCanal = new PainelCanal(canal);
                    painelCanal.setAberto(true);
                    this.addObserver(painelCanal);
                    JanelaPrincipal.getInstancia().addPainel(painelCanal);
                    Client.getInstance().addChannel(canal);
                    Toolkit.getDefaultToolkit().beep();
                }
            } else {
                User user = new User(msg.getParameter());
                PainelUsuario painelUsuario = new PainelUsuario(user);
                painelUsuario.setAberto(true);
                this.addObserver(painelUsuario);
                JanelaPrincipal.getInstancia().addPainel(painelUsuario);
                Client.getInstance().addUser(user);
                Toolkit.getDefaultToolkit().beep();
            }
        }
        if (msg.getCommand().toUpperCase().equals("PART")) {
            Channel canal = Client.getInstance().getChannels().getCanalPorNome(msg.getParameter());
            JanelaPrincipal.getInstancia().removePainel(canal);
            Client.getInstance().rmChannel(canal);
        }
        if (msg.getCommand().toUpperCase().equals("353")) {
            String nomeCanal = "";
            int pos = msg.getTargetName().indexOf("=");
            if (pos >= 0) {
                String[] split = msg.getTargetName().split(" = ");
                nomeCanal = split[1].trim();
            } else {
                String[] split2 = msg.getTargetName().split(" @ ");
                nomeCanal = split2[1].trim();
            }
            String[] usuariosNoCanal = msg.getParameter().split(" ");
            Channel channel = Client.getInstance().getChannels().getCanalPorNome(nomeCanal);
            channel.getUsers().removerTodos();
            for (String s : usuariosNoCanal) {
                User user = new User();
                user.setNick(s);
                channel.getUsers().adicionarUsuario(user);
            }
            setChanged();
            notifyObservers(new Evento(channel, channel.getUsers()));
            Toolkit.getDefaultToolkit().beep();
        }
        if (msg.getCommand().toUpperCase().equals("PING")) {
            Client.getInstance().escreverLinha("PONG " + msg.getTargetName());
        }
        if (msg.getCommand().toUpperCase().equals("NICK")) {
            ConfigUsuario.getInstance().setNickName(msg.getParameter());
            Client.getInstance().escreverLinha("NICK " + msg.getParameter());
            Toolkit.getDefaultToolkit().beep();
        }
        if (false) {
            System.out.println("#");
            System.out.println("msg.getCommand()" + msg.getCommand());
            System.out.println("msg.getParameter()" + msg.getParameter());
            System.out.println("msg.getSource()" + msg.getSource());
            System.out.println("msg.getSourceName()" + msg.getSourceName());
            System.out.println("msg.getTarget()" + msg.getTarget());
            System.out.println("msg.getTargetName()" + msg.getTargetName());
            System.out.println("#");
        }
    }
