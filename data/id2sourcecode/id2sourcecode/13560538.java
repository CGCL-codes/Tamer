    public void setAttributes(Attribute[] attrs) {
        for (int i = 0; i < attrs.length; i++) {
            if (attrs[i].getName().endsWith(".csv")) {
                Attribute[] attrs2 = new Attribute[attrs.length - 1];
                for (int j = 0; j < i; j++) {
                    attrs2[j] = attrs[j];
                }
                for (int j = i; j < attrs2.length; j++) {
                    attrs2[j] = attrs[j + 1];
                }
                attrs = attrs2;
                i = -1;
            }
        }
        super.setAttributes(attrs);
        allAttributes = null;
    }
