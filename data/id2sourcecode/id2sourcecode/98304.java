    public void readMESHTreeFileIntoDB() {
        String inputLine;
        String filename = getMESHtreefilename();
        try {
            Connection db = tools.openDB(getMESHdbname());
            createMESHtreetable(db);
            BufferedReader in = new BufferedReader(new FileReader(filename));
            countTreeImport = 0;
            while (((inputLine = in.readLine()) != null) && (!stopTreeImport)) {
                String term = inputLine.substring(0, inputLine.indexOf(";"));
                if (term.indexOf("'") > -1) {
                    term = term.replace('\'', '$');
                }
                String hier_complete = inputLine.substring(inputLine.indexOf(";") + 1);
                String hier_prev, hier_key;
                if (hier_complete.length() == 3) {
                    hier_prev = "-";
                    hier_key = hier_complete;
                } else {
                    int l = hier_complete.length();
                    hier_prev = hier_complete.substring(l - 7, l - 4);
                    hier_key = hier_complete.substring(l - 3, l);
                }
                String ins, conv;
                conv = "";
                ins = conv.valueOf(countTreeImport) + ",'" + term + "','" + hier_key;
                ins = ins + "','" + hier_prev + "','" + hier_complete + "'";
                tools.insertIntoDB(db, getMESHtreetablename(), ins);
                countTreeImport++;
                if ((countTreeImport % 100) == 0) System.out.println(countTreeImport);
            }
            in.close();
            tools.closeDB(db);
        } catch (Exception e) {
            settings.writeLog("Error while reading MESH tree file: " + e.getMessage());
        }
    }
