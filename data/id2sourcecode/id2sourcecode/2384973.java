    public ObjectStack(AdapterBase adapter, Class<?> c, Object o, DelayedInsertionBuffer delayBuffer) {
        this.adapter = adapter;
        while (c != null) {
            ObjectRepresentation rep = null;
            if (o != null) {
                rep = new ObjectRepresentation(adapter, c, o, delayBuffer);
            } else {
                rep = new ObjectRepresentation(adapter, c, delayBuffer);
            }
            representations.add(0, rep);
            c = c.getSuperclass();
        }
        for (int x = getSize() - 1; x >= 0; x--) {
            ObjectRepresentation rep = getRepresentation(x);
            for (int p = 0; p < rep.getPropertyCount(); p++) {
                String name = rep.getPropertyName(p);
                for (int y = x - 1; y >= 0; y--) {
                    ObjectRepresentation superRep = getRepresentation(y);
                    if (superRep.hasProperty(name)) {
                        rep.removeProperty(p);
                        p--;
                        break;
                    }
                }
            }
        }
        if (this.getActualRepresentation().isImplementation(Collection.class)) {
            ObjectRepresentation lastKnownImplementor = getActualRepresentation();
            for (int x = this.getSize() - 2; x >= 0; x--) {
                if (this.getRepresentation(x).isImplementation(Collection.class)) {
                    lastKnownImplementor = this.getRepresentation(x);
                } else {
                    break;
                }
            }
            lastKnownImplementor.implementCollection();
        }
        if (this.getActualRepresentation().isImplementation(Map.class)) {
            ObjectRepresentation lastKnownImplementor = getActualRepresentation();
            for (int x = this.getSize() - 2; x >= 0; x--) {
                if (this.getRepresentation(x).isImplementation(Map.class)) {
                    lastKnownImplementor = this.getRepresentation(x);
                } else {
                    break;
                }
            }
            lastKnownImplementor.implementMap();
        }
        if (!adapter.allowsEmptyStatements()) {
            for (int x = getSize() - 1; x >= 0; x--) {
                ObjectRepresentation rep = getRepresentation(x);
                if (rep.getPropertyCount() == 0) {
                    rep.addValuePair(Defaults.DUMMY_COL_NAME, null, short.class);
                }
            }
        }
    }
