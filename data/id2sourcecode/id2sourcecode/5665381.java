    @Override
    public Class createClass(String name, ClassLoader topLevelLoader, InputStream source) throws ClassNotFoundException {
        if (!caching()) {
            return super.createClass(name, topLevelLoader, source);
        }
        try {
            Logging.dbg().fine("ACGIModelClassFactory:: Reading class " + name + "...");
            Object model = null;
            synchronized (topLevelLoader) {
                BufferingInputStream bis = new BufferingInputStream(source);
                MessageDigest md = MessageDigest.getInstance(DigestType);
                DigestInputStream dis = new DigestInputStream(bis, md);
                while (dis.read() >= 0) ;
                byte[] digest = md.digest();
                InputStream cache = getCache(name, digest);
                if (cache == null) {
                    bis.resetToStart();
                    OutputStream os = createCache(name, digest);
                    model = readModelWhileCaching(bis, os);
                    bis.close();
                    os.close();
                    installCache(name);
                    Logging.dbg().fine("ACGIModelClassFactory:: Class " + name + " cached.");
                } else {
                    bis.close();
                    model = readCachedModel(cache);
                    cache.close();
                    Logging.dbg().fine("ACGIModelClassFactory:: Reading cached version of " + name + ".");
                }
            }
            GenericInterpreterClassLoader cl = new GenericInterpreterClassLoader(topLevelLoader, name, this.getModelInterface(), null, model);
            Class c = cl.getModelInterpreterClass();
            Logging.dbg().fine("ACGIModelClassFactory:: Class " + name + " loaded.");
            return c;
        } catch (Exception exc) {
            exc.printStackTrace();
            Logging.dbg().severe("ACGIModelClassFactory:: ERROR loading class " + name + ".");
            throw new ClassNotFoundException("Could not create class '" + name + "'.", exc);
        }
    }
