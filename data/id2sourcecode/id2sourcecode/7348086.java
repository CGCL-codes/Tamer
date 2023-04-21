    public Method[] getMeths(final String name, final Type[] params, final Type caller) {
        Method[] matches;
        try {
            final Type myType = type.retrieveType();
            final Type superType = myType.isInterface() ? Type.objectType : myType.getSuperclass();
            matches = superType.getMeths(name, params, caller);
        } catch (ClassNotFoundException classEx) {
            matches = new Method[0];
        }
        int low = 0;
        int high = methods.length - 1;
        while (low <= high) {
            final int mid = (low + high) / 2;
            final int compare = name.compareTo(methods[mid].getName());
            if (compare < 0) {
                high = mid - 1;
            } else if (compare > 0) {
                low = mid + 1;
            } else {
                low = mid;
                high = mid - 1;
            }
        }
        int index;
        for (index = low; index >= 0 && index < methods.length && name.equals(methods[index].getName()); index--) ;
        for (int i = index + 1; i < methods.length && name.equals(methods[i].getName()); i++) {
            if (methods[i].match(params, caller)) {
                final Method[] newMatches = new Method[matches.length + 1];
                System.arraycopy(matches, 0, newMatches, 0, matches.length);
                newMatches[matches.length] = methods[i];
                matches = newMatches;
            }
        }
        return matches;
    }
