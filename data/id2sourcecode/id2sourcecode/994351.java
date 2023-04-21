    public void setCorner(DataStorage s, javax.swing.event.TreeModelEvent evt) {
        String name = ResourceManager.getResource("units");
        String units = "";
        try {
            if (evt.getTreePath().getPathCount() > 1) {
                DataGroup group = s.getGroup(0);
                if (group != null) {
                    if (orientation == CORNER_HORIZONTAL) {
                        units = s.getChannel(0, group.getXChannel()).getUnits();
                        if (units.length() > 0) name = units;
                        this.setText(name);
                    }
                    if (orientation == CORNER_VERTICAL) {
                        units = s.getChannel(0, 1).getUnits();
                        if (units.length() > 0) name = units;
                        this.setText(name);
                    }
                }
            } else {
                this.setText(name);
            }
        } catch (Exception e) {
        }
    }
