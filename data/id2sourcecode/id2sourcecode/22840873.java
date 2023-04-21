    @Override
    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        if (!component.isRendered()) {
            return;
        }
        super.encodeEnd(context, component);
        UIComponent parent = component.getParent();
        SyncValue syncValue = (SyncValue) component;
        UIComponent withComponent = context.getViewRoot().findComponent(syncValue.getWith());
        if (withComponent == null) {
            logger.error("SyncValue: component not found: {}", syncValue.getWith());
            return;
        }
        final ResponseWriter writer = context.getResponseWriter();
        writer.startElement("script", syncValue);
        writer.writeAttribute("type", "text/javascript", syncValue.getClientId());
        writer.write("$(document).ready(function() {" + NEW_LINE);
        writer.write(updateValueBinding(parent.getClientId(), withComponent.getClientId()) + NEW_LINE);
        writer.write(updateValueBinding(withComponent.getClientId(), parent.getClientId()) + NEW_LINE);
        writer.write("});" + NEW_LINE);
        writer.endElement("script");
    }
