    protected void initialise() {
        this.removeAll();
        ActionListener al = new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == inst) {
                    part.setInstrument(((NumberTextField) e.getSource()).getValue());
                } else if (e.getSource() == chan) {
                    part.setChannel(((NumberTextField) e.getSource()).getValue());
                } else if (e.getSource() == titleField) part.setTitle(titleField.getText());
            }
        };
        inst = new NumberTextField(0, 1000, part.getInstrument());
        inst.addActionListener(al);
        chan = new NumberTextField(1, 96, part.getChannel());
        chan.addActionListener(al);
        titleField = new JTextField(part.getTitle());
        JLabel ilab = new JLabel(" inst");
        JLabel clab = new JLabel(" chan");
        GB.add(this, 1, 0, ilab, 1, 1);
        GB.add(this, 2, 0, inst, 1, 1);
        GB.add(this, 3, 0, clab, 1, 1);
        GB.add(this, 4, 0, chan, 1, 1);
        makeLabView(scope, scopeBox, this.sctt, this.scdim);
        GB.add(this, 5, 0, scopeBox, 3, 1);
        makeLabView(quantise, quantBox, this.qutt, this.qudim);
        GB.add(this, 8, 0, quantBox, 2, 1);
        makeLabView(shuffle, this.shuBox, this.shutt, this.shudim);
        GB.add(this, 10, 0, shuBox, 1, 1);
        pnpanel.setPart(part);
        GB.add(this, 0, 1, pnpanel, 15, 10);
    }
