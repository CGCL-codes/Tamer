    public static void main(String[] args) {
        SpreadSheetGROOP spsg = new SpreadSheetGROOP(new File("testbed.csv"));
        Vector allNames = spsg.getAllSenderUsernames();
        for (int i = 0; i < allNames.size(); i++) {
            String name = (String) allNames.elementAt(i);
            if (!name.equalsIgnoreCase("server")) {
                spsg.createContig(name);
            }
        }
        spsg.createAlignmentScores();
        spsg.writeSpreadsheetToFile(new File("output.csv"));
    }
