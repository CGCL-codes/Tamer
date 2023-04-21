    public void addBinaryContext(BinaryContext bc, boolean overwrite) throws AlreadyExistsException, InvalidTypeException {
        Vector<String> newObjects = bc.getObjects();
        for (int i = 0; i < newObjects.size(); i++) {
            String objName = newObjects.elementAt(i);
            try {
                addObject(objName);
            } catch (AlreadyExistsException e) {
                LMLogger.logWarning(e, false);
            }
        }
        isModified = true;
        Vector<String> newAttributes = bc.getAttributes();
        for (int i = 0; i < newAttributes.size(); i++) {
            String attName = newAttributes.elementAt(i);
            if (!overwrite) {
                int tryCount = 0;
                int idx = getAttributeIndex(attName);
                while (idx > -1) {
                    tryCount++;
                    idx = getAttributeIndex(attName + "_" + tryCount);
                }
                if (tryCount > 0) {
                    attName = attName + "_" + tryCount;
                    newAttributes.setElementAt(attName, i);
                }
            }
            addAttribute(attName);
        }
        for (int i = 0; i < newObjects.size(); i++) {
            String objName = newObjects.elementAt(i);
            for (int j = 0; j < newAttributes.size(); j++) {
                String attName = newAttributes.elementAt(j);
                String value = bc.getValueAt(i, j);
                if (value.equals(TRUE)) {
                    int objIdx = getObjectIndex(objName);
                    int attIdx = getAttributeIndex(attName);
                    String oldValue = getValueAt(objIdx, attIdx);
                    setValueAt(TRUE, objIdx, attIdx);
                    if (!oldValue.equals(TRUE) && value.equals(TRUE)) countTRUE++; else if (oldValue.equals(TRUE) && !value.equals(TRUE)) countTRUE--;
                }
            }
        }
    }
