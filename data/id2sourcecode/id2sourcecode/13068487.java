    private VelQueryTemplates readVelQueryTemplates(URL url) throws IOException, JAXBException, SAXException {
        VelQueryTemplates templates = null;
        InputStream inputStream = null;
        try {
            inputStream = url.openStream();
            templates = this.unmarshallConfiguration(inputStream);
        } catch (JAXBException jaxbException) {
            logger.error("JAXBException nella lettura del template: [" + url.toExternalForm() + "]", jaxbException);
            throw jaxbException;
        } catch (SAXException saxException) {
            logger.error("SAXException nella lettura del template: [" + url.toExternalForm() + "]", saxException);
            throw saxException;
        } catch (IOException ioException) {
            logger.error("IOException nella lettura del template: [" + url.toExternalForm() + "]", ioException);
            throw ioException;
        } finally {
            LoadingUtils.quietlyClose(inputStream);
        }
        return templates;
    }
