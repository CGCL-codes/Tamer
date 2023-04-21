    public void writeTo(Element parentElement) {
        Loggers.SERVER.info("WebHookMainSettings: re-writing main settings");
        Loggers.SERVER.debug(this.getClass().getName() + ":writeTo :: " + parentElement.toString());
        Element el = new Element("webhooks");
        if (webHookMainConfig != null && webHookMainConfig.getProxyHost() != null && webHookMainConfig.getProxyHost().length() > 0 && webHookMainConfig.getProxyPort() != null && webHookMainConfig.getProxyPort() > 0) {
            el.addContent(webHookMainConfig.getProxyAsElement());
            Loggers.SERVER.debug(this.getClass().getName() + "writeTo :: proxyHost " + webHookMainConfig.getProxyHost().toString());
            Loggers.SERVER.debug(this.getClass().getName() + "writeTo :: proxyPort " + webHookMainConfig.getProxyPort().toString());
        }
        if (webHookMainConfig != null && webHookMainConfig.getInfoUrlAsElement() != null) {
            el.addContent(webHookMainConfig.getInfoUrlAsElement());
            Loggers.SERVER.debug(this.getClass().getName() + "writeTo :: infoText " + webHookMainConfig.getWebhookInfoText().toString());
            Loggers.SERVER.debug(this.getClass().getName() + "writeTo :: InfoUrl  " + webHookMainConfig.getWebhookInfoUrl().toString());
            Loggers.SERVER.debug(this.getClass().getName() + "writeTo :: show-reading  " + webHookMainConfig.getWebhookShowFurtherReading().toString());
        }
        parentElement.addContent(el);
    }
