    private void tagTextboxOnBlur(Textbox readerNameTextbox) {
        Row row = (Row) readerNameTextbox.getParent();
        Label readerIdLabel = (Label) row.getFirstChild();
        ReaderVo readerVo = new ReaderVo();
        readerVo.setReaderId(readerIdLabel.getValue());
        readerVo.setReaderName(readerNameTextbox.getValue());
        if (readerVo.getReaderName() == null) {
            readerVo.setReaderName("");
        }
        logger.info("store reader: " + readerVo.getReaderId() + " " + readerVo.getReaderName());
        getReaderDao().write(readerVo);
    }
