    private void renderUntergruppenSelect(final FacesContext context, final ResponseWriter writer, final String untergruppenBoxId, final Hauptgruppe firstHauptgruppe, final Untergruppe selectedUntergruppe, final boolean readonly) throws IOException {
        this.renderTableHead(writer, "Untergruppe:");
        this.renderSelectHead(writer, untergruppenBoxId, readonly);
        writer.writeAttribute("name", this.getFieldKey(context, SELECTED_UNTERGRUPPE), null);
        if (firstHauptgruppe != null) {
            for (final Untergruppe untergruppe : firstHauptgruppe.getUntergruppen()) {
                writer.startElement("option", this);
                writer.writeAttribute("value", untergruppe.getId(), null);
                if (untergruppe.equals(selectedUntergruppe)) {
                    writer.writeAttribute("selected", "selected", null);
                }
                writer.writeText(untergruppe.getUntergruppeName(), null);
                writer.endElement("option");
            }
        }
        writer.endElement("select");
        this.renderTableFooter(writer);
    }
