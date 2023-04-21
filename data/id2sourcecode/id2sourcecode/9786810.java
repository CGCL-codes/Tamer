    private Panel getPanel4() {
        Panel panel4 = new Panel();
        panel4.setLayout(new FlowLayout());
        panel4.setMaximumSize(new Dimension(n01, n02));
        panel4.setBounds(new Rectangle(n19, n20, n14, n22));
        panel4.add(getChannelsButton(), null);
        panel4.add(getUsersButton(), null);
        return panel4;
    }
