    @Override
    public void format(Reader reader, Writer writer, State state) throws WriteStreamException {
        try {
            if (state.getCommentary() != ParamsController.getNoCommentary()) {
                writer.write(symbol);
                return;
            }
            if (state.isIndentNeeded()) for (int i = 0; i < ParamsController.getIndent() * state.getIndentAmount(); i++) {
                writer.write(ParamsController.getIndentSymbol());
            }
            writer.write(symbol);
            state.setIndentNeeded(false);
        } catch (WriteStreamException e) {
            throw e;
        } finally {
        }
    }
