    protected void renderJSON(JSONArray jsonArray) {
        HttpServletResponse response = getEngine().getResponse();
        response.setContentType("text/javascript");
        try {
            response.setCharacterEncoding(getEngine().getEncoding());
            Writer writer = response.getWriter();
            jsonArray.write(writer);
            writer.close();
        } catch (IOException e) {
            throw new WheelException("Could not write json array to output writer. Perhaps the writer has already been initialized?", e, this);
        }
    }
