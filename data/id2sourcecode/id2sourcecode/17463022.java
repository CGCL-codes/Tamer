    private TValue put(PdfString key, TValue value, PdfReference nodeReference) {
        PdfDictionary node = (PdfDictionary) nodeReference.getDataObject();
        TValue oldValue;
        PdfArray children = (PdfArray) node.resolve(PdfName.Names);
        if (children == null) {
            children = (PdfArray) node.resolve(PdfName.Kids);
            int low = 0, high = children.size() - 1;
            while (true) {
                boolean matched = false;
                int mid = (low + high) / 2;
                PdfReference kidReference = (PdfReference) children.get(mid);
                PdfDictionary kid = (PdfDictionary) kidReference.getDataObject();
                PdfArray limits = (PdfArray) kid.resolve(PdfName.Limits);
                if (key.compareTo(limits.get(0)) < 0) {
                    high = mid - 1;
                } else if (key.compareTo(limits.get(1)) > 0) {
                    low = mid + 1;
                } else {
                    matched = true;
                }
                if (matched || low > high) {
                    Children kidChildren = getChildren(kid);
                    if (kidChildren.items.size() >= kidChildren.order) {
                        splitFullNode(children, mid, kidChildren.typeName);
                        if (key.compareTo(((PdfArray) kid.resolve(PdfName.Limits)).get(0)) < 0) {
                            kidReference = (PdfReference) children.get(mid);
                            kid = (PdfDictionary) kidReference.getDataObject();
                        }
                    }
                    oldValue = put(key, value, kidReference);
                    updateNodeLimits(node, children, PdfName.Kids);
                    break;
                }
            }
        } else {
            int childrenSize = children.size();
            int low = 0, high = childrenSize;
            while (true) {
                int mid = (mid = ((low + high) / 2)) - (mid % 2);
                if (mid >= childrenSize) {
                    oldValue = null;
                    children.add(key);
                    children.add(value.getBaseObject());
                    break;
                }
                int comparison = key.compareTo(children.get(mid));
                if (comparison < 0) {
                    high = mid - 2;
                } else if (comparison > 0) {
                    low = mid + 2;
                } else {
                    oldValue = wrap(children.get(mid + 1), (PdfString) children.get(mid));
                    children.set(mid, key);
                    children.set(++mid, value.getBaseObject());
                    break;
                }
                if (low > high) {
                    oldValue = null;
                    children.add(low, key);
                    children.add(++low, value.getBaseObject());
                    break;
                }
            }
            updateNodeLimits(node, children, PdfName.Names);
        }
        return oldValue;
    }
