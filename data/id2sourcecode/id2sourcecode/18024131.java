    public void loadThemeStyle() {
        String resFile;
        URL url;
        resFile = System.getProperty("user.dir") + "/conf/themes/" + themeResFile;
        try {
            Properties props = new Properties();
            url = new URL("jar:file:" + resFile + "!/ressources/style");
            if (url != null) {
                try {
                    props.load(url.openStream());
                    if (props.getProperty("BG_TICKET_STATUS_INIT") != null) {
                        themeResStyle.put("BG_TICKET_STATUS_INIT", props.getProperty("BG_TICKET_STATUS_INIT"));
                    } else {
                        themeResStyle.put("BG_TICKET_STATUS_INIT", "#FF0000");
                    }
                    if (props.getProperty("BG_TICKET_STATUS_OPENED") != null) {
                        themeResStyle.put("BG_TICKET_STATUS_OPENED", props.getProperty("BG_TICKET_STATUS_OPENED"));
                    } else {
                        themeResStyle.put("BG_TICKET_STATUS_OPENED", "#FF0000");
                    }
                    if (props.getProperty("BG_TICKET_STATUS_CLOSED") != null) {
                        themeResStyle.put("BG_TICKET_STATUS_CLOSED", props.getProperty("BG_TICKET_STATUS_CLOSED"));
                    } else {
                        themeResStyle.put("BG_TICKET_STATUS_CLOSED", "#FF0000");
                    }
                    if (props.getProperty("BG_TICKET_STATUS_PENDING") != null) {
                        themeResStyle.put("BG_TICKET_STATUS_PENDING", props.getProperty("BG_TICKET_STATUS_PENDING"));
                    } else {
                        themeResStyle.put("BG_TICKET_STATUS_PENDING", "#FF0000");
                    }
                    if (props.getProperty("BG_TICKET_STATUS_CANCELLED") != null) {
                        themeResStyle.put("BG_TICKET_STATUS_CANCELLED", props.getProperty("BG_TICKET_STATUS_CANCELLED"));
                    } else {
                        themeResStyle.put("BG_TICKET_STATUS_CANCELLED", "#FF0000");
                    }
                } catch (IOException ex) {
                    CoreConfig.logger.error("error whille loading theme style!\n" + ex);
                }
            } else {
                CoreConfig.logger.error("unable to load ressource file for style : " + resFile);
            }
        } catch (MalformedURLException ex) {
            CoreConfig.logger.error("error whille loading theme style!\n" + ex);
        }
    }
