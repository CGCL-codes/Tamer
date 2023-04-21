    @Override
    public void format(Reader reader, Writer writer, State state) throws WriteStreamException {
        if (state.getCommentary() != ParamsController.getNoCommentary()) return;
        try {
            writer.write(ParamsController.getEndOfLine());
            writer.write(ParamsController.getLineFeed());
            state.setIndentNeeded(true);
        } catch (WriteStreamException e) {
            throw e;
        } finally {
        }
    }
