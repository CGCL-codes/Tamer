    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.debug("enter service backyard servlet");
        log.debug("enter BackyardServlet");
        Backyard backyard = new Backyard(req, resp);
        backyard.setServlet(this);
        String data = req.getParameter("data");
        try {
            log.debug("Parsing JSON: " + data);
            JSONObject json = new JSONObject(data);
            String function = json.getString("fn");
            if (function.matches("handshake")) {
                log.debug("Handshake");
                resp.getWriter().print("{\"status\":\"OK\",\"id\":\"" + req.getSession().getId() + "\"}");
                return;
            }
            if (function.matches("comet")) {
                log.debug("comet");
                backyard.startAsync();
            }
            if (function.matches("listen")) {
                log.debug("listen");
                listenChannel(req, resp, json, backyard);
            }
            if (function.matches("send")) {
                log.debug("sendtochannel");
                JSONObject channeldata = json.getJSONObject("channel");
                try {
                    backyard.getChannel(channeldata.getString("channel_name")).sendMessage(json.getString("data"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (ParseException e) {
            log.warn(e);
        }
    }
