    @Override
    public void format(Reader reader, Writer writer, State state) throws WriteStreamException {
        if (state.getCommentary() != ParamsController.getNoCommentary()) return;
        try {
            for (int i = 0; i < ParamsController.getIndent() * (state.getIndentAmount() - 1); i++) writer.write(ParamsController.getIndentSymbol());
            writer.write(ParamsController.getIndentFinisher());
            writer.write(ParamsController.getLineFeed());
            state.decIndentAmount();
            state.setIndentNeeded(true);
        } catch (WriteStreamException e) {
            throw e;
        } finally {
        }
    }
