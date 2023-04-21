    public List getAttachedEditParts(Object guide, GraphicalViewer viewer) {
        List attachedModelObjects = getAttachedModelObjects(guide);
        List attachedEditParts = new ArrayList(attachedModelObjects.size());
        Iterator i = attachedModelObjects.iterator();
        while (i.hasNext()) {
            Object editPart = viewer.getEditPartRegistry().get(i.next());
            if (editPart != null) attachedEditParts.add(editPart);
        }
        return attachedEditParts;
    }
