    public void addAll(IQueryResult result) {
        addEntries(result.entries());
        for (Iterator iter = result.getMetaPropertiesSet().iterator(); iter.hasNext(); ) {
            MetaProperty metaProperty = (MetaProperty) iter.next();
            String propertyName = metaProperty.getName();
            try {
                Object propertyValue = result.getProperty(propertyName).getValue();
                if (this.getMetaPropertiesSet().hasMetaProperty(propertyName) && this.getMetaPropertiesSet().getMetaProperty(propertyName).isWriteable()) {
                    this.getProperty(propertyName).setValue(propertyValue);
                } else {
                    this.addProperty(new SimpleProperty(metaProperty, propertyValue));
                }
            } catch (InvalidPropertyOperationException ex) {
                _log.fatal("Cannot read readable property or write writeable property.");
            } catch (DuplicatePropertyNameException ex) {
                _log.fatal("Properties with duplicated names existed in original query result.");
            }
        }
    }
