    @Override
    public String[] getFormats() {
        return ArrayUtils.unique(String.class, reader.getFormats(), writer.getFormats());
    }
