    public void analizuj(String linia, ArrayList<String> leksemy) throws Exception {
        try {
            if (linia == null || linia.length() == 0) return;
            if (linia.equalsIgnoreCase("end;")) {
                leksemy.add("end");
                leksemy.add(";");
                return;
            }
            if (linia.indexOf("read") != -1 || linia.indexOf("write") != -1 || linia.indexOf("writeln") != -1) {
                String[] str = linia.split(" ");
                sprawdzCudzyslow(str, linia, leksemy);
                if (str.length == 1 && !(str[0].equalsIgnoreCase("write") || str[0].equalsIgnoreCase("read") || str[0].equalsIgnoreCase("writeln"))) throw new Exception("Error: B��d w sk�adni");
                if (str.length == 1) throw new Exception("Error: Brak agrumentu dla instrukcji " + str[0]);
                if (str[0].equalsIgnoreCase("read")) {
                    for (int i = 1; i < str.length; i++) {
                        if (str[i].indexOf("+") != -1 || str[i].indexOf("-") != -1 || str[i].indexOf("*") != -1 || str[i].indexOf("/") != -1 || str[i].indexOf("mod") != -1 || str[i].indexOf("div") != -1 || str[i].indexOf("=") != -1 || str[i].indexOf(":=") != -1) throw new Exception("Error: Read nie mo�e by� u�ywanie wraz z operatorami");
                        if (!czyOperator(str[i])) if (czyCalkowita(str[i])) throw new Exception("Error: Nieprawid�owy argument dla read");
                        if (czyIdentyfikator(str[i])) {
                            if (!Skladnia.czyZmiennaTablicowa(str[i]) && !(str[i].contains("["))) {
                                Zmienna z = parent.getZmienne().get(str[i]);
                                if (z == null) throw new Exception("Error: Brak zmiennej " + str[i]);
                            }
                        }
                    }
                }
            }
            String[] str = linia.split(" ");
            for (String bycmozeleksem : str) {
                if (czySlowoKluczowe(bycmozeleksem) || czyOperator(bycmozeleksem) || czyIdentyfikator(bycmozeleksem) || czyString(bycmozeleksem) || czyCalkowita(bycmozeleksem) || czyRzeczywista(bycmozeleksem)) {
                    leksemy.add(bycmozeleksem);
                } else {
                    analizujDalej(bycmozeleksem, leksemy);
                }
            }
            oki = true;
        } catch (Exception e) {
            parent.wynik.append(e.toString());
        }
    }
