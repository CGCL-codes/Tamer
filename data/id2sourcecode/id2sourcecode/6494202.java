    String getHTMLDescription(String action) {
        try {
            String rsname = "html/" + action + "_" + Locale.getDefault().getLanguage() + ".html";
            URL url = this.getClass().getResource(rsname);
            Reader br;
            try {
                br = new BufferedReader(new InputStreamReader(url.openStream(), "utf8"));
            } catch (NullPointerException npe1) {
                try {
                    br = new BufferedReader(new InputStreamReader(this.getClass().getResource("html/" + action + "_en.html").openStream(), "utf8"));
                } catch (NullPointerException npe2) {
                    return "<html>" + DemoApp.getRes().getString("No description for {0} found...", action) + "</html>";
                }
            }
            char[] buffer = new char[4096];
            String t = "";
            while (br.read(buffer) != -1) {
                t = t.concat(new String(buffer));
            }
            br.close();
            return t;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "<html>" + DemoApp.getRes().getString("No description for {0} found...", action) + "</html>";
    }
