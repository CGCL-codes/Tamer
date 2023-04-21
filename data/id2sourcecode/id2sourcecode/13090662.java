    private Document validate(String pid) throws IOException, ValidationFailedException {
        final String login = username;
        final String password = this.password;
        Authenticator.setDefault(new Authenticator() {

            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(login, password.toCharArray());
            }
        });
        URL validatorurl = new URL(webservicelocation + pid);
        InputStream reply = null;
        reply = validatorurl.openStream();
        DocumentBuilderFactory fac = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        try {
            builder = fac.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            LOG.warn("the validator crashed", e);
            throw new ValidationFailedException("exceptions", "The.validator.crashed", null, null, null);
        }
        Document result = null;
        try {
            result = builder.parse(reply);
        } catch (SAXException e) {
            LOG.warn("the validator crashed", e);
            throw new ValidationFailedException("exceptions", "The.validator.crashed", null, null, null);
        }
        return result;
    }
