        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            TVProgramme programme = (TVProgramme) value;
            String progInfo = programme.getChannel().getDisplayName() + ' ' + DateFormat.getInstance().format(new Date(programme.getStart())) + ' ' + programme.getTitle();
            setText(progInfo);
            if (isSelected) {
                setBackground(list.getSelectionBackground());
                setForeground(list.getSelectionForeground());
            } else {
                setBackground(list.getBackground());
                setForeground(list.getForeground());
            }
            setEnabled(list.isEnabled());
            setFont(list.getFont());
            setOpaque(true);
            return this;
        }
