    private void mergeFileHookConfigurators(ArrayList configuratorList, ArrayList errors) {
        ClassLoader cl = getClass().getClassLoader();
        Enumeration hookConfigurators;
        try {
            hookConfigurators = cl != null ? cl.getResources(HookRegistry.HOOK_CONFIGURATORS_FILE) : ClassLoader.getSystemResources(HookRegistry.HOOK_CONFIGURATORS_FILE);
        } catch (IOException e) {
            errors.add(new FrameworkLogEntry(FrameworkAdaptor.FRAMEWORK_SYMBOLICNAME, FrameworkLogEntry.ERROR, 0, "getResources error on " + HookRegistry.HOOK_CONFIGURATORS_FILE, 0, e, null));
            return;
        }
        while (hookConfigurators.hasMoreElements()) {
            URL url = (URL) hookConfigurators.nextElement();
            try {
                Properties configuratorProps = new Properties();
                configuratorProps.load(url.openStream());
                String hooksValue = configuratorProps.getProperty(HOOK_CONFIGURATORS);
                if (hooksValue == null) continue;
                String[] configurators = ManifestElement.getArrayFromList(hooksValue, ",");
                for (int i = 0; i < configurators.length; i++) if (!configuratorList.contains(configurators[i])) configuratorList.add(configurators[i]);
            } catch (IOException e) {
                errors.add(new FrameworkLogEntry(FrameworkAdaptor.FRAMEWORK_SYMBOLICNAME, FrameworkLogEntry.ERROR, 0, "error loading: " + url.toExternalForm(), 0, e, null));
            }
        }
    }
