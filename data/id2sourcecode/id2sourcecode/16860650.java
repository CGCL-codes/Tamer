    @Override
    protected Object overlayImage(Object object, Object image) {
        ComposedImage composedImage = getComposedImage(object, image);
        Collection<Object> images = composedImage.getImages();
        if (object instanceof Element) {
            Element element = (Element) object;
            for (Stereotype appliedStereotype : element.getAppliedStereotypes()) {
                Resource eResource = appliedStereotype.eResource();
                if (eResource != null) {
                    ResourceSet resourceSet = eResource.getResourceSet();
                    if (resourceSet != null) {
                        URIConverter uriConverter = resourceSet.getURIConverter();
                        URI normalizedURI = uriConverter.normalize(eResource.getURI());
                        for (Image icon : appliedStereotype.getIcons()) {
                            String location = icon.getLocation();
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
