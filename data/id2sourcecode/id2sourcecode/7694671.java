    public static boolean showDialog(Component parentComponent) {
        gr_gw.setText("Group read/group write");
        gr_ow.setText("Group read/owner write");
        or_ow.setText("Owner read/owner write");
        wr_gw.setText("All read/group write");
        JPanel panel1 = new JPanel();
        panel1.setLayout(new BoxLayout(panel1, BoxLayout.Y_AXIS));
        group = new ButtonGroup();
        group.add(gr_gw);
        group.add(gr_ow);
        group.add(or_ow);
        group.add(wr_gw);
        gr_gw.setSelected(true);
        panel1.add(wr_gw);
        panel1.add(Box.createHorizontalStrut(30));
        panel1.add(gr_gw);
        panel1.add(Box.createHorizontalStrut(30));
        panel1.add(gr_ow);
        panel1.add(Box.createHorizontalStrut(30));
        panel1.add(or_ow);
        panel1.add(Box.createHorizontalStrut(30));
        return OkCancelDialog.showDialog(parentComponent, panel1, "Choose access mode");
    }
