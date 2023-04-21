    private void setupSpecialPage(String vWiki, String specialPage) throws Exception {
        if (!exists(vWiki, specialPage)) {
            logger.debug("Setting up " + specialPage);
            write(vWiki, Topic.readDefaultTopic(specialPage), true, specialPage);
        }
    }
