    public void collectInternalCommand() {
        Database localDb = this.getFatherPanel().getLocalDb();
        try {
            String query = "SELECT ID_SH2, IMAGE_NAME_SH2, ID_SH1, IMAGE_NAME_SH1, LINE_NUMBER, LINK_ACTION " + "FROM LINK_SHAPES " + "WHERE ID_SH1=" + getElemId() + " AND IMAGE_NAME_SH1='" + getImgName() + "' AND " + "IMAGE_NAME_SH1=IMAGE_NAME_SH2 ORDER BY LINE_NUMBER";
            Statement sp = localDb.createStatement();
            ResultSet rp = sp.executeQuery(query);
            Text command = Text.EMPTY;
            while (rp.next()) {
                command = Text.valueOf(rp.getString("LINK_ACTION"));
                Text token[] = ParserUtils.split(command, " ");
                if (token[0].toUpperCase().contentEquals(Text.valueOf("ASSIGNVALUETO"))) {
                    variablesHashTable.put(token[1], new Integer(rp.getInt("ID_SH2")));
                } else if (token[0].toUpperCase().contentEquals(Text.valueOf("ASSIGNVALUE"))) {
                    this.setAssignExpression(token[1]);
                } else if (token[0].toUpperCase().contentEquals(Text.valueOf("FASTASSIGN"))) {
                    this.setFastAssignExpression(token[1]);
                } else if (token[0].toUpperCase().contentEquals(Text.valueOf("ABSDIFF"))) {
                    Text[] expr = new Text[token.length - 1];
                    System.arraycopy(token, 1, expr, 0, expr.length);
                    setAbsDiffExpression(expr);
                } else if (token[0].toUpperCase().contentEquals(Text.valueOf("ASSIGNCOLORTO"))) {
                    colorVariablesHashTable.put(token[1], new Integer(rp.getInt("ID_SH2")));
                } else if (token[0].toUpperCase().contentEquals(Text.valueOf("CHANGEFONTCOLORIF"))) {
                    for (int i = 1; i < token.length - 2; i++) {
                        setFontColorExpression(0, getFontColorExpression()[0].concat(BLANK).concat(token[i]));
                    }
                    setFontColorExpression(1, getFontColorExpression()[1].concat(BLANK).concat(token[token.length - 2]));
                    setFontColorExpression(2, getFontColorExpression()[2].concat(BLANK).concat(token[token.length - 1]));
                } else if (token[0].toUpperCase().contentEquals(Text.valueOf("CHANGECOLORSIF"))) {
                    for (int i = 1; i < token.length - 2; i++) {
                        setColorExpression(0, getColorExpression()[0].concat(BLANK).concat(token[i]));
                    }
                    setColorExpression(1, getColorExpression()[1].concat(BLANK).concat(token[token.length - 2]));
                    setColorExpression(2, getColorExpression()[2].concat(BLANK).concat(token[token.length - 1]));
                } else if (token[0].toUpperCase().contentEquals(Text.valueOf("ORCHECKCOLORS"))) {
                    Text[] colExpr = new Text[token.length];
                    colExpr[0] = OR;
                    int j = 1;
                    for (int i = 0; i < token.length - 1; i++) {
                        if (!token[i + 1].contentEquals(Text.EMPTY)) {
                            colExpr[j] = token[i + 1];
                            j++;
                        }
                    }
                    Text[] res = new Text[j];
                    setFastCheckColorExpression(res);
                    System.arraycopy(colExpr, 0, res, 0, res.length);
                } else if (token[0].toUpperCase().contentEquals(Text.valueOf("ANDCHECKCOLORS"))) {
                    Text[] colExpr = new Text[token.length];
                    colExpr[0] = AND;
                    int j = 1;
                    for (int i = 0; i < token.length - 1; i++) {
                        if (!token[i + 1].contentEquals(Text.EMPTY)) {
                            colExpr[j] = token[i + 1];
                            j++;
                        }
                    }
                    Text[] res = new Text[j];
                    setFastCheckColorExpression(res);
                    System.arraycopy(colExpr, 0, res, 0, res.length);
                } else if (token[0].toUpperCase().contentEquals(Text.valueOf("ORCHECKFONTCOLORS"))) {
                    Text[] colExpr = new Text[token.length];
                    colExpr[0] = OR;
                    int j = 1;
                    for (int i = 0; i < token.length - 1; i++) {
                        if (!token[i + 1].contentEquals(Text.EMPTY)) {
                            colExpr[j] = token[i + 1];
                            j++;
                        }
                    }
                    Text[] res = new Text[j];
                    setFastCheckFontColorExpression(res);
                    System.arraycopy(colExpr, 0, res, 0, res.length);
                } else if (token[0].toUpperCase().contentEquals(Text.valueOf("ANDCHECKFONTCOLORS"))) {
                    Text[] colExpr = new Text[token.length];
                    colExpr[0] = AND;
                    int j = 1;
                    for (int i = 0; i < token.length - 1; i++) {
                        if (!token[i + 1].contentEquals(Text.EMPTY)) {
                            colExpr[j] = token[i + 1];
                            j++;
                        }
                    }
                    Text[] res = new Text[j];
                    setFastCheckFontColorExpression(res);
                    System.arraycopy(colExpr, 0, res, 0, res.length);
                } else if (token[0].toUpperCase().contentEquals(Text.valueOf("VISIBLEIF"))) {
                    Text visExpr = Text.EMPTY;
                    for (int i = 1; i < token.length; i++) {
                        visExpr = visExpr.concat(token[i]);
                    }
                    setVisualizationExpression(visExpr);
                } else if (token[0].toUpperCase().contentEquals(Text.valueOf("CHANGEBORDERCOLORIF"))) {
                    for (int i = 1; i < token.length - 2; i++) {
                        setBorderColorExpression(0, getBorderColorExpression()[0].concat(BLANK).concat(token[i]));
                    }
                    setBorderColorExpression(1, getBorderColorExpression()[1].concat(BLANK).concat(token[token.length - 2]));
                    setBorderColorExpression(2, getBorderColorExpression()[2].concat(BLANK).concat(token[token.length - 1]));
                } else if (token[0].toUpperCase().contentEquals(Text.valueOf("CHANGEFILLCOLORIF"))) {
                    for (int i = 1; i < token.length - 2; i++) {
                        setFillColorExpression(0, getFillColorExpression()[0].concat(BLANK).concat(token[i]));
                    }
                    setFillColorExpression(1, getFillColorExpression()[1].concat(BLANK).concat(token[token.length - 2]));
                    setFillColorExpression(2, getFillColorExpression()[2].concat(BLANK).concat(token[token.length - 1]));
                } else if (token[0].toUpperCase().contentEquals(Text.valueOf("CHANGEVALUEIF"))) {
                    for (int i = 1; i < token.length - 2; i++) {
                        setChangeValueExpression(0, getFillColorExpression()[0].concat(BLANK).concat(token[i]));
                    }
                    setChangeValueExpression(1, getFillColorExpression()[1].concat(BLANK).concat(token[token.length - 2]));
                    setChangeValueExpression(2, getFillColorExpression()[2].concat(BLANK).concat(token[token.length - 1]));
                } else if (token[0].toUpperCase().contentEquals(Text.valueOf("EXECUTE"))) {
                    enableThreadCreation = true;
                    String path = command.toString().substring(token[0].length() + 1, command.length());
                    if (path.startsWith("%CURDIR/")) {
                        path = getFatherPanel().getCurDir() + "/" + path.replaceAll("%CURDIR/", "");
                    } else if (path.startsWith("%CURDIR\\")) {
                        path = getFatherPanel().getCurDir() + path.replaceAll("%CURDIR", "");
                    }
                    path = path.replaceAll("//", "\\");
                    byte buff[] = new byte[2048];
                    try {
                        InputStream fileIn = new FileInputStream(path);
                        int i = fileIn.read(buff);
                        associatedScript = new String(buff);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
