    public Teacher(Socket socket) throws IOException, ClassNotFoundException {
        out = new ObjectOutputStream(socket.getOutputStream());
        in = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
        studentName = (String) in.readObject();
        setupUI();
        createReaderThread();
        timer = createScreenShotThread();
        writer = createWriterThread();
        addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent e) {
                timer.cancel();
            }
        });
        addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent e) {
                try {
                    out.close();
                } catch (IOException ex) {
                }
                try {
                    in.close();
                } catch (IOException ex) {
                }
            }
        });
        System.out.println("finished connecting to " + socket);
    }
