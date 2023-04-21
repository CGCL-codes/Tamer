    @IAppEventHandler(appEvent = "initLockProcess")
    public ActionForward initLockProcess(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        DynaValidatorForm objForm = (DynaValidatorForm) form;
        Tournament logicTour = new Tournament();
        List tournamentList = logicTour.fetchActiveTournament();
        if (tournamentList != null && tournamentList.size() > 0) {
            request.getSession().setAttribute("opt_tournament_id", tournamentList);
            String tournament_id = request.getParameter("tournament_id");
            if (tournament_id == null) {
                RefxVO objVO = (RefxVO) tournamentList.get(0);
                tournament_id = objVO.getRefx_value();
            }
            LCTournamentVO tourVO = logicTour.fetchFullDetailsofTournament(tournament_id);
            String next_eff_date = tourVO.getNext_effective_date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date effDate = sdf.parse(next_eff_date);
            sdf.applyPattern("dd-MM-yyyy");
            tourVO.setNext_effective_date(sdf.format(effDate));
            System.out.println("TOURNAMENT ID: " + tournament_id + "next eff" + next_eff_date);
            objForm.set("objLCTournament", tourVO);
        } else {
            request.setAttribute(LCConstants.STATUS_MESSAGE_CODE, "no_records");
        }
        request.getSession().setAttribute("opt_tournament_id", tournamentList);
        return mapping.findForward("locktournamentdate");
    }
