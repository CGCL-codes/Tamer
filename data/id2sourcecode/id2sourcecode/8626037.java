    public ActionEvent(java.awt.event.ActionEvent evt) {
        super(evt.getSource(), java.awt.event.ActionEvent.ACTION_PERFORMED, evt.getActionCommand());
        String command = evt.getActionCommand();
        String[] strs = command.split(" ");
        this.command = strs[0];
        if (strs.length > 1) {
            this.arguments = new String[strs.length - 1];
            for (int i = 0; i < strs.length - 1; i++) {
                this.arguments[i] = strs[i + 1];
            }
        }
    }
