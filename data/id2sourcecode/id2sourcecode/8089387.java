    protected void addCatalogReplace(DVector DV, Environmental E) {
        int start = 0;
        int end = DV.size() - 1;
        String name = E.Name();
        int lastStart = 0;
        int lastEnd = DV.size() - 1;
        int comp = -1;
        int mid = -1;
        while (start <= end) {
            mid = (end + start) / 2;
            comp = ((Environmental) DV.elementAt(mid, 1)).Name().compareToIgnoreCase(name);
            if (comp == 0) break; else if (comp > 0) {
                lastEnd = end;
                end = mid - 1;
            } else {
                lastStart = start;
                start = mid + 1;
            }
        }
        if (comp == 0) {
            if (E instanceof DBIdentifiable) ((DBIdentifiable) E).setDatabaseID(((DBIdentifiable) DV.elementAt(mid, 1)).databaseID());
            ((Environmental) DV.elementAt(mid, 1)).destroy();
            DV.setElementAt(mid, 1, E);
        } else {
            if (mid >= 0) for (comp = lastStart; comp <= lastEnd; comp++) if (((Environmental) DV.elementAt(comp, 1)).Name().compareToIgnoreCase(name) > 0) {
                DV.insertElementAt(comp, E, new CataDataImpl(""));
                return;
            }
            DV.addElement(E, new CataDataImpl(""));
        }
    }
