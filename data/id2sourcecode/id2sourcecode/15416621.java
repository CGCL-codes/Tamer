    @Override
    public void format(Reader reader, Writer writer, State state) throws WriteStreamException {
        if (state.getCommentary() == ParamsController.getNoCommentary() || symbol != ParamsController.getLineFeed()[ParamsController.getLineFeed().length - 1]) return;
        try {
            writer.write(ParamsController.getLineFeed());
        } catch (WriteStreamException e) {
            throw e;
        } finally {
        }
        if (state.getCommentary() == ParamsController.getCommentary1stType()) state.setCommentary(ParamsController.getNoCommentary());
        state.setIndentNeeded(true);
    }
