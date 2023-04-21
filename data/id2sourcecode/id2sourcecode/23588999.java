    public Properties getProperties(final PropertiesResourceType propertiesResourceType) throws BuildException {
        if (propertiesResourceType == null) {
            throw new NullPointerException("propertiesResourceType");
        }
        InputStream in = null;
        boolean suppressExceptionOnClose = true;
        final Properties properties = new Properties();
        final URL url = this.getResource(propertiesResourceType.getLocation());
        try {
            if (url != null) {
                final URLConnection con = url.openConnection();
                con.setConnectTimeout(propertiesResourceType.getConnectTimeout());
                con.setReadTimeout(propertiesResourceType.getReadTimeout());
                con.connect();
                in = con.getInputStream();
                if (propertiesResourceType.getFormat() == PropertiesFormatType.PLAIN) {
                    properties.load(in);
                } else if (propertiesResourceType.getFormat() == PropertiesFormatType.XML) {
                    properties.loadFromXML(in);
                }
            } else if (propertiesResourceType.isOptional()) {
                this.log(Messages.getMessage("propertiesNotFound", propertiesResourceType.getLocation()), Project.MSG_WARN);
            } else {
                throw new BuildException(Messages.getMessage("propertiesNotFound", propertiesResourceType.getLocation()), this.getLocation());
            }
            suppressExceptionOnClose = false;
        } catch (final SocketTimeoutException e) {
            final String message = Messages.getMessage(e);
            if (propertiesResourceType.isOptional()) {
                this.getProject().log(Messages.getMessage("resourceTimeout", message != null ? " " + message : ""), e, Project.MSG_WARN);
            } else {
                throw new BuildException(Messages.getMessage("resourceTimeout", message != null ? " " + message : ""), e, this.getLocation());
            }
        } catch (final IOException e) {
            final String message = Messages.getMessage(e);
            if (propertiesResourceType.isOptional()) {
                this.getProject().log(Messages.getMessage("resourceFailure", message != null ? " " + message : ""), e, Project.MSG_WARN);
            } else {
                throw new BuildException(Messages.getMessage("resourceFailure", message != null ? " " + message : ""), e, this.getLocation());
            }
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (final IOException e) {
                if (suppressExceptionOnClose) {
                    this.logMessage(Level.SEVERE, Messages.getMessage(e), e);
                } else {
                    throw new BuildException(Messages.getMessage(e), e, this.getLocation());
                }
            }
        }
        return properties;
    }
