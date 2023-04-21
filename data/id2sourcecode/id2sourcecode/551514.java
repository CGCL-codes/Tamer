    public boolean czy_slowo_kluczowe(String slowo) {
        int i = 0;
        boolean jest_slowem_kluczowym = false;
        String[] slowa_kluczowe = { "begin", "end", "while", "do", "if", "then", "else", "array", "var", "read(", "readln(", "write(", "writeln(", "program", "and", "or", "of" };
        while (i < slowa_kluczowe.length) {
            slowo = slowo.toLowerCase();
            if (slowo.compareTo(slowa_kluczowe[i]) == 0) {
                jest_slowem_kluczowym = true;
            }
            i++;
        }
        return jest_slowem_kluczowym;
    }
