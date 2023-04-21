    private JComponent createLabelChannelId(final int channelIndex) {
        final JTextField label = new JTextField("" + (channelIndex + 1), 2);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setEditable(false);
        label.setBorder(null);
        label.setBackground(null);
        label.setHighlighter(null);
        final BooleanHolder holder = getContext().getPreHeating().getChannelEnabled(channelIndex);
        holder.add(new BooleanListener() {

            public void changed() {
                if (holder.getValue()) {
                    label.setBackground(Color.white);
                } else {
                    label.setBackground(null);
                }
            }
        });
        addMouseListener(label, channelIndex);
        return label;
    }
