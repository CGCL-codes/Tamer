    public void actionPerformed(ActionEvent a) {
        String u = uname.getText();
        String p = pname.getText();
        Rectangle rect = go.getBounds();
        go.setText("Loading...");
        rect.width += 40;
        rect.x -= 20;
        go.setBounds(rect);
        go.setEnabled(false);
        worldRef = new World(this);
        commandList.add("connect " + u + " " + p);
        try {
            worldRef.connect("user.interface.org.nz", 7777);
            worldRef.login(u, p);
        } catch (IOException e) {
            System.err.println(e);
            go.setText("Login");
            rect.width -= 40;
            rect.x += 20;
            go.setBounds(rect);
            go.setEnabled(true);
            return;
        }
        if (true) {
            removeAll();
            setLayout(new BorderLayout());
            add(createGameView(), BorderLayout.CENTER);
            inputField.requestFocus();
            validate();
            TimerTask method = new TimerTask() {

                public void run() {
                    game.update();
                }
            };
            timer = new Timer();
            timer.scheduleAtFixedRate(method, delay, delay);
            worldRef.startConnectionThread();
        }
    }
