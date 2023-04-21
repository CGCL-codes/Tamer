    protected Object overlayImage(Object object, Object image) {
        ComposedImage composedImage = getComposedImage(object, image);
        Collection images = composedImage.getImages();
        if (object instanceof Element) {
            Element element = (Element) object;
            for (Iterator appliedStereotypes = element.getAppliedStereotypes().iterator(); appliedStereotypes.hasNext(); ) {
                Stereotype appliedStereotype = (Stereotype) appliedStereotypes.next();
                Resource eResource = appliedStereotype.eResource();
                if (eResource != null) {
                    ResourceSet resourceSet = eResource.getResourceSet();
                    if (resourceSet != null) {
                        URIConverter uriConverter = resourceSet.getURIConverter();
                        URI normalizedURI = uriConverter.normalize(eResource.getURI());
                        for (Iterator icons = appliedStereotype.getIcons().iterator(); icons.hasNext(); ) {
                            String location = ((Image) icons.next()).getLocation();
                            if (!UML2Util.isEmpty(location) && location.indexOf("ovr16") != -1) {
                                URI uri = URI.createURI(location).resolve(normalizedURI);
                                try {
                                    URL url = new URL(uriConverter.normalize(uri).toString());
                                    url.openStream().close();
                                    images.add(url);
                                } catch (Exception e) {
                                }
                            }
                        }
                    }
                }
            }
        }
        if (AdapterFactoryEditingDomain.isControlled(object)) {
            images.add(getResourceLocator().getImage("full/ovr16/ControlledObject"));
        }
        return composedImage;
    }
