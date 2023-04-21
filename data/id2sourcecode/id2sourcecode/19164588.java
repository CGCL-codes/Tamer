    private void writeFirmennamen(final boolean readonly, final ResponseWriter writer) throws IOException {
        writer.startElement("td", null);
        this.renderSelectHead(writer, this.createFirmenBoxId(this.id), readonly);
        writer.writeAttribute("onchange", "select_change('" + this.createFirmenBoxId(this.id) + "','" + this.createSteuerNrBoxId(this.id) + "');", null);
        for (final Firmennamen firmenname : this.firmennamen) {
            writer.startElement("option", this);
            writer.writeAttribute("value", firmenname.getId(), null);
            writer.writeText(firmenname.getFirmaName(), null);
            writer.endElement("option");
        }
        writer.endElement("select");
        writer.endElement("td");
    }
