    public void addReader(Component _readerId, Component _readerDescription, Component _readerGridRows) {
        Textbox readerIdTextbox = (Textbox) _readerId;
        Textbox readerDescriptionTextbox = (Textbox) _readerDescription;
        ReaderVo readerVoZul = new ReaderVo(readerIdTextbox.getValue(), readerDescriptionTextbox.getValue());
        this.getReaderDao().write(readerVoZul);
        this.populate((Rows) _readerGridRows);
    }
