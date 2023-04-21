    public void parse() throws Exception {
        if (!lexems.get(0).equals("program")) throw new Exception("Brak slowa kluczowego program na poczatku kodu");
        if (!Scanner.isIdentifier(lexems.get(1))) throw new Exception("Brak nazwy programu");
        if (!lexems.get(lexems.size() - 1).equals("end.")) throw new Exception("Program niepoprawnie zakonczony");
        boolean isVar = false;
        isVar = lexems.contains("var");
        if (isVar) {
            int i = parent.getPosOfLastVariable();
            boolean foundBegin = false;
            while (!foundBegin && i < lexems.size()) {
                if (lexems.get(i++).equalsIgnoreCase("begin")) foundBegin = true;
            }
            if (!foundBegin) throw new Exception("Sekcja wykonywalna nie rozpoczyna sie od slowa kluczowego 'begin'");
            for (int j = parent.getPosOfLastVariable(); j < lexems.size() - 1; j++) {
                String a = lexems.get(j);
                String b = lexems.get(j + 1);
                if (Scanner.isChar(a) && Scanner.isChar(b)) throw new Exception("Znaki " + a + " oraz " + b + " nie moga wystepowac jednoczesnie po sobie");
                if (Scanner.isIdentifier(a) && Scanner.isIdentifier(b)) throw new Exception("Identyfikatory " + a + " oraz " + b + " nie moga wystepowac jednoczesnie po sobie");
                if (Scanner.isInteger(a) && Scanner.isInteger(b)) throw new Exception("Liczby " + a + " oraz " + b + " nie moga wystepowac jednoczesnie po sobie");
                if (Scanner.isKeyword(a) && Scanner.isKeyword(b)) {
                    if (!a.equalsIgnoreCase("begin") && !a.equalsIgnoreCase("do")) if (!a.equalsIgnoreCase("then")) throw new Exception("Slowa kluczowe " + a + " oraz " + b + " nie moga wystepowac jednoczesnie po sobie");
                    if (a.equalsIgnoreCase("begin") && !(b.equalsIgnoreCase("while") || b.equalsIgnoreCase("if") || b.equalsIgnoreCase("write") || b.equalsIgnoreCase("read") || b.equalsIgnoreCase("writeln") || b.equalsIgnoreCase("end.") || b.equalsIgnoreCase("end"))) throw new Exception("Slowa kluczowe " + a + " oraz " + b + " nie moga wystepowac jednoczesnie po sobie");
                }
                if (Scanner.isReal(a) && Scanner.isReal(b)) throw new Exception("Liczby " + a + " oraz " + b + " nie moga wystepowac jednoczesnie po sobie");
                if (Scanner.isString(a) && Scanner.isString(b)) throw new Exception("Napisy " + a + " oraz " + b + " nie moga wystepowac jednoczesnie po sobie");
                if (Scanner.isOperator(a) && Scanner.isOperator(b)) {
                    if (a.equals(")") && b.equals("(")) throw new Exception("Nawiasy " + a + " oraz " + b + " nie moga wystepowac jednoczesnie po sobie");
                    if (a.equals(")") && (!Scanner.isArithemiticalExpression(b) && !b.equals(")") && !b.equals(";") && !b.equals("=") && !Scanner.isLogicalOperator(b))) throw new Exception("Operatory " + a + " oraz " + b + " nie moga wystepowac obok siebie");
                    if (a.equals("-")) {
                        if (!b.equals("(")) throw new Exception("Operatory " + a + " oraz " + b + " nie moga wystepowac obok siebie");
                    }
                    if (Scanner.isArithemiticalExpression(a) && !(b.equals("(") || Scanner.isIdentifier(b) || Scanner.isInteger(b) || Scanner.isReal(b))) throw new Exception("Operatory " + a + " oraz " + b + " nie moga wystepowac obok siebie");
                }
            }
            while (i < lexems.size() - 1) {
                String lexem = lexems.get(i);
                List<String> temp = new ArrayList<String>();
                int open = 0;
                int close = 0;
                while (!lexem.equals(";") && i < lexems.size()) {
                    if (lexems.get(i).equals("(")) open++;
                    if (lexems.get(i).equals(")")) close++;
                    lexem = lexems.get(i++);
                    temp.add(lexem);
                }
                if (open != close) throw new Exception("Ilosc nawiasow otwierajacych oraz zamykajacych nie zgadza sie");
                int j = 0;
                if (temp.get(0).equalsIgnoreCase("while")) {
                    boolean foundDo = false;
                    for (j = 1; j < temp.size(); j++) {
                        if (temp.get(j).equalsIgnoreCase("do")) {
                            foundDo = true;
                            break;
                        }
                    }
                    if (!foundDo) throw new Exception("Blad skladni - brak slowa kluczowego 'do' w petli 'while'");
                }
                if (temp.get(0).equalsIgnoreCase("if")) {
                    boolean foundDo = false;
                    for (j = 1; j < temp.size(); j++) {
                        if (temp.get(j).equalsIgnoreCase("then")) {
                            foundDo = true;
                            break;
                        }
                    }
                    if (!foundDo) throw new Exception("Blad skladni - brak slowa kluczowego 'then' w klauzuli 'if'");
                }
                int x = 0;
                if (temp.get(0).equalsIgnoreCase("while") || temp.get(0).equalsIgnoreCase("if")) {
                    String xx = temp.get(x);
                    while (x < temp.size() && !(xx.equalsIgnoreCase("do") || xx.equalsIgnoreCase("then"))) {
                        x++;
                        xx = temp.get(x);
                    }
                    logicalInstruction(1, x, temp);
                }
                if (x != 0) temp = temp.subList(x + 1, temp.size());
                if (temp.contains(":=") && !temp.contains("if")) {
                    String lvalue = temp.get(0);
                    Variable v = parent.getVariables().get(lvalue);
                    if (v == null && !Scanner.isKeyword(temp.get(0))) throw new Exception("Brak zmiennej " + lvalue + " - przypisanie nie jest mozliwe");
                    if (!Scanner.isKeyword(lvalue)) {
                        String type = v.getType();
                        if (type.equalsIgnoreCase("integer")) {
                            for (int h = x; h < temp.size(); h++) {
                                String lex = temp.get(h);
                                if (Scanner.isLogicalOperator(lex)) throw new Exception("Nie mozna uzyc operatora logicznego w przypisaniu do zmiennej typu integer");
                                if (Scanner.isReal(lex)) throw new Exception("Nie mozna przypisac real do integer");
                                if (Scanner.isChar(lex)) throw new Exception("Nie mozna przypisac char do integer");
                                if (Scanner.isString(lex)) throw new Exception("Nie mozna przypisac string do integer");
                                if (Scanner.isBoolean(lex)) throw new Exception("Nie mozna przypisac boolean do integer");
                                if (Scanner.isIdentifier(lex) && parent.getVariables().containsKey(lex)) {
                                    String typ = parent.getVariables().get(lex).getType();
                                    if (typ.equalsIgnoreCase("boolean")) throw new Exception("Nie mozna przypisac boolean do integer");
                                    if (typ.equalsIgnoreCase("real")) throw new Exception("Nie mozna przypisac real do integer");
                                    if (typ.equalsIgnoreCase("string")) throw new Exception("Nie mozna przypisac string do integer");
                                    if (typ.equalsIgnoreCase("char")) throw new Exception("Nie mozna przypisac char do integer");
                                }
                            }
                        }
                        if (type.equalsIgnoreCase("real")) {
                            for (int h = x; h < temp.size(); h++) {
                                String lex = temp.get(h);
                                if (Scanner.isLogicalOperator(lex)) throw new Exception("Nie mozna uzyc operatora logicznego w przypisaniu do zmiennej typu real");
                                if (lex.equalsIgnoreCase("mod")) throw new Exception("Operacja mod niedozwolona dla typu real");
                                if (lex.equalsIgnoreCase("div")) throw new Exception("Operacja div niedozwolona dla typu real");
                                if (Scanner.isChar(lex)) throw new Exception("Nie mozna przypisac char do real");
                                if (Scanner.isString(lex)) throw new Exception("Nie mozna przypisac string do real");
                                if (Scanner.isBoolean(lex)) throw new Exception("Nie mozna przypisac boolean do real");
                                if (Scanner.isIdentifier(lex) && parent.getVariables().containsKey(lex)) {
                                    String typ = parent.getVariables().get(lex).getType();
                                    if (typ.equalsIgnoreCase("boolean")) throw new Exception("Nie mozna przypisac boolean do real");
                                    if (typ.equalsIgnoreCase("string")) throw new Exception("Nie mozna przypisac string do real");
                                    if (typ.equalsIgnoreCase("char")) throw new Exception("Nie mozna przypisac char do real");
                                }
                            }
                        }
                        if (type.equalsIgnoreCase("string")) {
                            for (int h = x; h < temp.size(); h++) {
                                String lex = temp.get(h);
                                if (Scanner.isLogicalOperator(lex)) throw new Exception("Nie mozna uzyc operatora logicznego w przypisaniu do zmiennej typu string");
                                if (lex.equalsIgnoreCase("mod")) throw new Exception("Operacja mod niedozwolona dla typu string");
                                if (lex.equalsIgnoreCase("div")) throw new Exception("Operacja div niedozwolona dla typu string");
                                if (Scanner.isBoolean(lex)) throw new Exception("Nie mozna przypisac boolean do string");
                                if (Scanner.isIdentifier(lex) && parent.getVariables().containsKey(lex)) {
                                    String typ = parent.getVariables().get(lex).getType();
                                    if (typ.equalsIgnoreCase("boolean")) throw new Exception("Nie mozna przypisac boolean do string");
                                    if (typ.equalsIgnoreCase("integer")) throw new Exception("Nie mozna przypisac integer do string");
                                    if (typ.equalsIgnoreCase("real")) throw new Exception("Nie mozna przypisac real do real");
                                }
                            }
                        }
                        if (type.equalsIgnoreCase("char")) {
                            for (int h = x; h < temp.size(); h++) {
                                String lex = temp.get(h);
                                if (lex.equals("*") || lex.equals("/")) throw new Exception("Nie mozna uzyc operatora arytmetycznego w przypisaniu do zmiennej typu char");
                                if (lex.equalsIgnoreCase("mod")) throw new Exception("Operacja mod niedozwolona dla typu char");
                                if (lex.equalsIgnoreCase("div")) throw new Exception("Operacja div niedozwolona dla typu char");
                                if (Scanner.isBoolean(lex)) throw new Exception("Nie mozna przypisac boolean do char");
                                if (Scanner.isIdentifier(lex) && parent.getVariables().containsKey(lex)) {
                                    String typ = parent.getVariables().get(lex).getType();
                                    if (typ.equalsIgnoreCase("boolean")) throw new Exception("Nie mozna przypisac boolean do char");
                                    if (typ.equalsIgnoreCase("integer")) throw new Exception("Nie mozna przypisac integer do char");
                                    if (typ.equalsIgnoreCase("real")) throw new Exception("Nie mozna przypisac real do char");
                                    if (typ.equalsIgnoreCase("string")) throw new Exception("Nie mozna przypisac string do char");
                                }
                            }
                        }
                        if (type.equalsIgnoreCase("boolean")) {
                            String lex = temp.get(x + 2);
                            boolean bool = false;
                            try {
                                String typ = parent.getVariables().get(lex).getType();
                                if (!typ.equalsIgnoreCase("boolean")) throw new Exception("Nie mozna przypisac typu " + typ + " do boolean");
                            } catch (Exception e) {
                                if (!Scanner.isBoolean(lex)) throw new Exception("Nie mozna przypisac " + lex + " do boolean");
                            }
                        }
                    }
                }
                continue;
            }
        } else {
        }
    }
