    public void writeAll() {
        if (getReadOnly()) log.error("unexpected write operation when readOnly is set");
        writingChanges = false;
        amWriting = true;
        continueWrite();
    }
