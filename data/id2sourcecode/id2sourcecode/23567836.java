    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        super.encodeBegin(context, component);
        if (!component.isRendered()) {
            return;
        }
        ResponseWriter writer = context.getResponseWriter();
        if (!isFirstSpreadSheetRendered(context)) {
            writer.startElement("script", component);
            writer.writeAttribute("language", "javascript", null);
            writer.writeAttribute("type", "text/javascript", null);
            writer.writeComment("\n" + "        var gwtspreadsheetinput_ids=[];\n" + "    ");
            writer.endElement("script");
        }
        SpreadSheet spreadSheet = (SpreadSheet) component;
        String clientId = spreadSheet.getClientId(context);
        String scriptId = getScriptId(clientId);
        String scrollXId = getScrollXInputId(clientId);
        String scrollYId = getScrollYInputId(clientId);
        String selectionRowInputId = getSelectionRowInputId(clientId);
        String selectionColInputId = getSelectionColInputId(clientId);
        SpreadSheetData data = new SpreadSheetData(spreadSheet);
        writer.startElement("div", component);
        writer.writeAttribute("id", clientId, null);
        writer.startElement("script", component);
        writer.writeAttribute("language", "javascript", null);
        writer.writeAttribute("type", "text/javascript", null);
        String spreadSheetWidth = spreadSheet.getWidth();
        if (spreadSheetWidth == null) {
            spreadSheetWidth = "500";
        }
        String spreadSheetHeight = spreadSheet.getHeight();
        if (spreadSheetHeight == null) {
            spreadSheetHeight = "400";
        }
        StringBuilder jsBuilder = new StringBuilder();
        jsBuilder.append("\n").append("            gwtspreadsheetinput_ids.push(\"").append(clientId).append("\");\n").append("            ").append(scriptId).append("_params={\n").append("                    colNames: [");
        boolean sep = false;
        for (SpreadSheetColumn col : data.getCols()) {
            if (sep) {
                jsBuilder.append(',');
            }
            sep = true;
            jsBuilder.append("\"").append(col.getTitle()).append("\"");
        }
        jsBuilder.append("],\n").append("                    colWidthContorlIds: [");
        sep = false;
        for (String controlIdString : data.getColWidthControlIds()) {
            if (sep) {
                jsBuilder.append(",\n                                         ");
            }
            sep = true;
            jsBuilder.append(" \"").append(controlIdString).append("\" ");
        }
        jsBuilder.append(" ],\n").append("                    colIds: [");
        sep = false;
        for (SpreadSheetColumn col : data.getCols()) {
            if (sep) {
                jsBuilder.append(",\n                              ");
            }
            sep = true;
            jsBuilder.append(" \"").append(col.getId()).append("\" ");
        }
        jsBuilder.append(" ],\n").append("                    scrollXId: \"").append(scrollXId).append("\",\n").append("                    scrollYId: \"").append(scrollYId).append("\",\n").append("                    selectionRow: \"").append(selectionRowInputId).append("\",\n").append("                    selectionCol: \"").append(selectionColInputId).append("\",\n").append("                    width: \"").append(spreadSheetWidth).append("\",\n").append("                    height: \"").append(spreadSheetHeight).append("\"\n").append("                };\n").append("        ");
        writer.writeComment(jsBuilder.toString());
        writer.endElement("script");
        for (int i = 0; i < data.getColCount(); i++) {
            String colWidthControlId = data.getColWidthControlIds().get(i);
            SpreadSheetColumn col = data.getCols().get(i);
            int colWidth = col.getWidth();
            if (colWidth < 0) {
                colWidth = 150;
            }
            writer.startElement("input", component);
            writer.writeAttribute("id", colWidthControlId, null);
            writer.writeAttribute("name", colWidthControlId, null);
            writer.writeAttribute("type", "hidden", null);
            writer.writeAttribute("value", "" + colWidth, null);
            writer.endElement("input");
        }
        writer.startElement("input", component);
        writer.writeAttribute("id", scrollXId, null);
        writer.writeAttribute("name", scrollXId, null);
        writer.writeAttribute("type", "hidden", null);
        writer.writeAttribute("value", "" + spreadSheet.getScrollX(), null);
        writer.endElement("input");
        writer.startElement("input", component);
        writer.writeAttribute("id", scrollYId, null);
        writer.writeAttribute("name", scrollYId, null);
        writer.writeAttribute("type", "hidden", null);
        writer.writeAttribute("value", "" + spreadSheet.getScrollY(), null);
        writer.endElement("input");
        writer.startElement("input", component);
        writer.writeAttribute("id", selectionRowInputId, null);
        writer.writeAttribute("name", selectionRowInputId, null);
        writer.writeAttribute("type", "hidden", null);
        writer.writeAttribute("value", "" + spreadSheet.getSelectionRow(), null);
        writer.endElement("input");
        writer.startElement("input", component);
        writer.writeAttribute("id", selectionColInputId, null);
        writer.writeAttribute("name", selectionColInputId, null);
        writer.writeAttribute("type", "hidden", null);
        writer.writeAttribute("value", "" + spreadSheet.getSelectionCol(), null);
        writer.endElement("input");
        encodeData(spreadSheet, data, writer, context);
        writer.endElement("div");
        if (isLastSpreadSheetOnPage(context, component)) {
            writer.startElement("script", component);
            writer.writeAttribute("language", "javascript", null);
            writer.writeAttribute("type", "text/javascript", null);
            String folder = spreadSheet.getJsFolder();
            if (folder == null) {
                folder = "com.gwtspreadsheetinput.gwt.SpreadSheet";
            }
            writer.writeAttribute("src", folder + "/com.gwtspreadsheetinput.gwt.SpreadSheet.nocache.js", null);
            writer.endElement("script");
        }
    }
