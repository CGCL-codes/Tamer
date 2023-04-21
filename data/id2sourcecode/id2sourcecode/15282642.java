    public void loadLogFromCookie() {
        String logAsString = CookieUtils.readCookie(SwingSDIApplet.this, "log");
        try {
            logStream = new ByteArrayInputStream(logAsString.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException uee) {
            uee.printStackTrace();
            write(uee.toString());
        }
        usarLog = true;
        write("El contenido del log es:\n");
        write("[" + CookieUtils.readCookie(SwingSDIApplet.this, "log"));
        write("]\n");
        reinit();
    }
