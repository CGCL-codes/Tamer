    public void readMESHDescriptorFileIntoDB() {
        String inputLine, ins;
        String filename = getMESHdescriptorfilename();
        String uid, name;
        Vector treenr = new Vector();
        Vector related = new Vector();
        int start, end;
        int countUID, countTreenr, countRelated;
        try {
            Connection db = tools.openDB(getMESHdbname());
            createMESHtreegraphtables(db);
            BufferedReader in = new BufferedReader(new FileReader(filename));
            countUID = 0;
            countTreenr = 0;
            countRelated = 0;
            tools.printDate();
            while ((inputLine = in.readLine()) != null) {
                if (inputLine.indexOf("<DescriptorRecord DescriptorClass") != -1) {
                    treenr.clear();
                    related.clear();
                    inputLine = in.readLine();
                    start = inputLine.indexOf("<DescriptorUI>") + 14;
                    end = inputLine.indexOf("</DescriptorUI>");
                    uid = inputLine.substring(start, end);
                    inputLine = in.readLine();
                    inputLine = in.readLine();
                    start = inputLine.indexOf("<String>") + 8;
                    end = inputLine.indexOf("</String>");
                    name = inputLine.substring(start, end);
                    if (name.indexOf("'") != -1) name = name.replace('\'', '$');
                    inputLine = in.readLine();
                    while ((inputLine != null) && (inputLine.indexOf("<SeeRelatedList>") == -1) && (inputLine.indexOf("<TreeNumberList>") == -1)) inputLine = in.readLine();
                    while ((inputLine != null) && (inputLine.indexOf("</SeeRelatedList>") == -1) && (inputLine.indexOf("<TreeNumberList>") == -1)) {
                        if (inputLine.indexOf("<DescriptorUI>") != -1) {
                            start = inputLine.indexOf("<DescriptorUI>") + 14;
                            end = inputLine.indexOf("</DescriptorUI>");
                            String nr = inputLine.substring(start, end);
                            related.add(nr);
                        }
                        inputLine = in.readLine();
                    }
                    while ((inputLine != null) && (inputLine.indexOf("<TreeNumberList>") == -1)) inputLine = in.readLine();
                    inputLine = in.readLine();
                    while ((inputLine != null) && (inputLine.indexOf("</TreeNumberList>") == -1)) {
                        start = inputLine.indexOf("<TreeNumber>") + 12;
                        end = inputLine.indexOf("</TreeNumber>");
                        String nr = inputLine.substring(start, end);
                        treenr.add(nr);
                        inputLine = in.readLine();
                    }
                    ins = countUID + ", '" + uid + "', '" + name + "'";
                    tools.insertIntoDB(db, getMESHgraphtableUID_NAME(), ins);
                    for (int i = 0; i < related.size(); i++) {
                        ins = countRelated + ", '" + uid + "', '" + (String) related.get(i) + "'";
                        tools.insertIntoDB(db, getMESHgraphtableUID_UID(), ins);
                        countRelated++;
                    }
                    for (int i = 0; i < treenr.size(); i++) {
                        ins = countTreenr + ", '" + (String) treenr.get(i) + "', '" + uid + "'";
                        tools.insertIntoDB(db, getMESHgraphtableTREENR_UID(), ins);
                        countTreenr++;
                    }
                    countUID++;
                    if ((countUID % 500) == 0) System.out.println(countUID);
                }
            }
            System.out.println("End import descriptors: " + countUID);
            tools.printDate();
            in.close();
            tools.closeDB(db);
        } catch (Exception e) {
            settings.writeLog("Error while reading MESH descriptor file: " + e.getMessage());
        }
    }
