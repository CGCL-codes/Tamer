    private void refreshLayout() {
        for (Component c : getComponents()) {
            if (c instanceof JCheckBox) {
                ((JCheckBox) c).removeActionListener(listener);
            }
        }
        removeAll();
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = 0;
        for (int x = 0; x < B0rx0r.NUMBER_OF_STEREO_OUTS * 2; x++) {
            gbc.gridx = x;
            add(new JLabel("" + (x + 1)), gbc);
        }
        for (int x = 0; x < B0rx0r.NUMBER_OF_STEREO_OUTS * 2; x++) {
            gbc.gridx = x;
            for (int y = 0; y < getChannelCount(); y++) {
                gbc.gridy = y + 1;
                JCheckBox cb = new JCheckBox();
                cb.setSelected(current.getOutputMap().getOutputs(y).contains(x));
                cb.setName(x + "-" + y);
                add(cb, gbc);
                cb.addActionListener(listener);
            }
        }
        invalidate();
        revalidate();
        repaint();
    }
