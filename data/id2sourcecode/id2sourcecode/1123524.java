    public ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException exception) throws Exception {
        StolData sd = (StolData) command;
        ArrayList<Integer> listadostepnych = new ArrayList<Integer>();
        int ilosc;
        String data;
        Date fordata;
        int godzinaOd;
        int godzinaDo;
        String miejscowosc;
        String lokal;
        String stanowisko;
        System.out.print("wpisana data " + sd.getData() + "\n");
        System.out.print("wpisana godzina od " + sd.getGodzinaOd() + "\n");
        System.out.print("wpisana godzina do " + sd.getGodzinaDo() + "\n");
        System.out.print("wpisane miasto " + sd.getMiejscowosc() + "\n");
        System.out.print("wpisany lokal " + sd.getLokal() + "\n");
        System.out.print("wpisane stanowisko " + sd.getStanowisko() + "\n");
        System.out.print("wpisana godzina OD po prasowaniu " + sd.getFgodzinaOd() + "\n");
        System.out.print("wpisana godzina Do po prasowaniu " + sd.getFgodzinaDo() + "\n");
        System.out.print("wpisana data po sparsowaniu " + DateFormat.getDateInstance().format(sd.getFordata()) + "\n");
        data = DateFormat.getDateInstance().format(sd.getFordata());
        fordata = sd.getFordata();
        godzinaOd = sd.getFgodzinaOd();
        godzinaDo = sd.getFgodzinaDo();
        miejscowosc = sd.getMiejscowosc();
        lokal = sd.getLokal();
        stanowisko = sd.getStanowisko();
        DateFormat tf = new SimpleDateFormat("HH");
        Lista lgodzinaOd = new Lista();
        Lista lgodzinaDo = new Lista();
        LTylkoDostepne lltd = new LTylkoDostepne();
        String SgodzinaOd;
        String SgodzinaDo;
        Date gOd;
        int igOd;
        Date gDo;
        int igDo;
        int iterator;
        String q1;
        int flaga = -1;
        Connection con = HiberSession.getInstance().getSession().connection();
        Statement s = con.createStatement();
        q1 = "SELECT Godzina_OD, Godzina_DO FROM " + "Rezerwacje WHERE " + "IDStanowiska = " + stanowisko + " AND Data = \'" + DateFormat.getDateInstance().format(sd.getFordata()) + "\' ";
        System.out.println("\n BUM SZAKALAKA \n");
        System.out.println(q1);
        ResultSet rs = s.executeQuery(q1);
        while (rs.next()) {
            SgodzinaOd = rs.getString(1);
            gOd = tf.parse(SgodzinaOd);
            igOd = gOd.getHours();
            lgodzinaOd.getLista().addLast(igOd);
            System.out.println(SgodzinaOd);
            System.out.println(gOd);
            System.out.println(igOd);
            SgodzinaDo = rs.getString(2);
            gDo = tf.parse(SgodzinaDo);
            igDo = gDo.getHours();
            lgodzinaDo.getLista().addLast(igDo);
            System.out.println(SgodzinaDo);
            System.out.println(gDo);
            System.out.println(igDo);
        }
        rs.close();
        s.close();
        System.out.println("Dugosc listy - jej rozmiar listy nr 2 " + lgodzinaOd.getLista().size());
        ListaCzasow[] lczasow = new ListaCzasow[24];
        for (int c = 0; c < 24; c++) {
            lczasow[c] = new ListaCzasow();
            lczasow[c].dostepny = true;
        }
        for (int i = 0; i < lgodzinaOd.getLista().size(); i++) {
            for (int j = lgodzinaOd.getLista().get(i); j < lgodzinaDo.getLista().get(i); j++) {
                System.out.println("To jest " + i + " iteracja godzina Od wynosi " + lgodzinaOd.getLista().get(i) + " Godzina Do wynosi " + lgodzinaDo.getLista().get(i));
                lczasow[j].dostepny = false;
            }
        }
        for (int g = 0; g <= 23; g++) {
            if (lczasow[g].dostepny == true) {
                System.out.println("Godzina " + g + " jest dostepna");
                listadostepnych.add(g);
            } else {
                System.out.println("Godzina " + g + " jest niedostepna");
            }
        }
        for (int z = godzinaOd; z < godzinaDo; z++) {
            if (lczasow[z].dostepny == false) {
                flaga = 1;
            }
        }
        if (flaga == 1) {
            System.out.println("Te godziny sa juz zajeta");
            ModelAndView mvc = new ModelAndView("pages/gucio");
            mvc.addObject("rezerwacje", listadostepnych);
            return mvc;
        } else {
            System.out.println("Te godziny sa wolne");
        }
        ModelAndView mav = new ModelAndView("pages/rezStol");
        return mav;
    }
