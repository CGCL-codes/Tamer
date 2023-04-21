    public void scan(String line, ArrayList<String> lexems) throws Exception {
        if (line == null || line.length() == 0) return;
        if (line.equalsIgnoreCase("end;")) {
            lexems.add("end");
            lexems.add(";");
            return;
        }
        if (line.indexOf("read") != -1 || line.indexOf("READ") != -1 || line.indexOf("WRITE") != -1 || line.indexOf("WRITELN") != -1 || line.indexOf("write") != -1 || line.indexOf("writeln") != -1) {
            String[] str = line.split(" ");
            if (str[0].equalsIgnoreCase("write") || str[0].equalsIgnoreCase("writeln")) {
                lexems.add(str[0]);
                int cudz = 0;
                for (int i = 0; i < line.length(); i++) if (line.charAt(i) == '\"') cudz++;
                if (!(cudz % 2 == 0)) if (cudz != 0) throw new Exception("Napis jest niepoprawnie zakonczony");
                if (cudz == 0) {
                    if (str.length > 1) {
                        lexems.add(str[1]);
                        return;
                    }
                } else {
                    int poz = line.indexOf("\"");
                    String temp = "";
                    while (poz < line.length()) {
                        temp += line.charAt(poz);
                        ++poz;
                    }
                    lexems.add(temp);
                    return;
                }
                int o = 0;
                int z = 0;
                if (line.indexOf("\"") == -1) {
                    for (int i = 0; i < line.length(); i++) {
                        if (line.charAt(i) == '(') o++;
                        if (line.charAt(i) == ')') z++;
                    }
                    if (o != z) throw new Exception("Liczba nawiasow otwierajacych oraz zamykajacych nie zgadza sie");
                    if (o == 0) {
                    }
                }
            }
            if (str.length == 1 && !(str[0].equalsIgnoreCase("write") || str[0].equalsIgnoreCase("read") || str[0].equalsIgnoreCase("writeln"))) throw new Exception("Blad skladni - read|write|writeln[spacja]argument");
            if (str.length == 1) throw new Exception("Brak agrumentu dla instrukcji " + str[0]);
            if (str[0].equalsIgnoreCase("read")) {
                for (int i = 1; i < str.length; i++) {
                    if (str[i].indexOf("+") != -1 || str[i].indexOf("-") != -1 || str[i].indexOf("*") != -1 || str[i].indexOf("/") != -1 || str[i].indexOf("div") != -1 || str[i].indexOf("=") != -1 || str[i].indexOf(":=") != -1) throw new Exception("Nie mozna uzywac operatorow w instrukcji read");
                    if (!isOperator(str[i])) if (isInteger(str[i])) throw new Exception("Nieprawidlowy argument dla instrukcji read");
                    if (isIdentifier(str[i])) {
                        Variable v = parent.getVariables().get(str[i]);
                        if (v == null) throw new Exception("Brak zmiennej " + str[i]);
                    }
                }
            }
        }
        if (line.indexOf(":=") != -1) {
            String[] lee = line.split(":=");
            if (parent.getVariables().get(lee[0]) != null) {
                String type = parent.getVariables().get(lee[0]).getType();
                if (type.equalsIgnoreCase("string") && lee.length > 1) {
                    lexems.add(lee[0]);
                    lexems.add(":=");
                    int cudz = 0;
                    for (int g = 0; g < lee[1].length(); g++) if (lee[1].charAt(g) == '\"') cudz++;
                    if ((cudz % 2) != 0 && cudz != 0) throw new Exception("Ciag znakow jest niepoprawnie zakonczony");
                    if (cudz == 0) {
                        try {
                            type = parent.getVariables().get(lee[1]).getType();
                            if (!type.equalsIgnoreCase("string") || !type.equalsIgnoreCase("char")) throw new Exception("Nie mozna przypisac typu " + type + " do string");
                        } catch (Exception ex) {
                            throw new Exception("Brak zmiennej " + lee[1]);
                        }
                    }
                    lexems.add(lee[1]);
                    return;
                }
            }
        }
        String[] str = line.split(" ");
        for (String s : str) {
            if (isKeyword(s) || isOperator(s) || isIdentifier(s) || isString(s) || isInteger(s) || isReal(s) || isChar(s)) {
                lexems.add(s);
            } else {
                analize(s, lexems);
            }
        }
    }
