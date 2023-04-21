    public Collection<?> newCollectionInstance(Class<?> type, Collection<?> values, Class<?>... fallbacks) {
        Collection newCollection = null;
        try {
            if (values != null && type.getConstructor(Collection.class) != null) {
                newCollection = (Collection) ReflectionUtils.newInstance(type, values);
            } else {
                newCollection = (Collection) ReflectionUtils.newInstance(type);
                if (values != null) {
                    newCollection.addAll(values);
                }
            }
        } catch (Exception e) {
            if (fallbacks != null && fallbacks.length > 0) {
                if (fallbacks.length == 1) {
                    newCollection = newCollectionInstance(fallbacks[0], values);
                } else {
                    Class<?>[] theOtherFallbacks = new Class<?>[fallbacks.length - 1];
                    for (int i = 1; i < fallbacks.length; i++) {
                        theOtherFallbacks[i - 1] = fallbacks[i];
                    }
                    newCollection = newCollectionInstance(fallbacks[0], values, theOtherFallbacks);
                }
            }
        }
        return newCollection;
    }
