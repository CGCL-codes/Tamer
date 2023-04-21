    public String readFile() {
        File file = new File(_f);
        try {
            BufferedReader in = new BufferedReader(new FileReader(file));
            String body = new String("<LB>\r\n");
            String str = new String();
            int lbTol = 0;
            boolean listStarted = false;
            while ((str = in.readLine()) != null) {
                str = str.trim();
                if (str.indexOf("*Note:*") != -1) {
                    str = replace(str, "*Note:*", "Note - ", 0);
                }
                if (str.indexOf("*Tip:*") != -1) {
                    str = replace(str, "*Tip:*", "Tip - ", 0);
                }
                if (str.indexOf("*Caution:*") != -1) {
                    str = replace(str, "*Caution:*", "Caution - ", 0);
                }
                if (str.indexOf("{{code}}") != -1) {
                    str = replace(str, "{{code}}", "{{ code }}", 0);
                }
                if (str.indexOf("{{info}}") != -1) {
                    str = replace(str, "{{info}}", "{{ info }}", 0);
                }
                if (str.indexOf("{{tip}}") != -1) {
                    str = replace(str, "{{tip}}", "{{ tip }}", 0);
                }
                if (str.indexOf("{{note}}") != -1) {
                    str = replace(str, "{{note}}", "{{ note }}", 0);
                }
                if (str.indexOf("{{noformat}}") != -1) {
                    str = replace(str, "{{noformat}}", "{{ noformat }}", 0);
                }
                if (str.startsWith("|") && !str.endsWith("|") && !str.endsWith("||")) {
                    str = str + "|";
                }
                if (str.indexOf("\\[") != -1) {
                    str = replace(str, "\\[", "(", 0);
                }
                if (str.indexOf("\\]") != -1) {
                    str = replace(str, "\\]", ")", 0);
                }
                if (!str.trim().startsWith("*") && !str.trim().startsWith("#") && !str.trim().startsWith("|")) {
                    str = replace(str, "{info", "<LB>\r\n{info", 0);
                    str = replace(str, "{warning", "<LB>\r\n{warning", 0);
                    str = replace(str, "{note", "<LB>\r\n{note", 0);
                    str = replace(str, "{tip", "<LB>\r\n{tip", 0);
                    str = replace(str, "{noformat}", "<LB>\r\n{noformat}", 0);
                }
                if (str.indexOf("*+[") != -1 && str.indexOf("]+*") != -1) {
                    str = replace(str, "*+[", "[", 0);
                    str = replace(str, "]+*", "]", 0);
                }
                if (str.equals("----")) {
                    continue;
                }
                if (str.indexOf("{section") != -1) {
                    continue;
                }
                if (str.indexOf("{column") != -1) {
                    continue;
                }
                if (str.indexOf("{livesearch") != -1) {
                    continue;
                }
                if (str.indexOf("{panel") != -1) {
                    continue;
                }
                if (str.toLowerCase().indexOf("table of content") != -1) {
                    continue;
                }
                if (str.indexOf("_[") != -1 && str.indexOf("]_") != -1) {
                    str = replace(str, "_[", "[", 0);
                    str = replace(str, "]_", "]", 0);
                }
                if (str.indexOf("*[") != -1 && str.indexOf("]*") != -1) {
                    str = replace(str, "*[", "[", 0);
                    str = replace(str, "]*", "]", 0);
                }
                if (str.startsWith("*") && str.endsWith("*")) {
                    str = replace(str, "*", "_", 0);
                }
                if (str.indexOf("| |") != -1) {
                    str = replace(str, "| |", "|-|", 0);
                }
                if (str.indexOf("|&nbsp;|") != -1) {
                    str = replace(str, "|&nbsp;|", "|-|", 0);
                }
                if (str.indexOf("{{[") != -1 && str.indexOf("]}}") != -1) {
                    str = replace(str, "{{[", "{{", 0);
                    str = replace(str, "]}}", "}}", 0);
                }
                if (str.startsWith("h7.") || str.startsWith("h6.") || str.startsWith("||") || str.startsWith("h4.") || str.startsWith("h5.") || str.startsWith("h1.") || str.startsWith("h2.") || str.startsWith("h3.")) {
                    body += "<LB>\r\n";
                }
                if (str.startsWith("#") || str.startsWith("*")) {
                    if (!listStarted) {
                        listStarted = true;
                    } else {
                        listStarted = true;
                    }
                } else {
                    listStarted = false;
                }
                str = replace(str, "||", "$ROWM", 0);
                str = replace(str, "|", "$COLM", 0);
                if (str.startsWith("$ROWM") && str.indexOf("$COLM") != -1) {
                    str = replace(str, "$ROWM", "$COLM", 0);
                }
                str = replace(str, "$ROWM", "||", 0);
                str = replace(str, "$COLM", "|", 0);
                body = LBCheck(body, str);
                if (str.startsWith("||") && str.endsWith("||")) {
                    inTable = true;
                    orphanedTable = false;
                    str = quickClean(str);
                    body += str;
                    body += "<LB>\r\n";
                    str = in.readLine();
                    str = str.trim();
                    str = quickClean(str);
                    body = LBCheck(body, str);
                }
                if (str.startsWith("###") || str.endsWith("***")) {
                    str = quickClean(str);
                    if (str.indexOf("{code}") != -1) {
                        body += str.substring(2, str.length());
                        body += "<LB>\r\n";
                    } else {
                        String tstr = str;
                        str = in.readLine();
                        str = quickClean(str);
                        str = str.trim();
                        if (str.indexOf("{code}") != -1) {
                            body += tstr.substring(2, tstr.length());
                            body += "<LB>\r\n";
                            body += str;
                            body += "<LB>\r\n";
                        } else {
                            body += tstr;
                            body += "<LB>\r\n";
                            body += str;
                            body += "<LB>\r\n";
                        }
                    }
                }
                if (str.startsWith("##") || str.endsWith("**")) {
                    str = quickClean(str);
                    if (str.indexOf("{code}") != -1) {
                        body += str.substring(1, str.length());
                        body += "<LB>\r\n";
                    } else {
                        String tstr = str;
                        str = in.readLine();
                        str = quickClean(str);
                        str = str.trim();
                        if (str.indexOf("{code}") != -1) {
                            body += tstr.substring(1, tstr.length());
                            body += "<LB>\r\n";
                            body += str;
                            body += "<LB>\r\n";
                        } else {
                            body += tstr;
                            body += "<LB>\r\n";
                            body += str;
                            body += "<LB>\r\n";
                        }
                    }
                }
                str = str.trim();
                if (str.startsWith("|") && str.endsWith("|") && orphanedTable) {
                    int count = 0;
                    inTable = true;
                    count = findCellCount(str);
                    char[] strArray = str.toCharArray();
                    body += "<LB>\r\n";
                    for (int i = 0; i < count; i++) {
                        body += "|| ";
                    }
                    body += "<LB>\r\n";
                }
                if (cleanLBLimit != -1) {
                    if (str.equals("")) {
                        if (lbTol == cleanLBLimit) {
                            continue;
                        } else {
                            lbTol++;
                        }
                    } else {
                        lbTol = 0;
                    }
                }
                str = quickClean(str);
                body += str;
                body += "<LB>\r\n";
            }
            in.close();
            body = body + "<LB>\r\n";
            return body;
        } catch (Exception ex) {
            writeParserErrorOutput("Error while reading from the input file.\r\n" + ex.getMessage());
            System.out.println(props.WITS_ParseErrorMessage);
            System.exit(0);
        }
        return null;
    }
