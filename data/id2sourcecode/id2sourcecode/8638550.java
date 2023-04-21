    public XDirectory(String name, int size, Date modify_date, boolean owns, boolean can_write, boolean can_read, boolean can_execute, String extra_information, XFileSet files) {
        super(name, size, modify_date, owns, can_write, can_read, can_execute, extra_information);
        this.files = files;
    }
