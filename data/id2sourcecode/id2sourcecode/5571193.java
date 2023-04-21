    private static void addModulesInManifest(RegistryBuilder builder, URL url) {
        InputStream in = null;
        Throwable fail = null;
        try {
            in = url.openStream();
            Manifest mf = new Manifest(in);
            in.close();
            in = null;
            String list = mf.getMainAttributes().getValue(MODULE_BUILDER_MANIFEST_ENTRY_NAME);
            addModulesInList(builder, list);
        } catch (RuntimeException ex) {
            fail = ex;
        } catch (IOException ex) {
            fail = ex;
        } finally {
            close(in);
        }
        if (fail != null) throw new RuntimeException(String.format("Exception loading module(s) from manifest %s: %s", url.toString(), InternalUtils.toMessage(fail)), fail);
    }
