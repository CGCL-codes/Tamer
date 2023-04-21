    public void encodeBegin(FacesContext context) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        JSFUtility.renderScriptOnce(writer, this, context);
        String[] jsfiles = { "plugins/flexigrid/flexigrid.js" };
        String[] cssfiles = { "plugins/flexigrid/css/flexigrid.css" };
        JSFUtility.renderScriptOnce(writer, context, this, jsfiles, cssfiles, REQUEST_MAP_FLEXIGRID);
        writer.startElement("script", this);
        writer.writeAttribute("type", "text/javascript", null);
        Map attr = this.getAttributes();
        writer.writeText("$(document).ready(function(){\n", null);
        String clientId = getClientId(context);
        clientId = clientId.replace(":", "\\\\:");
        writer.writeText("$(\"#" + clientId + "\").flexigrid({", null);
        boolean commaNeeded = JSFUtility.writeJSObjectOptions(writer, attr, Grid.class);
        innerRenderJSOptions(writer, commaNeeded);
        writer.writeText("});\n", null);
        writer.writeText("});", null);
        writer.endElement("script");
        writer.startElement("table", this);
        writer.writeAttribute("id", getClientId(context), null);
        writer.writeAttribute("style", "display:none;", null);
        if (attr.get(DATALIST) != null) {
            List<UIComponent> children = (List<UIComponent>) getChildren();
            List objs = (List) attr.get(DATALIST);
            for (Object obj : objs) {
                writer.startElement("tr", this);
                for (UIComponent cm : children) {
                    writer.startElement("td", cm);
                    Method method;
                    try {
                        String field = (String) cm.getAttributes().get(Column.NAME);
                        field = field.substring(0, 1).toUpperCase() + field.substring(1);
                        method = obj.getClass().getMethod("get" + field, null);
                        Object data = method.invoke(obj, (new Object[0]));
                        writer.writeText(data, null);
                    } catch (Exception e) {
                    }
                    writer.endElement("td");
                }
                writer.endElement("tr");
            }
        }
        writer.endElement("table");
        writer.flush();
    }
