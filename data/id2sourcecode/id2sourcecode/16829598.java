    public Component getTableCellRendererComponent(final JTable table, final Object value, final boolean isSelected, final boolean hasFocus, final int row, final int column) {
        setText(value == null ? null : value.toString());
        if (value instanceof User) {
            setIcon(ResourceUtil.userIcon);
        } else if (value instanceof Group) {
            setIcon(ResourceUtil.groupIcon);
        } else if (value instanceof Path) {
            setIcon(null);
        } else if (value instanceof Repository) {
            setIcon(null);
        } else if (value instanceof String) {
            final String valueString = (String) value;
            if (valueString.equals(ResourceUtil.getString("accesslevel.readonly"))) {
                setIcon(ResourceUtil.readOnlyIcon);
            } else if (valueString.equals(ResourceUtil.getString("accesslevel.readwrite"))) {
                setIcon(ResourceUtil.readWriteIcon);
            } else if (valueString.equals(ResourceUtil.getString("accesslevel.denyaccess"))) {
                setIcon(ResourceUtil.denyAccessIcon);
            } else {
                setIcon(null);
            }
        } else {
            setIcon(null);
        }
        if (isSelected && hasFocus) {
            setBackground(table.getSelectionBackground());
            setForeground(table.getSelectionForeground());
            setBorder(new LineBorder(table.getSelectionBackground()));
        } else if (isSelected && !hasFocus) {
            setBackground(table.getBackground());
            setForeground(table.getSelectionForeground());
            setBorder(new LineBorder(table.getSelectionBackground()));
        } else if (!isSelected && hasFocus) {
            setBackground(table.getBackground());
            setForeground(table.getForeground());
            setBorder(new LineBorder(table.getSelectionForeground()));
        } else {
            setBackground(table.getBackground());
            setForeground(table.getForeground());
            setBorder(new LineBorder(table.getBackground()));
        }
        setEnabled(table.isEnabled());
        setFont(table.getFont());
        setOpaque(true);
        return this;
    }
