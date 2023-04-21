    private static void loadFile(URL url, boolean load) {
        String mainClass = "PluginMain";
        try {
            URL[] urls = { url };
            URLClassLoader ucl = new URLClassLoader(urls);
            JarURLConnection uc = (JarURLConnection) url.openConnection();
            Attributes attr = uc.getMainAttributes();
            if (attr != null) mainClass = attr.getValue(Attributes.Name.MAIN_CLASS);
            if (mainClass == null) throw new Exception("Plugin specifies null main class.");
            System.out.println("Loading plugin: " + mainClass);
            Class cl = ucl.loadClass(mainClass);
            GenericPluginInterface plugin = (GenericPluginInterface) cl.newInstance();
            allPlugins.put(plugin.getName(), plugin);
            if (load) plugin.load(BotCoreStatic.getInstance());
            plugin.setGlobalDefaultSettings(BotCoreStatic.getInstance());
        } catch (ClassNotFoundException e) {
            System.err.println("   --> Load failed (Plugin '" + url + "' doesn't have main class: " + mainClass);
            e.printStackTrace();
        } catch (ClassCastException e) {
            System.err.println("   --> Load failed (Plugin '" + url + "''s main class doesn't implement PluginInterface");
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Unable to load plugin file: " + url);
            e.printStackTrace();
        }
    }
