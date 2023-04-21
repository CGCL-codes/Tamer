    @Override
    public void format(Reader reader, Writer writer, State state) throws WriteStreamException {
        try {
            writer.write(ParamsController.getCommentaryPart2());
        } catch (WriteStreamException e) {
            throw e;
        } finally {
        }
        if (reader.getPrev() == ParamsController.getCommentaryPart1()) {
            if (state.getCommentary() != ParamsController.getCommentary1stType()) {
                state.setCommentary(ParamsController.getCommentary2ndType());
            }
        }
    }
