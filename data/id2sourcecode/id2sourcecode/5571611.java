    public Component getTableCellRendererComponent(final JTable table, final Object value, final boolean isSelected, final boolean hasFocus, final int row, final int column) {
        label.setText("");
        Component renderer = label;
        if (value instanceof Attribute) {
            Attribute attribute = (Attribute) value;
            String name = attribute.getDefinition().getName();
            if (Attribute.INTENSITY.equals(name)) {
                Level level = getLevel(attribute, 0);
                label.setText("" + level.getIntValue() + "%");
            } else if (Attribute.RGB.equals(name)) {
                if (attribute.getChannelCount() == 3) {
                    int red = getDmxValue(attribute, 0);
                    int green = getDmxValue(attribute, 1);
                    int blue = getDmxValue(attribute, 2);
                    Color color = new Color(red, green, blue);
                    colorBox.setColor(color);
                    renderer = colorBox;
                }
            } else if (Attribute.CMY.equals(name)) {
                int red = 255 - getDmxValue(attribute, 0);
                int green = 255 - getDmxValue(attribute, 1);
                int blue = 255 - getDmxValue(attribute, 2);
                Color color = new Color(red, green, blue);
                colorBox.setColor(color);
                renderer = colorBox;
            } else {
                if (attribute.getChannelCount() == 1) {
                    if (attribute.getDefinition().getValueCount() > 0) {
                        int dmxValue = getDmxValue(attribute, 0);
                        String valueName = attribute.getDefinition().getValueOf(dmxValue);
                        label.setText(valueName);
                    }
                }
            }
        }
        return renderer;
    }
