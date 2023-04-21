    @Override
    public void init() {
        global.setSystemLaF(true);
        if (getParameter("url") != null) if (!(getParameter("url").equals("none"))) {
            String in = "";
            try {
                URL url = new URL(getParameter("url"));
                BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
                in = "";
                String str;
                while ((str = reader.readLine()) != null) {
                    in += str;
                }
                reader.close();
                global.getManager().loadXml(in.toString());
            } catch (IOException ex) {
            }
        }
        if (getParameter("editable") != null) super.init();
    }
