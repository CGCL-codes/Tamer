    private void renderHauptgruppenSelect(final FacesContext context, final List<Hauptgruppe> hauptgruppen, final Hauptgruppe selectedHauptgruppe, final ResponseWriter writer, final String hauptgruppenBoxId, final boolean readonly) throws IOException {
        this.renderTableHead(writer, "Hauptgruppe:");
        this.renderSelectHead(writer, hauptgruppenBoxId, readonly);
        writer.writeAttribute("onchange", "hauptgruppe_wahl();", null);
        for (final Hauptgruppe hauptgruppe : hauptgruppen) {
            writer.startElement("option", this);
            writer.writeAttribute("value", hauptgruppe.getId(), null);
            if (hauptgruppe.equals(selectedHauptgruppe)) {
                writer.writeAttribute("selected", "selected", null);
            }
            writer.writeText(hauptgruppe.getHauptgruppeName(), null);
            writer.endElement("option");
        }
        writer.endElement("select");
        this.renderTableFooter(writer);
    }
