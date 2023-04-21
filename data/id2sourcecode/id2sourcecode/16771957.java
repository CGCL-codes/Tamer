    public boolean mapTokenProperties(PropertyMap character, boolean reading, Properties map, PropertyDescriptorSet pds) {
        if (map == null) return false;
        boolean changed = false;
        for (String mtProp : map.stringPropertyNames()) {
            String ctProp = map.getProperty(mtProp).trim();
            boolean oneWay = ctProp.startsWith("*");
            if (oneWay && reading) continue;
            if (oneWay) ctProp = ctProp.substring(1);
            PropertyDescriptor pd = pds.contains(ctProp) ? pds.get(ctProp) : null;
            boolean isScript = pd == null;
            if (isScript && reading) continue;
            if (isScript) {
                try {
                    Script script = AbstractScript.createScript(ctProp);
                    if (script == null) {
                        LOGGER.log(Level.WARNING, "The field '" + ctProp + "' has no script defined for it and is ignored.");
                        continue;
                    }
                    Object value = script.execute(character);
                    changed |= handler.write(mtProp, value);
                } catch (RuntimeException e) {
                    LOGGER.log(Level.WARNING, "Exception executing script to get property. The MT property '" + mtProp + "' could not be set:\n" + ctProp, e);
                }
                continue;
            }
            switch(pd.getType()) {
                case MAP:
                    if (!reading && pd.getMapProperties().getDefaultPropertyName() != null) changed |= handler.write(mtProp, character.get(ctProp));
                    break;
                case BOOLEAN:
                case NUMBER:
                case STRING:
                    if (reading) {
                        String value = handler.read(mtProp);
                        if (value == null || value.trim().length() == 0) break;
                        try {
                            character.put(ctProp, pd.getType().propertyFromString(value, pd.getDefaultValue()));
                        } catch (Exception e) {
                            LOGGER.log(Level.WARNING, "Property value of '" + mtProp + "' in token file not a valid string: " + value, e);
                        }
                    } else {
                        changed |= handler.write(mtProp, character.get(ctProp));
                    }
                    break;
                case SCRIPT:
                    if (!reading) changed |= handler.write(mtProp, character.get(ctProp));
                    break;
                case SLOT:
                    if (!reading && pd.getMapProperties() != null && pd.getMapProperties().getDefaultPropertyName() != null) changed |= handler.write(mtProp, character.get(ctProp));
                    break;
                case IMAGE:
                case LIST:
                    break;
                default:
                    assert false : "Unknown property type: " + ctProp + "=" + pd.getType().toString();
            }
        }
        return changed;
    }
