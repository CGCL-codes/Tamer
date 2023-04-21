    public static void writeConfig(Config aConfig, String outputFileName) {
        System.out.println("writeConfig() called");
        try {
            Document document = DocumentHelper.createDocument();
            Element root = document.addElement("config");
            Element parElt = null;
            parElt = new DefaultElement("param");
            parElt.addAttribute("name", "serverAddress");
            parElt.addAttribute("value", aConfig.getRbnbServerAddress());
            root.add(parElt);
            parElt = new DefaultElement("param");
            parElt.addAttribute("name", "serverPort");
            parElt.addAttribute("value", aConfig.getRbnbServerPort());
            root.add(parElt);
            parElt = new DefaultElement("param");
            parElt.addAttribute("name", "sampleIntParam");
            parElt.addAttribute("value", Integer.toString(aConfig.getSampleIntParam()));
            root.add(parElt);
            List tables = aConfig.getTableConfigsAsList();
            for (Iterator tableIter = tables.iterator(); tableIter.hasNext(); ) {
                TableConfig aTable = (TableConfig) tableIter.next();
                Element tabElt = new DefaultElement("table");
                tabElt.addAttribute("name", aTable.getName());
                List columns = aTable.getTableConfigColumnsAsList();
                for (Iterator colIter = columns.iterator(); colIter.hasNext(); ) {
                    TableConfigColumn aCol = (TableConfigColumn) colIter.next();
                    Element colElt = new DefaultElement("column");
                    colElt.addAttribute("name", aCol.getName());
                    if (aCol.getChannelMapping() != null) colElt.addAttribute("channelMapping", aCol.getChannelMapping());
                    if (aCol.getDataValue() != null) {
                        colElt.addAttribute("dataValue", aCol.getDataValue().getFirst());
                    }
                    if (aCol.getType() != null) {
                        colElt.addAttribute("type", aCol.getType().getFirst());
                    }
                    tabElt.add(colElt);
                }
                root.add(tabElt);
            }
            OutputFormat format = OutputFormat.createPrettyPrint();
            XMLWriter writer = new XMLWriter(new FileWriter(outputFileName), format);
            writer.write(document);
            writer.close();
            System.out.println("Wrote the config to file: " + outputFileName);
            writer = new XMLWriter(System.out, format);
            writer.write(document);
        } catch (Exception e) {
            System.out.println("Error: exception while writing config file!");
            e.printStackTrace();
        }
    }
