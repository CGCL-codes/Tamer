    public Breakpoint(String what, boolean read, boolean write) {
        this.file = null;
        this.view = null;
        if (read) {
            if (write) {
                this.options = "-a";
                this.when = "access";
            } else {
                this.options = "-r";
                this.when = "read";
            }
        } else {
            this.options = "";
            this.when = "write";
        }
        this.what = what;
        initialize();
        BreakpointList.getInstance().add(this);
    }
