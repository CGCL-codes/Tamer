    private void writeSteuernummern(final boolean readonly, final ResponseWriter writer) throws IOException {
        writer.startElement("td", null);
        this.renderSelectHead(writer, this.createSteuerNrBoxId(this.id), readonly);
        writer.writeAttribute("onchange", "select_change('" + this.createSteuerNrBoxId(this.id) + "','" + this.createFirmenBoxId(this.id) + "');", null);
        for (final Firmennamen firmenname : this.firmennamen) {
            writer.startElement("option", this);
            writer.writeAttribute("value", firmenname.getId(), null);
            writer.writeText(firmenname.getSteuernummer(), null);
            writer.endElement("option");
        }
        writer.endElement("select");
        writer.endElement("td");
    }
