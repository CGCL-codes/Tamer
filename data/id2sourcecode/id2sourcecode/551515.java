    public boolean czy_slowo_kluczowe_pocz(String slowo) {
        int i = 0;
        boolean jest_slowem_kluczowym_pocz = false;
        String[] slowa_kluczowe_pocz = { "begin", "end", "while", "if", "else", "read(", "readln(", "write(", "writeln(" };
        while (i < slowa_kluczowe_pocz.length) {
            slowo = slowo.toLowerCase();
            if (slowo.compareTo(slowa_kluczowe_pocz[i]) == 0) {
                jest_slowem_kluczowym_pocz = true;
            }
            i++;
        }
        return jest_slowem_kluczowym_pocz;
    }
