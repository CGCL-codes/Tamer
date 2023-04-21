    private long failWrite() {
        validator.fail("attempt to write to read-only instance [this]: " + this);
        return 0;
    }
