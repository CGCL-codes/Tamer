    public void event(CometEvent ev) throws IOException, ServletException {
        log.debug("enter service backyard servlet");
        log.debug("enter BackyardServlet");
        HttpServletRequest req = ev.getHttpServletRequest();
        HttpServletResponse resp = ev.getHttpServletResponse();
        Backyard backyard = new Backyard(req, resp);
        backyard.setServlet(this);
        String data = req.getParameter("data");
        try {
            log.debug("Parsing JSON: " + data);
            JSONObject json = new JSONObject(data);
            String function = json.getString("fn");
            if (function.matches("handshake")) {
                log.debug("Handshake");
                PrintWriter pw = ev.getHttpServletResponse().getWriter();
                pw.print("{\"status\":\"OK\"}");
                pw.flush();
                pw.close();
                return;
            }
            if (function.matches("comet")) {
                log.debug("comet");
                if (req.getAttribute("de.jochenbrissier.byw.comet") != null && req.getAttribute("de.jochenbrissier.byw.comet").equals("nonTomcat")) {
                    backyard.startAsync();
                } else {
                    backyard.startAsync(ev);
                }
            }
            if (function.matches("listen")) {
                log.debug("listen");
                listenChannel(req, resp, json, backyard);
                ev.close();
            }
            if (function.matches("send")) {
                log.debug("sendtochannel");
                JSONObject channeldata = json.getJSONObject("channel");
                try {
                    backyard.getChannel(channeldata.getString("channel_name")).sendMessage(json.getString("data"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                ev.close();
            }
        } catch (Exception e) {
            log.warn(e);
        }
    }
