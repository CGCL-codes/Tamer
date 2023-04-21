    public SettingsShowChannelGUI(ConsoleInquiry c, ActionInterpreter ai) {
        _console = c;
        _actInt = ai;
        ConsoleSettings cs = _console.getSettings();
        setLayout(new GridBagLayout());
        GridBagConstraints cons = new GridBagConstraints();
        cons.fill = GridBagConstraints.BOTH;
        cons.gridx = 0;
        cons.gridy = 0;
        add(new JLabel("Channels per Line"), cons);
        cons.gridx = 1;
        add(_perLine = new JTextField(Integer.toString(_initPerLine = cs.getChannelsPerLine())), cons);
        cons.gridx = 0;
        cons.gridy = 1;
        add(new JLabel("Channels per Horizontal Group"), cons);
        cons.gridx = 1;
        add(_hGrouping = new JTextField(Integer.toString(_initHGrouping = cs.getChannelGrouping())), cons);
        cons.gridx = 0;
        cons.gridy = 2;
        add(new JLabel("Channels per Vertical Group"), cons);
        cons.gridx = 1;
        add(_vGrouping = new JTextField(Integer.toString(_initVGrouping = cs.getLineGrouping())), cons);
        cons.gridx = 0;
        cons.gridy = 3;
        cons.gridx = 1;
        add(_apply = new JButton("Apply"), cons);
        _apply.addActionListener(this);
    }
