    private JPanel getProgramButtonPanel() {
        if (programButtonPanel == null) {
            programButtonPanel = new JPanel();
            programButtonPanel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
            List serviceList = box.getChannelManager().getServicesSortedByProgramming();
            GridLayout gridLayout = new GridLayout();
            gridLayout.setColumns(3);
            gridLayout.setRows((serviceList.size() / 3) + 1);
            programButtonPanel.setLayout(gridLayout);
            for (Iterator iter = serviceList.iterator(); iter.hasNext(); ) {
                final Channel channel = (Channel) iter.next();
                JButton b = new JButton();
                Dimension dimension = new Dimension(59, 42);
                b.setMaximumSize(dimension);
                b.setSize(dimension);
                b.setMinimumSize(dimension);
                b.setPreferredSize(dimension);
                b.setIcon(channel.getLogo());
                b.setBackground(Color.WHITE);
                b.setToolTipText(channel.getName().getDisplayString());
                b.addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent e) {
                        lockGui();
                        remoteControl.switchToStation(channel.getPosition());
                        unlockGui();
                    }
                });
                programButtonPanel.add(b);
            }
        }
        return programButtonPanel;
    }
