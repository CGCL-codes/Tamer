    public void parse(BufferedReader bReader) throws IOException, CMLException {
        this.bReader = bReader;
        if (inputCML == null) createAndAddCMLElement(FORMAT, "");
        try {
            LineMatcher.readMatchers(parserUrl);
            lineMatchers = LineMatcher.lineMatchers;
            System.out.println("LineMatchers: " + lineMatchers.length);
        } catch (Exception e) {
            e.printStackTrace();
            throw new CMLException("" + e);
        }
        Vector lineVector = new Vector();
        while (true) {
            String line = peekLine();
            if (line == null) break;
            line = this.getCurrentLine();
            line = Util.rightTrim(line);
            lineVector.addElement(line);
        }
        for (int i = 0; i < lineVector.size(); i++) {
            StringValImpl sv = null;
            String line = (String) lineVector.elementAt(i);
            System.out.println("[" + line + "]");
            for (int j = 0; j < lineMatchers.length; j++) {
                if (!lineMatchers[j].match(line, 0)) continue;
                boolean matched = true;
                int lineCount = lineMatchers[j].nLines;
                for (int k = 1; k < lineCount; k++) {
                    if (i + k >= lineVector.size()) {
                        matched = false;
                        break;
                    }
                    String nextLine = (String) lineVector.elementAt(i + k);
                    if (j == 0) System.out.println(">>>>>" + nextLine + "<<<<<");
                    if (j == 0) System.out.println("<<<<<" + lineMatchers[j].lines[k] + ">>>>>");
                    if (!lineMatchers[j].match(nextLine, k)) {
                        matched = false;
                        break;
                    }
                }
                if (matched) {
                    String s = "";
                    for (int k = 0; k < lineCount; k++) {
                        s += (String) lineVector.elementAt(i + k);
                        if (k < lineCount - 1) s += "\n";
                    }
                    sv = new StringValImpl(this, s, null);
                    sv.setTitle(lineMatchers[j].title);
                    i += lineCount;
                    System.out.print("+");
                    break;
                }
            }
            if (sv == null) {
                sv = new StringValImpl(this, line, null);
                sv.setTitle(((line.equals("")) ? "BLANK" : "??"));
                System.out.print(".");
            }
            inputCML.appendChild(sv);
        }
        System.out.println("PreStyle finished...");
    }
