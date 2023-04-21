    private javax.swing.JPanel getJContentPane() {
        if (jContentPane == null) {
            jContentPane = new javax.swing.JPanel();
            jContentPane.setLayout(new java.awt.BorderLayout());
            jContentPane.add(getJPanel(), java.awt.BorderLayout.NORTH);
            jContentPane.add(getJPanel1(), java.awt.BorderLayout.SOUTH);
            jContentPane.add(getChannelPropertyPanel(), java.awt.BorderLayout.CENTER);
        }
        return jContentPane;
    }
