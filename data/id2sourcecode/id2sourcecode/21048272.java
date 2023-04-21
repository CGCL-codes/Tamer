    public boolean existePossibilidadeDeAtualizacao(Bundle bundle) {
        boolean retorno = false;
        Dictionary<String, String> dictionary = bundle.getHeaders();
        String localBundleVersion = dictionary.get("Bundle-Version");
        String strURLJar = "jar:" + bundle.getLocation() + "!/";
        try {
            URL urlJar = new URL(strURLJar);
            JarURLConnection jarURLConnection = (JarURLConnection) urlJar.openConnection();
            JarFile jarFile = jarURLConnection.getJarFile();
            PluginWrapper pluginWrapper = new PluginWrapperImpl(jarFile, new URL(bundle.getLocation()));
            String remoteBundleVersion = pluginWrapper.getManifest().bundleVersion();
            retorno = !(localBundleVersion.equals(remoteBundleVersion));
        } catch (MalformedURLException ex) {
            Logger.getLogger(PluginsPanel.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ioException) {
            Logger.getLogger(PluginsPanel.class.getName()).log(Level.SEVERE, null, ioException);
        }
        return retorno;
    }
