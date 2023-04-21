    private static String cleanXMLDataDescriptionFile(String xmlDataFileDescriptionFileName) {
        String tempFileName = xmlDataFileDescriptionFileName + ".temp";
        try {
            DeleteExternalFile(tempFileName);
            BufferedReader input = new BufferedReader(new FileReader(new File(xmlDataFileDescriptionFileName)));
            BufferedWriter output = new BufferedWriter(new FileWriter(new File(tempFileName)));
            String nextLine = input.readLine();
            while (nextLine != null) {
                nextLine = nextLine.replace("xmi:version=\"2.0\"", "");
                nextLine = nextLine.replace("xmlns:xmi=\"http://www.omg.org/XMI\"", "");
                nextLine = nextLine.replace("xmlns=\"datadescriptor.xsd\"", "");
                nextLine = nextLine.replace("array=\"\"", "");
                nextLine = nextLine.replace("branch=\"\"", "");
                nextLine = nextLine.replace("colDelimiter=\"\"", "");
                nextLine = nextLine.replace("cols=\"\"", "");
                nextLine = nextLine.replace("columns=\"\"", "");
                nextLine = nextLine.replace("count=\"\"", "");
                nextLine = nextLine.replace("countType=\"\"", "");
                nextLine = nextLine.replace("data=\"\"", "");
                nextLine = nextLine.replace("dataType=\"\"", "");
                nextLine = nextLine.replace("dataTypeType=\"\"", "");
                nextLine = nextLine.replace("newlineType=\"\"", "");
                nextLine = nextLine.replace("newLineTypeType=\"\"", "");
                nextLine = nextLine.replace("pattern=\"\"", "");
                nextLine = nextLine.replace("record=\"\"", "");
                nextLine = nextLine.replace("rowDelimiter=\"\"", "");
                nextLine = nextLine.replace("rows=\"\"", "");
                nextLine = nextLine.replace("table=\"\"", "");
                nextLine = nextLine.replace("target=\"\"", "");
                nextLine = nextLine.replace("type=\"\"", "");
                output.write(nextLine);
                output.write(System.getProperty("line.separator"));
                nextLine = input.readLine();
            }
            output.flush();
            output.close();
            input.close();
        } catch (Exception e) {
        }
        return tempFileName;
    }
