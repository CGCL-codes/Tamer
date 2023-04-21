    @Override
    public void format(Reader reader, Writer writer, State state) throws WriteStreamException {
        if (state.getCommentary() != ParamsController.getNoCommentary()) {
            writer.write(ParamsController.getSpace());
            return;
        }
        try {
            if (reader.getPrev() != ParamsController.getLineFeed()[1] && reader.getPrev() != ParamsController.getSpace()) writer.write(ParamsController.getSpace());
        } catch (WriteStreamException e) {
            throw e;
        } finally {
        }
    }
