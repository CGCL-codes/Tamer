    public int ricercaBinaria(String pattern, int colonna) {
        if (pattern == null) return 0;
        int pos = 1, estremo1 = 1, estremo2, confronto;
        String trovato = "";
        boolean finito = false;
        pos = 0;
        try {
            trovato = getValueAt(pos, colonna).toString();
        } catch (RuntimeException e) {
        }
        confronto = trovato.compareToIgnoreCase(pattern);
        if (confronto > 0) return -pos; else if (confronto == 0) return pos;
        pos = getRowCount() - 1;
        trovato = "";
        try {
            trovato = getValueAt(pos, colonna).toString();
        } catch (RuntimeException e) {
        }
        confronto = trovato.compareToIgnoreCase(pattern);
        if (confronto < 0) return -pos; else if (confronto == 0) {
            while (pos > 0 && getValueAt(pos - 1, colonna).toString().compareToIgnoreCase(pattern) == 0) pos--;
            return pos;
        }
        estremo2 = pos;
        int tentativi = 1;
        while (!finito) {
            finito = (estremo1 - estremo2 >= -1);
            pos = estremo1 + (estremo2 - estremo1) / 2;
            trovato = "";
            try {
                trovato = getValueAt(pos, colonna).toString();
            } catch (RuntimeException e) {
            }
            confronto = trovato.compareToIgnoreCase(pattern);
            CostantiDavide.msgInfo("tentativo: " + tentativi + " da cercare: " + pattern + " trovato: " + trovato);
            if (confronto == 0) {
                while (pos > 0 && getValueAt(pos - 1, colonna).toString().compareToIgnoreCase(pattern) == 0) pos--;
                return pos;
            }
            if (confronto > 0) estremo2 = pos; else estremo1 = pos;
            tentativi++;
        }
        return -pos;
    }
