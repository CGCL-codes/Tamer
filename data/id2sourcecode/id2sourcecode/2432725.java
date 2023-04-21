    public String runMacro(ExternalHTTPRequests httpReq, String parm) {
        Hashtable parms = parseParms(parm);
        String last = httpReq.getRequestParameter("CHANNEL");
        if (parms.containsKey("RESET")) {
            if (last != null) httpReq.removeRequestParameter("CHANNEL");
            return "";
        }
        MOB mob = Authenticate.getAuthenticatedMob(httpReq);
        if (mob != null) {
            String lastID = "";
            for (int i = 0; i < CMLib.channels().getNumChannels(); i++) {
                String name = CMLib.channels().getChannelName(i);
                if ((last == null) || ((last.length() > 0) && (last.equals(lastID)) && (!name.equals(lastID)))) {
                    if (CMLib.channels().mayReadThisChannel(mob, i, true)) {
                        httpReq.addRequestParameters("CHANNEL", name);
                        return "";
                    }
                    last = name;
                }
                lastID = name;
            }
            httpReq.addRequestParameters("CHANNEL", "");
            if (parms.containsKey("EMPTYOK")) return "<!--EMPTY-->";
            return " @break@";
        }
        return " @break@";
    }
