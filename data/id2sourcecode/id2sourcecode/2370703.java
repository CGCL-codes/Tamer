    @IAppEventHandler(appEvent = "lockTournamentDay")
    public ActionForward lockTournamentDay(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String tournament_id = request.getParameter("tournament_id");
        String next_eff_date = request.getParameter("next_effective_day");
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        Date dateNed = sdf.parse(next_eff_date.trim());
        sdf.applyPattern("yyyy-MM-dd HH:mm:ss");
        String formatted_next_eff_date = sdf.format(dateNed);
        ProcessJobs.lockTournamentChanges(tournament_id.trim(), formatted_next_eff_date);
        return mapping.findForward("success");
    }
