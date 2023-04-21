    public void testFLV() {
        log.debug("testFLV");
        try {
            ITagReader reader = null;
            if (flv != null) {
                reader = flv.getReader();
            }
            if (reader == null) {
                file.seek(0);
                reader = new FLVReader(file.getChannel());
            }
            log.trace("reader: {}", reader);
            log.debug("Has more tags: {}", reader.hasMoreTags());
            ITag tag = null;
            while (reader.hasMoreTags()) {
                tag = reader.readTag();
                log.debug("\n{}", tag);
            }
        } catch (IOException e) {
            log.warn("", e);
        }
    }
