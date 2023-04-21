    @Override
    public void init() {
        setBackground(Color.BLACK);
        JWondrousMachine.MACHINES = new Vector<JWondrousMachine>();
        JMIDI.load();
        JWondrousMachine.MAX_CHAN = JMIDI.getChannels().length;
        JWondrousMachine.MAX_INST = JMIDI.getInstruments().length;
        BUTT_NEW = new Button("Create New Machine");
        BUTT_NEW.addActionListener(this);
        add(BUTT_NEW);
    }
