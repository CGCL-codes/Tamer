    @Override
    public void format(Reader reader, Writer writer, State state) throws WriteStreamException {
        if (state.getCommentary() != ParamsController.getNoCommentary()) return;
        try {
            if (state.isIndentNeeded()) for (int i = 0; i < state.getIndentAmount() * ParamsController.getIndent(); i++) {
                writer.write(ParamsController.getIndentSymbol());
            }
            writer.write(ParamsController.getIndentStarter());
            writer.write(ParamsController.getLineFeed());
            state.incIndentAmount();
            state.setIndentNeeded(true);
        } catch (WriteStreamException e) {
            throw e;
        } finally {
        }
    }
