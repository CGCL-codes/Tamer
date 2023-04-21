    public void ensureDynamicControls() {
        if (readControl != null) return;
        ControlRow readWrite = new ControlRow();
        readWrite.add(readControl = new ReadControl());
        readWrite.add(writeControl = new WriteControl());
        add(readWrite);
    }
