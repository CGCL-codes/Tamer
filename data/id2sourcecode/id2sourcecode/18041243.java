    public int ls() {
        session.update_readerList();
        int nbReaders = session.get_cgd().get_nbReader();
        for (int i = 0; i < nbReaders; i++) {
            screen.write(i + 1 + ": " + session.get_cgd().get_reader(i + 1) + "  ");
            if (session.get_cgd().contains_card(i)) {
                screen.write("Card inserted  ");
                screen.writeln("ATR=" + Apdu.ba2s(session.get_cgd().getATR(i)));
            } else {
                screen.writeln("No card inserted !");
            }
        }
        screen.writeln("---- " + nbReaders + " reader(s) found !");
        return Errors.SUCCESS;
    }
