    private synchronized StringBuffer getUri(String sender, String addr, StringBuffer seite, String kodierung, int timeout, String meldung) {
        char[] zeichen = new char[1];
        try {
            long w = wartenBasis * Integer.parseInt(daten.system[Konstanten.SYSTEM_WARTEN_NR]);
            this.wait(w);
        } catch (Exception ex) {
            System.err.println("GetUrl.getUri: " + ex.getMessage());
        }
        daten.filmeLaden.incSeitenZaehler(sender);
        gibBescheid();
        user_agent = daten.system[Konstanten.SYSTEM_USER_AGENT_NR];
        seite.setLength(0);
        URLConnection conn = null;
        InputStream in = null;
        InputStreamReader inReader = null;
        try {
            URL url = new URL(addr);
            conn = url.openConnection();
            conn.setRequestProperty("User-Agent", user_agent);
            if (timeout > 0) {
                conn.setReadTimeout(timeout);
                conn.setConnectTimeout(timeout);
            }
            in = conn.getInputStream();
            inReader = new InputStreamReader(in, kodierung);
            while (!stop && inReader.read(zeichen) != -1) {
                seite.append(zeichen);
            }
        } catch (Exception ex) {
            if (!meldung.equals("")) {
                daten.fehler.fehlerMeldung("GetUrl.getUri für: ", meldung);
            }
            daten.fehler.fehlerMeldung(ex, "GetUrl.getUri für: " + addr);
        } finally {
            try {
                if (in != null) {
                    inReader.close();
                }
            } catch (IOException ex) {
            }
        }
        return seite;
    }
