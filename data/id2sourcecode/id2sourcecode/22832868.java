    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        super.getListCellRendererComponent(list, value, index, isSelected && !(value.toString().equals(Globals.getThisPlayer_loginName())), cellHasFocus);
        setFont(new Font(Settings.getNameStyle(), Font.PLAIN, 14));
        setToolTipText("<html><xmp>" + value.toString());
        setText(value.toString());
        if (Settings.getColorizeBody()) {
            setForeground(Settings.getForegroundColor());
            if (isSelected && !(value.toString().equals(Globals.getThisPlayer_loginName()))) {
                setBackground(Settings.getSelectionColor());
            } else {
                setBackground(Settings.getBackgroundColor());
            }
        }
        setHorizontalAlignment(LEFT);
        setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
        Channel c = model.getChannel(value.toString());
        if (c != null) {
            setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));
            setFont(new Font(Settings.getNameStyle(), Font.BOLD, 14));
        } else {
            if (model.isMuted(value.toString())) {
                setIcon(mutedIcon);
            } else {
                if (model.isTalking(value.toString())) {
                    setIcon(talkingIcon);
                } else {
                    setIcon(emptyIcon);
                }
            }
        }
        return this;
    }
