    public XFile(String name, long size, Date modify_date, boolean owns, boolean can_read, boolean can_write, boolean can_execute, String extra_information) {
        setName(name);
        setSize(size);
        setModifyDate(modify_date);
        setIsOwner(owns);
        setCanWrite(can_write);
        setCanRead(can_read);
        setCanExecute(can_execute);
        setExtraInformation(extra_information);
    }
