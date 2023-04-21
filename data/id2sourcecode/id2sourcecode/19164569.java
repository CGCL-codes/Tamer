    @Override
    public void encodeEnd(final FacesContext context) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        writer.write("</td>");
        writer.write("<td class=\"value\">");
        writer.write("<table  border=\"0\">");
        writer.startElement("tbody", null);
        final boolean readonly = this.bestimmeReadonly();
        this.renderErsteZeile(writer);
        writer.startElement("tr", null);
        this.writeFirmennamen(readonly, writer);
        this.writeSteuernummern(readonly, writer);
        if (!readonly) {
            this.writeButtons(writer, this.id);
        }
        writer.endElement("tr");
        writer.endElement("tbody");
        writer.endElement("table");
        this.renderTableFooter(writer);
    }
