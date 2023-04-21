    protected java.lang.Class p_loadClass(java.lang.String Classname) throws java.lang.ClassNotFoundException {
        java.util.Iterator iter = p_Classpaths.iterator();
        while (iter.hasNext()) {
            try {
                java.net.URLConnection urlConnection = (new java.net.URL(((java.lang.String) (iter.next())).concat(Classname.replace('.', '/')).concat(".class"))).openConnection();
                int dataSize = urlConnection.getContentLength();
                byte[] data = new byte[dataSize];
                dataSize = (new java.io.BufferedInputStream(urlConnection.getInputStream())).read(data);
                java.lang.Class cls = defineClass(Classname, data, 0, dataSize);
                resolveClass(cls);
                p_LoadedClasses.put(Classname, new CClassLoader.CClassEntry(cls, (new java.util.Date()).getTime()));
                return cls;
            } catch (java.io.IOException Ex) {
            }
        }
        throw new java.lang.ClassNotFoundException("The requested class of name '" + Classname + "' could not be loaded from registered classpaths.");
    }
