            public Collection<EditProvider> getGeneratedAccessors() {
                Collection<EditProvider> accessors = new ArrayList<EditProvider>();
                if (readerSelected && writerSelected) accessors.add(new GeneratedAccessor(AttrAccessorNodeWrapper.ATTR_ACCESSOR, toString(), type, classNode)); else if (readerSelected) accessors.add(new GeneratedAccessor(AttrAccessorNodeWrapper.ATTR_READER, toString(), type, classNode)); else if (writerSelected) accessors.add(new GeneratedAccessor(AttrAccessorNodeWrapper.ATTR_WRITER, toString(), type, classNode));
                return accessors;
            }
