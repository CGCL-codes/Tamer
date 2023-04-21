    @SuppressWarnings("unchecked")
    public boolean reload(final OrmUjo ujo) {
        if (ujo == null) {
            return false;
        }
        final MetaTable metaTable = handler.findTableModel(ujo.getClass());
        final MetaPKey pkeys = MetaTable.PK.getValue(metaTable);
        boolean fk = ujo instanceof ExtendedOrmUjo;
        Criterion<OrmUjo> criterion = null;
        for (MetaColumn c : MetaPKey.COLUMNS.of(pkeys)) {
            Criterion<OrmUjo> crn = Criterion.where(c.getProperty(), c.getValue(ujo));
            criterion = criterion != null ? criterion.and(crn) : crn;
        }
        OrmUjo result = createQuery(criterion).uniqueResult();
        if (result == null) {
            return false;
        }
        ujo.writeSession(null);
        for (MetaColumn c : MetaTable.COLUMNS.of(metaTable)) {
            if (fk && c.isForeignKey()) {
                UjoProperty p = c.getProperty();
                ujo.writeValue(p, ((ExtendedOrmUjo) result).readFK(p));
            } else if (c.isColumn()) {
                c.getProperty().copy(result, ujo);
            }
        }
        ujo.writeSession(this);
        ujo.readChangedProperties(true);
        return true;
    }
