    private List createObject(String moduleName, List<Line> lines, List<CompileParserException> errors) throws CompileParserException {
        List<Line> funcs = Collections.EMPTY_LIST;
        boolean extracting = false;
        if (lines != null) {
            for (ListIterator l = lines.listIterator(); l.hasNext(); ) {
                Line myLine = (Line) l.next();
                String line = myLine.getText();
                extracting = extracting || line.startsWith(Constants.VAR + " ") || line.endsWith(":") || line.startsWith(DEF_FUNC + " ");
                if (extracting) {
                    if (funcs == Collections.EMPTY_LIST) funcs = new ArrayList<Line>();
                    funcs.add(myLine);
                    l.remove();
                }
            }
        }
        obj = new Obj(name, superName, false, null, null, grammar.getModulo()).digest(moduleName, funcs, errors);
        obj.setAction(this);
        grammar.getModulo().addObject(obj);
        return lines;
    }
