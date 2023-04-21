    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Backyard ba = new Backyard(req, resp);
        ba.listenToChannel("kein/plan");
        System.out.println(ba.getChannel("kein/plan").getMembers());
        System.out.println(ba.getChannel("kein/plan").isMember(req.getSession().getId()));
    }
