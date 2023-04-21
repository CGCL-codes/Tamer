    public int[] getProperties(JxplElement atom) {
        int[] a, properties;
        properties = null;
        a = (int[]) atoms.get(atom);
        if (a != null) {
            properties = new int[a.length - 1];
            for (int i = 0; i < properties.length; i++) properties[i] = a[i + 1];
        }
        return properties;
    }
