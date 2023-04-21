    private static void createColorPicker(ResponseWriter writer, InputColor inputColor) throws IOException {
        writer.write("            this.dialog.renderEvent.subscribe(function() {\n");
        writer.write("				if (!this.picker) { " + "//make sure that we haven't already created our Color Picker\n");
        writer.write("					this.picker = new YAHOO.widget.ColorPicker" + "('" + InputColorDialogRendererHelper.getPanelColorPickerID(inputColor) + "', {\n");
        writer.write("						container: this.dialog,\n");
        writer.write("						showcontrols: true,  \n");
        writer.write("						showhexcontrols: true, \n");
        writer.write("						showhsvcontrols: true  \n");
        writer.write("					});\n");
    }
