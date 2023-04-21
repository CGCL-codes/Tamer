    public JWDE xml2Object(URL url, URL mappingURL) {
        Mapping mapping = new Mapping();
        Unmarshaller unmarshal = new Unmarshaller(JWDE.class);
        JWDE jwde = null;
        unmarshal.setIgnoreExtraElements(true);
        try {
            mapping.loadMapping(mappingURL);
            unmarshal.setMapping(mapping);
            XMLHelper.validate(url);
            InputSource is = new InputSource(url.openStream());
            jwde = (JWDE) unmarshal.unmarshal(is);
        } catch (IOException ioEx) {
            ioEx.printStackTrace();
        } catch (MappingException mapEx) {
            mapEx.printStackTrace();
        } catch (ValidationException valEx) {
            valEx.printStackTrace();
        } catch (MarshalException marEx) {
            marEx.printStackTrace();
        } catch (SAXException saxEx) {
            saxEx.printStackTrace();
        }
        return jwde;
    }
