    private void doAjax(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map<String, Object> root = new HashMap<String, Object>();
        resp.setContentType("text/plain");
        resp.setDateHeader("Expires", new Date(0).getTime());
        resp.setHeader("Cache-Control", "no-cache, must-revalidate");
        PrintWriter out = resp.getWriter();
        String sub = req.getParameter("sub");
        if (sub.equals("conf")) {
            runConfig(req, root);
            loadConfig(root);
        } else if (sub.equals("echoFrm")) {
            loadConfig(root);
        } else if (sub.equals("echoOverride")) {
            loadOverride(root);
        } else if (sub.equals("addOverride")) {
            doOverride(req, root);
        } else if (sub.equals("echoProg")) {
            doEchoProgram(req, root);
        } else if (sub.equals("rmOverride")) {
            doDelOverride(req, root);
        } else if (sub.equals("debug")) {
            loadDebug(root);
        } else if (sub.equals("log")) {
            BufferedReader r = new BufferedReader(new FileReader("sre.log"));
            String line;
            while ((line = r.readLine()) != null) out.write(line + "\n");
            r.close();
            return;
        } else if (sub.equals("lastRun")) {
            out.write(DataStore.getInstance().getSetting(DataStore.LAST_SCAN, DataStore.DEFAULT_LAST_SCAN));
            out.flush();
            return;
        } else if (sub.equals("saveDebug")) {
            saveDebug(Boolean.parseBoolean(req.getParameter("test_mode")), Boolean.parseBoolean(req.getParameter("test_monitor")));
        } else throw new IOException("Invalid submodule requested");
        JSONObject json = new JSONObject();
        Collection<?> list = (Collection<?>) root.get("errors");
        if (list != null) {
            JSONArray jarr = new JSONArray();
            for (Object o : list) jarr.put(o);
            try {
                json.put("errors", jarr);
            } catch (JSONException e) {
            }
        }
        for (String s : root.keySet()) {
            try {
                json.put(s, root.get(s));
            } catch (JSONException e) {
            }
        }
        out.write(json.toString());
        out.flush();
        return;
    }
