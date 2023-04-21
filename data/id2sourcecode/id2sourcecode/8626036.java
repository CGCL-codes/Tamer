    public ActionEvent(Object source, String command) {
        super(source, java.awt.event.ActionEvent.ACTION_PERFORMED, command, 0, 0);
        String[] strs = command.split(" ");
        this.command = strs[0];
        if (strs.length > 1) {
            this.arguments = new String[strs.length - 1];
            for (int i = 0; i < strs.length - 1; i++) {
                this.arguments[i] = strs[i + 1];
            }
        }
    }
