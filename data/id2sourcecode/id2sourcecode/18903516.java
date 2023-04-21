    private void buildNewExecutionLog(String fn) {
        try {
            FileOutputStream zipFile = new FileOutputStream(fn + ".zip");
            ZipOutputStream output = new ZipOutputStream(new BufferedOutputStream(zipFile));
            output.putNextEntry(new ZipEntry(fn + " content.xml"));
            writeLn("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", output);
            writeLn("<WorkflowLog xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"" + " xsi:noNamespaceSchemaLocation=\"WorkflowLog.xsd\"" + " description=\"ProM execution log\">", output);
            writeLn("<Source program=\"" + About.NAME + "\">", output);
            writeLn("</Source>", output);
            writeLn("<Process id=\"" + PROCESS + "\"" + " description=\"Process containing the ProM execution Log\">", output);
            output.closeEntry();
            output.close();
        } catch (FileNotFoundException ex) {
            Message.add("Could not open execution log", Message.ERROR);
        } catch (IOException ex) {
            Message.add("Could not open execution log", Message.ERROR);
        }
    }
