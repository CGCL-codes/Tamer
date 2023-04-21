    public static java.util.Date getClassDateCompilation(Class<?> clazz) throws IOException {
        String className = clazz.getName();
        className = className.replaceAll("\\.", "/");
        className = "/" + className + ".class";
        URL url = Class.class.getResource(className);
        URLConnection urlConnection = url.openConnection();
        java.util.Date lastModified = new java.util.Date(urlConnection.getLastModified());
        return lastModified;
    }
