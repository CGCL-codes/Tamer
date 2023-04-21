    public static void run() {
        DateFormat shortDateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm");
        String command = new String();
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        try {
            boolean justStarted = true;
            if (JReader.getConfig().getUpdateAllOnStartup()) {
                updateVisibleChannels();
            }
            if (JReader.getConfig().getAutoUpdateMinutes() > 0) {
            }
            while (!(command.equals("quit"))) {
                if (justStarted) {
                    command = "help";
                    justStarted = false;
                } else {
                    command = in.readLine().trim();
                }
                if (command.equals("help")) {
                    System.out.println("Dostepne komendy:");
                    System.out.print("show channels\t");
                    System.out.print("show items\t");
                    System.out.print("show preview\t");
                    System.out.println("show tags");
                    System.out.print("add channel\t");
                    System.out.print("previous item\t");
                    System.out.print("next item\t");
                    System.out.print("update all\t");
                    System.out.println("next unread");
                    System.out.print("select item\t");
                    System.out.print("select channel\t");
                    System.out.print("select all\t");
                    System.out.print("select unread\t");
                    System.out.println("select tag");
                    System.out.print("mark channel\t");
                    System.out.print("update channel\t");
                    System.out.print("edit tags\t");
                    System.out.println("remove channel");
                    System.out.print("set sort\t");
                    System.out.print("set delete\t");
                    System.out.print("set update\t");
                    System.out.println("set autoupdate");
                    System.out.print("import\t\t");
                    System.out.print("export\t\t");
                    System.out.print("help\t\t");
                    System.out.println("quit");
                } else if (command.equals("show channels")) {
                    if (JReader.getVisibleChannels().size() == 0) {
                        System.out.println("Lista subskrypcji jest pusta.");
                    } else {
                        Channel channel;
                        for (int i = 0; i < JReader.getVisibleChannels().size(); i++) {
                            channel = JReader.getChannel(i);
                            System.out.print("Kanal " + (i + 1) + ": ");
                            if (channel.isFail()) {
                                System.out.print("FAIL ");
                            }
                            System.out.print(channel.getTitle());
                            if (channel.getUnreadItemsCount() > 0) {
                                System.out.println(" (" + channel.getUnreadItemsCount() + ")");
                            } else {
                                System.out.println();
                            }
                        }
                    }
                } else if (command.equals("show items")) {
                    if (JReader.getItems().size() == 0) {
                        System.out.println("Lista wiadomosci jest pusta.");
                    } else {
                        for (int i = 0; i < JReader.getItems().size(); i++) {
                            Item item = JReader.getItems().get(i);
                            System.out.print((i + 1) + ": ");
                            if (item.getDate() != null) {
                                System.out.print(shortDateFormat.format(item.getDate()) + " ");
                            }
                            if (!item.isRead()) {
                                System.out.print("N ");
                            }
                            if (JReader.getChannel(item.getChannelId()).getTitle().length() > 12) {
                                System.out.print(JReader.getChannel(item.getChannelId()).getTitle().substring(0, 12) + " ");
                            } else {
                                System.out.print(JReader.getChannel(item.getChannelId()).getTitle() + " ");
                            }
                            System.out.println(item.getTitle());
                        }
                    }
                } else if (command.equals("show preview")) {
                    if (JReader.getPreview(0).getCurrent() == null) {
                        System.out.println("Nie wybrano zadnej wiadomosci.");
                    } else {
                        System.out.println(JReader.getPreview(0).getCurrent().getTitle());
                        System.out.println("Link: " + JReader.getPreview(0).getCurrent().getLink());
                        if (JReader.getPreview(0).getCurrent().getAuthor() != null) {
                            System.out.println("Autor: " + JReader.getPreview(0).getCurrent().getAuthor());
                        }
                        if (JReader.getPreview(0).getCurrent().getSource() != null) {
                            System.out.println("Zrodlo: " + JReader.getPreview(0).getCurrent().getSource());
                        }
                        System.out.println("Opis: " + JReader.getPreview(0).getCurrent().getHTML());
                    }
                } else if (command.equals("show tags")) {
                    if (JReader.getTags().size() == 0) {
                        System.out.println("Lista tagow jest pusta.");
                    } else {
                        for (String tag : JReader.getTags()) {
                            System.out.print(tag.concat(" "));
                        }
                        System.out.println();
                    }
                } else if (command.equals("add channel")) {
                    try {
                        System.out.print("Podaj adres URL: ");
                        String url = in.readLine();
                        url = url.replace("\\tld", "~");
                        System.out.print("Podaj tagi: ");
                        String tags = in.readLine();
                        JReader.addChannel(url, tags);
                        System.out.println("Kanal zostal dodany");
                    } catch (LinkNotFoundException lnfe) {
                        System.out.println("Nie znaleziono kanalow na tej stronie.");
                    } catch (MalformedURLException mue) {
                        System.out.print("Nie mozna dodac kanalu.");
                        System.out.println(" URL jest nieprawidlowy.");
                    } catch (SAXParseException spe) {
                        System.out.print("Nie mozna dodac kanalu.");
                        System.out.println(" Zrodlo nie jest prawidlowym plikiem XML.");
                        System.out.print("Blad w linii " + spe.getLineNumber() + ". ");
                        System.out.println("Szczegoly: " + spe.getLocalizedMessage());
                    } catch (SAXException saxe) {
                        System.out.print("Nie mozna dodac kanalu.");
                        System.out.println(" Blad parsera XML.");
                    } catch (SocketException se) {
                        System.out.println("Nie mozna dodac kanalu. Szczegoly:");
                        System.out.println(se.getLocalizedMessage());
                    } catch (IOException ioe) {
                        System.out.print("Nie mozna dodac kanalu.");
                        System.out.println(" Pobieranie strony nie powiodlo sie.");
                    }
                } else if (command.equals("previous item")) {
                    if (JReader.previousItem(0) == null) {
                        System.out.println("Nie mozna sie cofnac.");
                    }
                } else if (command.equals("next item")) {
                    if (JReader.nextItem(0) == null) {
                        System.out.println("Nie mozna przejsc dalej.");
                    }
                } else if (command.equals("update all")) {
                    updateVisibleChannels();
                } else if (command.equals("next unread")) {
                    if (!JReader.nextUnread(0)) {
                        System.out.println("Nie ma nieprzeczytanych wiadomosci.");
                    }
                } else if (command.equals("select item")) {
                    try {
                        System.out.print("Podaj numer elementu: ");
                        int nr = new Integer(in.readLine()) - 1;
                        JReader.selectItem(JReader.getItems().get(nr), 0);
                    } catch (NumberFormatException nfe) {
                        System.out.println("Musisz podac liczbe calkowita.");
                    } catch (IndexOutOfBoundsException ioobe) {
                        System.out.println("Nie ma takiego elementu.");
                    }
                } else if (command.equals("select channel")) {
                    try {
                        System.out.print("Podaj numer kanalu: ");
                        int nr = new Integer(in.readLine()) - 1;
                        JReader.selectChannel(nr, 0);
                    } catch (NumberFormatException nfe) {
                        System.out.println("Musisz podac liczbe calkowita.");
                    } catch (IndexOutOfBoundsException ioobe) {
                        System.out.println("Nie ma takiego kanalu.");
                    }
                } else if (command.equals("select all")) {
                    JReader.selectAll();
                } else if (command.equals("select unread")) {
                    JReader.selectUnread();
                } else if (command.equals("select tag")) {
                    System.out.print("Wybierz tag (all - wszystkie, " + "untagged - nieoznaczone): ");
                    JReader.selectTag(in.readLine().replace("untagged", ""));
                } else if (command.equals("mark channel")) {
                    try {
                        System.out.print("Podaj numer kanalu: ");
                        int nr = new Integer(in.readLine()) - 1;
                        JReader.markChannelAsRead(JReader.getChannel(nr));
                    } catch (NumberFormatException nfe) {
                        System.out.println("Musisz podac liczbe calkowita.");
                    } catch (IndexOutOfBoundsException ioobe) {
                        System.out.println("Nie ma takiego kanalu.");
                    }
                } else if (command.equals("update channel")) {
                    int nr = 0;
                    try {
                        System.out.print("Podaj numer kanalu: ");
                        nr = new Integer(in.readLine()) - 1;
                        JReader.updateChannel(JReader.getChannel(nr));
                        JReader.getChannel(nr).setFail(false);
                        System.out.println("Kanal zostal zaktualizowany.");
                    } catch (SAXParseException spe) {
                        System.out.print("Nie mozna zaktualizowac kanalu.");
                        System.out.println(" Zrodlo nie jest prawidlowym plikiem XML.");
                        System.out.print("Blad w linii " + spe.getLineNumber() + ". ");
                        System.out.println("Szczegoly: " + spe.getLocalizedMessage());
                        JReader.getChannel(nr).setFail(true);
                    } catch (SAXException saxe) {
                        System.out.print("Nie mozna dodac kanalu.");
                        System.out.println(" Blad parsera XML.");
                        JReader.getChannel(nr).setFail(true);
                    } catch (SocketException se) {
                        System.out.println("Nie mozna zaktualizowac kanalu. Szczegoly:");
                        System.out.println(se.getLocalizedMessage());
                        JReader.getChannel(nr).setFail(true);
                    } catch (IOException ioe) {
                        System.out.print("Nie mozna zaktualizowac kanalu.");
                        System.out.println(" Brak polaczenia ze strona.");
                        JReader.getChannel(nr).setFail(true);
                    } catch (NumberFormatException nfe) {
                        System.out.println("Musisz podac liczbe calkowita.");
                    } catch (IndexOutOfBoundsException ioobe) {
                        System.out.println("Nie ma takiego kanalu.");
                    }
                } else if (command.equals("edit tags")) {
                    try {
                        System.out.print("Podaj numer kanalu: ");
                        int nr = new Integer(in.readLine()) - 1;
                        if (JReader.getChannel(nr).getTags().size() != 0) {
                            System.out.println("Tagi: " + JReader.getChannel(nr).getTagsAsString());
                        } else {
                            System.out.println("Ten kanal nie ma tagow.");
                        }
                        System.out.print("Podaj nowe tagi: ");
                        JReader.editTags(JReader.getChannel(nr), in.readLine());
                    } catch (NumberFormatException nfe) {
                        System.out.println("Musisz podac liczbe calkowita.");
                    } catch (IndexOutOfBoundsException ioobe) {
                        System.out.println("Nie ma takiego kanalu.");
                    }
                } else if (command.equals("remove channel")) {
                    try {
                        System.out.print("Podaj numer kanalu: ");
                        int nr = new Integer(in.readLine()) - 1;
                        JReader.removeChannel(nr);
                    } catch (NumberFormatException nfe) {
                        System.out.println("Musisz podac liczbe calkowita.");
                    } catch (IndexOutOfBoundsException ioobe) {
                        System.out.println("Nie ma takiego kanalu.");
                    }
                } else if (command.equals("set sort")) {
                    System.out.print("Sortuj wedlug (new/old): ");
                    String choice = in.readLine().trim();
                    if (choice.equals("old")) {
                        JReader.getConfig().setSortByNewest(false);
                        if (!JReader.getConfig().write()) {
                            System.out.println("Blad: zapisanie ustawien nie powiodlo sie.");
                        }
                    } else if (choice.equals("new")) {
                        JReader.getConfig().setSortByNewest(true);
                        if (!JReader.getConfig().write()) {
                            System.out.println("Blad: zapisanie ustawien nie powiodlo sie.");
                        }
                    } else {
                        System.out.println("Nieprawidlowy wybor.");
                    }
                } else if (command.equals("set delete")) {
                    if (JReader.getConfig().getDeleteOlderThanDays() == 0) {
                        System.out.println("Stare wiadomosci nie sa usuwane.");
                    } else {
                        System.out.println("Wiadomosci sa usuwane po " + JReader.getConfig().getDeleteOlderThanDays() + " dniach.");
                    }
                    System.out.print("Po ilu dniach usuwac wiadomosci? (0 - wcale): ");
                    JReader.getConfig().setDeleteOlderThanDays(new Integer(in.readLine().trim()));
                    if (!JReader.getConfig().write()) {
                        System.out.println("Blad: zapisanie ustawien nie powiodlo sie.");
                    }
                } else if (command.equals("set update")) {
                    System.out.println("Aktualizowanie kanalow po uruchomieniu: " + JReader.getConfig().getUpdateAllOnStartup());
                    System.out.print("Czy aktualizowac kanaly po uruchomieniu? (t/n) ");
                    if (in.readLine().toLowerCase().trim().equals("t")) {
                        JReader.getConfig().setUpdateAllOnStartup(true);
                    } else {
                        JReader.getConfig().setUpdateAllOnStartup(false);
                    }
                    if (!JReader.getConfig().write()) {
                        System.out.println("Blad: zapisanie ustawien nie powiodlo sie.");
                    }
                } else if (command.equals("set autoupdate")) {
                    if (JReader.getConfig().getAutoUpdateMinutes() == 0) {
                        System.out.println("Automatyczne aktualizowanie jest wylaczone.");
                    } else {
                        System.out.println("Automatyczne aktualizowanie co " + JReader.getConfig().getAutoUpdateMinutes() + " minut.");
                    }
                    System.out.print("Co ile minut aktualizowac kanaly? (0 - wcale): ");
                    int aum = new Integer(in.readLine().trim());
                    if (aum > 0 && JReader.getConfig().getAutoUpdateMinutes() == 0) {
                    }
                    JReader.getConfig().setAutoUpdateMinutes(aum);
                    if (!JReader.getConfig().write()) {
                        System.out.println("Blad: zapisanie ustawien nie powiodlo sie.");
                    }
                } else if (command.equals("import")) {
                    try {
                        System.out.print("Podaj lokalizacje pliku OPML: ");
                        if (JReader.importChannelList(in.readLine()) == 0) {
                            System.out.println("Brak kanalow do zaimportowania.");
                        }
                    } catch (SAXParseException spe) {
                        System.out.print("Nie mozna dokonac importu.");
                        System.out.println(" Zrodlo nie jest prawidlowym plikiem XML.");
                        System.out.print("Blad w linii " + spe.getLineNumber() + ". ");
                        System.out.println("Szczegoly: " + spe.getLocalizedMessage());
                    } catch (SAXException saxe) {
                        System.out.print("Nie mozna dodac kanalu.");
                        System.out.println(" Blad parsera XML.");
                    } catch (FileNotFoundException fnfe) {
                        System.out.print("Nie mozna dokonac importu.");
                        System.out.println(" Podany plik nie istnieje.");
                    } catch (IOException e) {
                        System.out.print("Nie mozna dokonac importu.");
                        System.out.println(" Odczytanie pliku nie powiodlo sie.");
                    }
                } else if (command.equals("export")) {
                    try {
                        System.out.print("Podaj plik docelowy: ");
                        JReader.exportChannelList(in.readLine());
                    } catch (IOException ioe) {
                        System.out.print("Nie mozna dokonac eksportu.");
                        System.out.println(" Zapisanie pliku jest niemozliwe.");
                    }
                } else if (!command.equals("") && !command.equals("quit")) {
                    System.out.println("Nieznane polecenie.");
                }
            }
        } catch (IOException ioe) {
            System.err.println("Blad interfejsu tekstowego.");
        }
    }
