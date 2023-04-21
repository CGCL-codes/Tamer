    @Override
    public void format(Reader reader, Writer writer, State state) throws WriteStreamException {
        try {
            writer.write(ParamsController.getCommentaryPart1());
        } catch (WriteStreamException e) {
            throw e;
        } finally {
        }
        if (reader.getPrev() == ParamsController.getCommentaryPart1()) {
            if (state.getCommentary() != ParamsController.getCommentary2ndType()) {
                state.setCommentary(ParamsController.getCommentary1stType());
            }
        } else if (reader.getPrev() == ParamsController.getCommentaryPart2()) {
            if (state.getCommentary() != ParamsController.getCommentary1stType()) {
                state.setCommentary(ParamsController.getNoCommentary());
            }
        }
    }
