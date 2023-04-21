    private String getType(String attName, boolean read, boolean write) {
        boolean allowed = true;
        if (attributes.containsKey(attName)) {
            MBeanAttributeInfo temp = (MBeanAttributeInfo) attributes.get(attName);
            if (read) {
                if (!temp.isReadable()) {
                    allowed = false;
                }
            }
            if (write) {
                if (!temp.isWritable()) {
                    allowed = false;
                }
            }
            if (!allowed) {
                return null;
            } else {
                return temp.getType();
            }
        } else {
            return null;
        }
    }
