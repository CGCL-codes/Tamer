    @Override
    public void encodeBegin(FacesContext context) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        String variableName = readVariableName();
        if (variableName.equals("")) {
            return;
        }
        writer.startElement("script", null);
        writer.writeAttribute("type", "text/javascript", null);
        writer.writeAttribute("language", "javascript", null);
        writer.write("/* Component id = " + this.getId() + "*/");
        writer.write("var " + variableName + " = ");
        if (readQuoted()) {
            writer.write("\"");
        }
        writer.write(this.readValue());
        if (readQuoted()) {
            writer.write("\"");
        }
        writer.write(";");
    }
