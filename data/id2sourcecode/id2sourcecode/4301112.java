    public static void printTree(BufferedWriter writer, int indent, XMLNode node, CsvReader reader, String filepath) throws IOException {
        if (node.getFchild() != null) {
            for (int i = 0; i < indent; i++) writer.write("   ");
            writer.write("<" + node.getElementName() + ">\n");
            printTree(writer, indent + 1, node.getFchild(), reader, filepath);
            for (int i = 0; i < indent; i++) writer.write("   ");
            writer.write("</" + node.getElementName() + ">\n");
        } else {
            for (int i = 0; i < indent; i++) writer.write("   ");
            writer.write("<" + node.getElementName() + ">" + reader.get(node.getElementName()) + "</" + node.getElementName() + ">\n");
        }
        if (node.getRbrother() != null) {
            if (reader.readRecord()) printTree(writer, indent, node.getRbrother(), reader, filepath); else {
                CsvReader csvreader = new CsvReader(filepath);
                csvreader.readHeaders();
                csvreader.readRecord();
                printTree(writer, indent, node.getRbrother(), csvreader, filepath);
            }
        }
    }
