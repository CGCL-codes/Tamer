                public void actionPerformed(ActionEvent e) {
                    String tivoName = getSelectedTivoName();
                    if (tivoName != null) {
                        String urlString = "http://" + config.TIVOS.get(tivoName) + "/TiVoConnect?Command=ResetServer";
                        String wan_port = config.getWanSetting(tivoName, "http");
                        if (wan_port != null) urlString = string.addPort(urlString, wan_port);
                        try {
                            URL url = new URL(urlString);
                            log.warn("Resetting " + tivoName + " TiVo: " + urlString);
                            url.openConnection();
                        } catch (Exception ex) {
                            log.error(ex.toString());
                        }
                    } else {
                        log.error("This command must be run with a TiVo tab selected.");
                    }
                }
