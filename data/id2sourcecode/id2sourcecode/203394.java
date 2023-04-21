    void processForeachRegion(Vector region) throws IOException {
        QuotedStringTokenizer pst = new QuotedStringTokenizer(params);
        if (!pst.hasMoreTokens()) throw new IOException("Missing variable in FOREACH");
        String var_name = pst.nextToken();
        if (!pst.hasMoreTokens()) throw new IOException("Missing filename in FOREACH");
        String file_name = pst.nextToken();
        String select = null;
        String start = null;
        String end = null;
        boolean inRange = false;
        if (pst.hasMoreTokens()) {
            select = pst.nextToken();
            if (!pst.hasMoreTokens()) throw new IOException("Missing field value in FOREACH");
            String fval = pst.nextToken();
            int dotdot = fval.indexOf("..");
            if (dotdot != -1 && dotdot == fval.lastIndexOf("..")) {
                start = fval.substring(0, dotdot);
                end = fval.substring(dotdot + 2);
            } else {
                start = fval;
            }
        }
        if (DEBUG) System.out.println("doing foreach with varname " + var_name + " on data file :" + file_name);
        if (DEBUG && select != null) {
            System.out.print("   selecting records with " + select);
            if (end == null) System.out.println(" equal to \"" + start + "\""); else System.out.println(" between \"" + start + "\" and \"" + end + "\"");
        }
        BufferedReader data;
        try {
            data = new BufferedReader(new FileReader(file_name));
        } catch (java.io.FileNotFoundException e) {
            data = new BufferedReader(new FileReader(inDir + file_name));
        }
        Vector fields_v = new Vector();
        Vector fpl_v = new Vector();
        for (String inLine = getNextLine(data); (inLine != null && inLine.length() != 0); inLine = getNextLine(data)) {
            StringTokenizer st = new StringTokenizer(inLine);
            fpl_v.addElement(new Integer(st.countTokens()));
            while (st.hasMoreTokens()) {
                String tok = st.nextToken();
                if (DEBUG) System.out.println("read field " + fields_v.size() + " :" + tok);
                fields_v.addElement(tok);
            }
        }
        fields_v.addElement(indexField);
        int[] fieldsPerLine = new int[fpl_v.size()];
        for (int i = 0; i < fieldsPerLine.length; i++) fieldsPerLine[i] = ((Integer) fpl_v.elementAt(i)).intValue();
        String[] fields = new String[fields_v.size()];
        for (int i = 0; i < fields.length; i++) fields[i] = (String) fields_v.elementAt(i);
        dataFileLoop: for (int curField = 0; ; curField++) {
            int i = 0;
            String[] fieldData = new String[fields.length];
            for (int j = 0; j < fieldsPerLine.length; j++) {
                String line = getNextLine(data);
                if (line == null) break dataFileLoop;
                if (fieldsPerLine[j] == 1) {
                    if (DEBUG) System.out.println("read field " + fields[i] + " :" + line);
                    fieldData[i++] = line;
                } else {
                    if (DEBUG) System.out.println("reading " + fieldsPerLine[j] + " fields");
                    StringTokenizer st = new StringTokenizer(line);
                    try {
                        for (int k = 0; k < fieldsPerLine[j]; k++) {
                            String tok = st.nextToken();
                            if (DEBUG) System.out.println("read field " + fields[i] + ": " + tok);
                            fieldData[i++] = tok;
                        }
                    } catch (NoSuchElementException x) {
                        throw new IOException("Missing field " + fields[i]);
                    }
                }
            }
            if (fieldsPerLine.length != 1) getNextLine(data);
            fieldData[i++] = Integer.toString(curField);
            if (select != null) {
                for (int j = 0; j < fields.length; j++) {
                    if (DEBUG) System.out.println("checking if select is field " + fields[j]);
                    if (select.equals(fields[j])) {
                        String value = fieldData[j];
                        if (value.equals(start)) inRange = true; else if (end == null) inRange = false; else if (value.equals(end)) end = null;
                        if (DEBUG) System.out.println("record in range; including");
                        break;
                    }
                }
                if (!inRange) break dataFileLoop;
            }
            for (int j = 1; j < region.size(); j++) {
                try {
                    String currentLine = (String) region.elementAt(j);
                    String result = substitute(currentLine, var_name, fields, fieldData);
                    out.print(result + "\n");
                } catch (ClassCastException e) {
                    Vector oldRegion = (Vector) region.elementAt(j);
                    Vector newRegion = substituteInRegion(oldRegion, var_name, fields, fieldData);
                    processTemplateRegion(newRegion);
                }
            }
        }
        data.close();
    }
