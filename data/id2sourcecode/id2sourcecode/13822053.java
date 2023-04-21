    public static InputStream getInputStream(Class<?> resourceClass, String resourceName, boolean searchParents) throws IOException {
        ExceptionUtils.throwIfNull(resourceClass, "resourceClass");
        ExceptionUtils.throwIfNull(resourceName, "resourceName");
        URL url = getResource(resourceClass, resourceName, searchParents);
        if (url == null) throw new IOException("unable to locate resource '" + resourceName + "' off of Class " + resourceClass == null ? "" : resourceClass.getName());
        return url.openStream();
    }
