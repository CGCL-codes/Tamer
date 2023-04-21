    private void fillSelection() {
        DataFrame ifr = (DataFrame) desktop.getActiveFrame();
        DataStorage source = ifr.getDataContent().getView().getStorage();
        Object[][] dat = new Object[source.getGroupsSize()][3];
        for (int i = 0; i < source.getGroupsSize(); i++) {
            dat[i][0] = "" + i;
            try {
                if (!source.getChannel(i, xchanTextField.getValue()).getAttribute().isNormal()) throw new NullPointerException();
                dat[i][1] = source.getChannel(i, xchanTextField.getValue()).getName();
            } catch (Exception e) {
                dat[i][1] = "--";
            }
            try {
                if (!source.getChannel(i, ychanTextField.getValue()).getAttribute().isNormal()) throw new NullPointerException();
                dat[i][2] = source.getChannel(i, ychanTextField.getValue()).getName();
            } catch (Exception e) {
                dat[i][2] = "--";
            }
        }
        selectionTable.setModel(new DefaultTableModel(dat, new String[] { ResourceManager.getResource("Sweep"), ResourceManager.getResource("X-Axis"), ResourceManager.getResource("Y-Axis") }));
    }
