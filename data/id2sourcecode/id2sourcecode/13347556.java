    public String toString() {
        return "VM option: " + getName() + " value: " + value + " " + " origin: " + origin + " " + (writeable ? "(read-only)" : "(read-write)");
    }
