    public ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
        TorData sd = (TorData) command;
        ArrayList<Integer> listadostepnych = new ArrayList<Integer>();
        int ilosc;
        String data;
        Date fordata;
        int godzinaOd;
        int godzinaDo;
        String miejscowosc;
        String lokal;
        String stanowisko;
        data = DateFormat.getDateInstance().format(sd.getFordata());
        fordata = sd.getFordata();
        godzinaOd = sd.getFgodzinaOd();
        godzinaDo = sd.getFgodzinaDo();
        miejscowosc = sd.getMiejscowosc();
        lokal = sd.getLokal();
        stanowisko = sd.getStanowisko();
        DateFormat dfgodz = new SimpleDateFormat("HH:mm:ss");
        DateFormat tf = new SimpleDateFormat("HH");
        Lista lgodzinaOd = new Lista();
        Lista lgodzinaDo = new Lista();
        LTylkoDostepne lltd = new LTylkoDostepne();
        String SgodzinaOd = "";
        String SgodzinaDo = "";
        Date gOd;
        int igOd;
        Date gDo;
        int igDo;
        int iterator;
        String q1;
        int flaga = -1;
        Connection con = HiberSession.getInstance().getSession().connection();
        Statement s = null;
        try {
            s = con.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        q1 = "SELECT Godzina_OD, Godzina_DO FROM " + "Rezerwacje WHERE " + "IDStanowiska = " + stanowisko + " AND Data = \'" + DateFormat.getDateInstance().format(sd.getFordata()) + "\' ";
        ResultSet rs = null;
        try {
            rs = s.executeQuery(q1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (rs.first() == false) {
            Rezerwacje r = null;
            if (request.getParameter("rezIdd") != null) r = RezerwacjeDAO.getInstance().load(Integer.valueOf(request.getParameter("rezIdd")), HiberSession.getInstance().getSession()); else r = new Rezerwacje();
            r.setData(fordata);
            r.setGodzinaOd(dfgodz.parse(sd.getGodzinaOd() + ":00:00"));
            r.setGodzinaDo(dfgodz.parse(sd.getGodzinaDo() + ":00:00"));
            r.setLokal(LokalDAO.getInstance().load(Integer.parseInt(lokal)));
            r.setStanowisko(StanowiskoDAO.getInstance().load(Integer.parseInt(stanowisko)));
            HttpSession hs = request.getSession();
            Integer idklienta = (Integer) hs.getAttribute("id");
            if (request.getParameter("rezIdd") == null) r.setKlient(KlientDAO.getInstance().load(idklienta));
            r.setRezerwacjaCzasowa("t");
            RezerwacjeDAO.getInstance().saveOrUpdate(r);
            ModelAndView mav = new ModelAndView("pages/rezToru");
            return mav;
        }
        try {
            while (rs.next()) {
                SgodzinaOd = rs.getString(1);
                gOd = tf.parse(SgodzinaOd);
                igOd = gOd.getHours();
                lgodzinaOd.getLista().addLast(igOd);
                SgodzinaDo = rs.getString(2);
                gDo = tf.parse(SgodzinaDo);
                igDo = gDo.getHours();
                lgodzinaDo.getLista().addLast(igDo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        try {
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            s.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
            ModelAndView mvc = new ModelAndView("pages/TorBlad");
            return mvc;
        }
        Rezerwacje r = null;
        if (request.getParameter("rezIdd") != null) r = RezerwacjeDAO.getInstance().load(Integer.valueOf(request.getParameter("rezIdd")), HiberSession.getInstance().getSession()); else r = new Rezerwacje();
        r.setData(fordata);
        r.setGodzinaOd(dfgodz.parse(sd.getGodzinaOd() + ":00:00"));
        r.setGodzinaDo(dfgodz.parse(sd.getGodzinaDo() + ":00:00"));
        r.setLokal(LokalDAO.getInstance().load(Integer.parseInt(lokal)));
        r.setStanowisko(StanowiskoDAO.getInstance().load(Integer.parseInt(stanowisko)));
        HttpSession hs = request.getSession();
        Integer idklienta = (Integer) hs.getAttribute("id");
        if (request.getParameter("rezIdd") == null) r.setKlient(KlientDAO.getInstance().load(idklienta));
        r.setRezerwacjaCzasowa("t");
        RezerwacjeDAO.getInstance().saveOrUpdate(r);
        System.out.println("Te godziny sa wolne");
        ModelAndView mav = new ModelAndView("pages/rezToru");
        return mav;
    }
