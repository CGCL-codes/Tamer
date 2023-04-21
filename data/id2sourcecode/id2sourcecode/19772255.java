    public static void openDefaultMailClient(List<String> attachmentList) {
        Desktop desktop = null;
        if (Desktop.isDesktopSupported()) {
            desktop = Desktop.getDesktop();
            if (desktop.isSupported(Desktop.Action.MAIL)) {
                URI uri = null;
                String attachmentString = "";
                for (String attachment : attachmentList) {
                    attachmentString += "&attachment=" + attachment;
                }
                try {
                    uri = new URI("mailto", "mailto:?SUBJECT=" + attachmentString, null);
                    desktop.mail(uri);
                } catch (IOException e) {
                    logger.log(Level.SEVERE, "", e);
                } catch (URISyntaxException e) {
                    logger.log(Level.SEVERE, "", e);
                }
                logger.info(LanguageResource.getLanguage().getString("info.email_client.open"));
                LoggingDesktopController.printInfo(LanguageResource.getLanguage().getString("info.email_client.open"));
            } else {
                logger.severe(LanguageResource.getLanguage().getString("error.no_desktop_support"));
                LoggingDesktopController.printError(LanguageResource.getLanguage().getString("error.no_desktop_support"));
            }
        } else {
            logger.severe(LanguageResource.getLanguage().getString("error.no_desktop_support"));
            LoggingDesktopController.printError(LanguageResource.getLanguage().getString("error.no_desktop_support"));
        }
    }
