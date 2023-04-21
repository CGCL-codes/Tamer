    public ClassData getClassData(String name) {
        if (remoteBusy_class.get().booleanValue()) return null;
        remoteBusy_class.set(Boolean.TRUE);
        try {
            try {
                String resourceName = name.replace('.', '/').concat(".class");
                List<URL> resources = getResources(resourceName, true);
                if (resources == null || resources.isEmpty()) return null;
                URL url = resources.get(0);
                return new ClassData(url.openStream());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } finally {
            remoteBusy_class.set(Boolean.FALSE);
        }
    }
