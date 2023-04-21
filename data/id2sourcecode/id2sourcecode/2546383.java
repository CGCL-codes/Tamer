    public ChatPanel(Chatter[] chatters) {
        current = null;
        frame = null;
        readField = new JTextField("       read only     ");
        readField.setEditable(false);
        writeField = new JTextField("      write/read     ");
        apply = new JButton("Apply");
        apply.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (current != null) current.sendMessage(writeField.getText());
            }
        });
        String[] tmp = new String[chatters.length];
        map = new HashMap<String, Chatter>();
        for (int i = 0; i < chatters.length; i++) {
            tmp[i] = chatters[i].getName();
            map.put(chatters[i].getName(), chatters[i]);
        }
        combo = new JComboBox(tmp);
        combo.addItemListener(this);
        current = map.get((String) combo.getItemAt(0));
        readField.setText(current.getLastMessage());
        add(readField);
        add(writeField);
        add(combo);
        add(apply);
    }
