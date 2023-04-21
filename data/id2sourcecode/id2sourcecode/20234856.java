    public KeyboardDrumsDemo(Synthesizer synth) {
        super("Drums");
        channel = synth.getChannels()[9];
        addKeyListener(new KeyAdapter() {

            public void keyPressed(KeyEvent e) {
                int key = e.getKeyCode();
                if (key >= 35 && key <= 81) {
                    channel.noteOn(key, velocity);
                }
            }

            public void keyReleased(KeyEvent e) {
                int key = e.getKeyCode();
                if (key >= 35 && key <= 81) channel.noteOff(key);
            }
        });
        addMouseMotionListener(new MouseMotionAdapter() {

            public void mouseMoved(MouseEvent e) {
                velocity = e.getX();
            }
        });
    }
