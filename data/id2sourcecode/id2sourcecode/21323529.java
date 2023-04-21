    public LPButton(LPart pm, KeyListener kl) {
        super();
        thislpb = this;
        this.setLPart(pm);
        this.setPreferredSize(new Dimension(120, 20));
        this.addMouseListener(clickedThis);
        ti.addMouseListener(clickedThis);
        ti.addFocusListener(new FocusAdapter() {

            public void focusLost(FocusEvent e) {
                ti.setEditable(false);
                lp.getPart().setTitle(ti.getText());
            }
        });
        ti.addKeyListener(new KeyAdapter() {

            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == e.VK_ENTER) {
                    KeyboardFocusManager.getCurrentKeyboardFocusManager().clearGlobalFocusOwner();
                    lp.getPart().setTitle(ti.getText());
                }
            }
        });
        vol.addValueListener(new ValueListener() {

            public void valueGeneratorUpdate(int rv) {
                if (seq != null) {
                    seq.sendVol(lp.getPart().getChannel() - 1, rv);
                    lp.getPart().setVolume(rv);
                }
            }
        });
        mu.setModel(lp.getMuteModel());
        mu.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                pmge.getPMG().getAutoPlayer().record(lp, "mute", mu.isSelected());
            }
        });
        so.setModel(lp.getSoloModel());
        this.setMuteKeyListener(kl);
        build();
    }
