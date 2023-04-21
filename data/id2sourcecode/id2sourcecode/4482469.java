    @Override
    public void encodeBegin(final FacesContext context) throws java.io.IOException {
        if (!this.isRendered()) {
            return;
        }
        final List<Hauptgruppe> hauptgruppen = this.bestimmeHauptgruppen(context);
        boolean readonly = false;
        final Object readOnlyValue = this.getValue(context, ATTRIBUTE_READONLY);
        if (readOnlyValue != null) {
            readonly = (Boolean) readOnlyValue;
        }
        final ResponseWriter writer = context.getResponseWriter();
        final String id = this.getId() == null ? context.getViewRoot().createUniqueId() : this.getId();
        final String hauptgruppenBoxId = id + ":hauptgruppe";
        final String untergruppenBoxId = id + ":untergruppe";
        final Untergruppe selectedUnterguppe = this.bestimmeSelectedUntergruppe(context, hauptgruppen);
        final Hauptgruppe selectedHauptgruppe;
        if (selectedUnterguppe == null) {
            selectedHauptgruppe = null;
        } else {
            selectedHauptgruppe = selectedUnterguppe.getHauptgruppe();
        }
        this.renderHauptgruppenSelect(context, hauptgruppen, selectedHauptgruppe, writer, hauptgruppenBoxId, readonly);
        this.renderUntergruppenSelect(context, writer, untergruppenBoxId, selectedHauptgruppe, selectedUnterguppe, readonly);
        this.renderJavaScript(hauptgruppenBoxId, untergruppenBoxId, hauptgruppen, writer);
    }
