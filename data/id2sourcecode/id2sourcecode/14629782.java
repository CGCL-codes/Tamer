    public ContainmentModel createRootContainmentModel(URL url) throws ModelException {
        if (url.toString().endsWith(".xml")) {
            try {
                DefaultConfigurationBuilder builder = new DefaultConfigurationBuilder();
                Configuration config = builder.build(url.toString());
                final ContainmentProfile profile = CREATOR.createContainmentProfile(config);
                return createRootContainmentModel(profile);
            } catch (ModelException e) {
                throw e;
            } catch (Throwable e) {
                final String error = "Could not create model due to a build related error.";
                throw new ModelException(error, e);
            }
        }
        try {
            final URLConnection connection = url.openConnection();
            final InputStream stream = connection.getInputStream();
            final ContainmentProfile profile = BUILDER.createContainmentProfile(stream);
            return createRootContainmentModel(profile);
        } catch (Throwable e) {
            final String error = REZ.getString("factory.containment.create-url.error", url.toString());
            throw new ModelException(error, e);
        }
    }
