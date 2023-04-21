    private static IndexedPropertyDescriptor _createMergedIndexedDescriptor(PropertyDescriptor secondaryDescriptor, PropertyDescriptor primaryDescriptor) {
        Method readMethod = _getMergedReadMethod(secondaryDescriptor, primaryDescriptor);
        Method writeMethod = _getMergedWriteMethod(secondaryDescriptor, primaryDescriptor);
        Method indexedReadMethod = null;
        Method indexedWriteMethod = null;
        if (secondaryDescriptor instanceof IndexedPropertyDescriptor) {
            IndexedPropertyDescriptor iSecondaryDescriptor = (IndexedPropertyDescriptor) secondaryDescriptor;
            readMethod = iSecondaryDescriptor.getReadMethod();
            writeMethod = iSecondaryDescriptor.getWriteMethod();
            indexedReadMethod = iSecondaryDescriptor.getIndexedReadMethod();
            indexedWriteMethod = iSecondaryDescriptor.getIndexedWriteMethod();
        }
        if (primaryDescriptor instanceof IndexedPropertyDescriptor) {
            IndexedPropertyDescriptor iPrimaryDescriptor = (IndexedPropertyDescriptor) primaryDescriptor;
            Method tempMethod = iPrimaryDescriptor.getIndexedReadMethod();
            if (tempMethod != null) {
                indexedReadMethod = tempMethod;
            }
            tempMethod = iPrimaryDescriptor.getIndexedWriteMethod();
            if (tempMethod != null) {
                indexedWriteMethod = tempMethod;
            }
        }
        try {
            IndexedPropertyDescriptor mergedIndexedDescriptor = new IndexedPropertyDescriptor(primaryDescriptor.getName(), readMethod, writeMethod, indexedReadMethod, indexedWriteMethod);
            _mergePropertyDescriptors(secondaryDescriptor, primaryDescriptor, mergedIndexedDescriptor);
            return mergedIndexedDescriptor;
        } catch (Exception e) {
            return null;
        }
    }
