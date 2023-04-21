    protected void renderNode(TreeView.Node node, PrintWriter writer, int level, int openNodeDepth, String formPrefix) {
        writer.print("    <tr");
        if (getSelectedNode() == node) writer.print(getSelectedNodeBackgroundColor());
        writer.println(">");
        TreeView.Node[] taxonomy = new Node[level];
        taxonomy[level - 1] = node;
        for (int i = level - 2; i >= 0; i--) taxonomy[i] = taxonomy[i + 1].getParent();
        int colSpans = 0;
        for (int i = 1; i < taxonomy.length; i++) {
            if (i == level - 1) {
                if (colSpans > 0) {
                    writer.print("        <td");
                    if (getSelectedNode() == node) writer.print(getSelectedNodeBackgroundColor());
                    if (colSpans > 1) writer.print(" colspan='" + colSpans + "'");
                    writer.println("></td>");
                    colSpans = 0;
                }
                if (node.getChildren().size() > 0) {
                    writer.print("        <td");
                    if (getSelectedNode() == node) writer.print(getSelectedNodeBackgroundColor());
                    writer.print(" align=center>");
                    writer.print("<input type='image' src='");
                    writer.print(getImagePath());
                    if (node.isOpened()) writer.print("open_arrow.png"); else writer.print("closed_arrow.png");
                    writer.print("' name='" + formPrefix + (node.isOpened() ? "close" : "open") + "." + node.resolvePath() + "'>");
                    writer.println("</td>");
                } else {
                    colSpans++;
                }
            } else colSpans++;
        }
        if (colSpans > 0) {
            writer.print("        <td");
            if (getSelectedNode() == node) writer.print(getSelectedNodeBackgroundColor());
            if (colSpans > 1) writer.print(" colspan='" + colSpans + "'");
            writer.println("></td>");
            colSpans = 0;
        }
        writer.print("        <td");
        if (getSelectedNode() == node) writer.print(getSelectedNodeBackgroundColor());
        writer.print(">");
        if (((Node) node).isSelectable()) writer.print("<input type='image' name='" + formPrefix + "select." + node.resolvePath() + "' src='"); else writer.print("<img src='");
        writer.print(node.getIconPath());
        writer.println("'></td>");
        writer.print("        <td width=100%");
        if (getSelectedNode() == node) writer.print(getSelectedNodeBackgroundColor());
        int spanToEnd = openNodeDepth - taxonomy.length + 2;
        if (spanToEnd > 0) writer.print(" colspan='" + String.valueOf(spanToEnd) + "'");
        writer.print("><font size='-2'");
        if (getSelectedNode() == node) writer.print(" color='#ffffff'");
        writer.print(">&nbsp;");
        writer.print(node.getText());
        writer.println("&nbsp;</font></td>");
        writer.println("    </tr>");
        if (node.isOpened()) for (int i = 0; i < node.getChildren().size(); i++) renderNode(node.getChildren().get(i), writer, level + 1, openNodeDepth, formPrefix);
    }
