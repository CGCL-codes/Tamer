    public SynthesizerBox(Synthesizer synth) throws MidiUnavailableException {
        super(synth);
        this.setPreferredSize(null);
        overridePaintComponent = false;
        Soundbank soundbank = synth.getDefaultSoundbank();
        if (synth.isSoundbankSupported(soundbank)) {
            synth.loadAllInstruments(soundbank);
        } else {
            soundbank = null;
        }
        namedInstrumentMap = new TreeMap<String, Instrument>();
        for (Instrument instrument : synth.getLoadedInstruments()) {
            namedInstrumentMap.put(instrument.getName(), instrument);
        }
        this.setLayout(new BorderLayout());
        this.setBackground(Color.RED);
        this.setBorder(new TitledBorder(new LineBorder(Color.WHITE), synth.getDeviceInfo().getName(), TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.BELOW_TOP, null, Color.WHITE));
        JPanel centerPanel = new JPanel();
        centerPanel.setBackground(Color.RED);
        tableModel = new MyTableModel();
        JTable instrumentTable = new JTable(tableModel);
        Vector<Integer> channelsVector = new Vector<Integer>();
        Vector<String> instrumentVector = new Vector<String>();
        MidiChannel[] channels = synth.getChannels();
        for (int i = 0; i < channels.length; i++) {
            if (channels[i] != null) {
                channelsVector.add(i + 1);
                int bank = (channels[i].getController(0) * 128) + channels[i].getController(32);
                int program = channels[i].getProgram();
                Patch patch = new Patch(bank, program);
                channels[i].programChange(bank, program);
                instrumentVector.add(soundbank.getInstrument(patch).getName());
            }
        }
        tableModel.addColumn("Channel", channelsVector);
        instrumentTable.getColumnModel().getColumn(0).setPreferredWidth(40);
        tableModel.addColumn("Instrument", instrumentVector);
        TableColumn instrumentColumn = instrumentTable.getColumnModel().getColumn(1);
        instrumentColumn.setPreferredWidth(160);
        JComboBox instrumentComboBox = new JComboBox();
        for (String instrumentName : namedInstrumentMap.keySet()) {
            instrumentComboBox.addItem(instrumentName);
        }
        instrumentColumn.setCellEditor(new DefaultCellEditor(instrumentComboBox));
        tableModel.addTableModelListener(this);
        JScrollPane tableScrollPane = new JScrollPane(instrumentTable);
        tableScrollPane.setPreferredSize(new Dimension(200, 100));
        centerPanel.add(tableScrollPane);
        this.add(centerPanel, BorderLayout.CENTER);
        if (hasReceiver()) {
            JPanel westPanel = new JPanel();
            westPanel.setBackground(Color.RED);
            westPanel.setLayout(new GridLayout(2, 1));
            westPanel.add(new JLabel());
            BoxArrow boxArrow = new BoxArrow();
            boxArrow.setColor(Color.WHITE);
            westPanel.add(boxArrow);
            this.add(westPanel, BorderLayout.WEST);
        }
        if (hasTransmitter()) {
            JPanel eastPanel = new JPanel();
            eastPanel.setBackground(Color.RED);
            eastPanel.setLayout(new GridLayout(2, 1));
            eastPanel.add(new JLabel());
            BoxArrow boxArrow = new BoxArrow();
            boxArrow.setColor(Color.WHITE);
            eastPanel.add(boxArrow);
            this.add(eastPanel, BorderLayout.EAST);
        }
    }
