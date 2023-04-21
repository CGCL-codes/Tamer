    public static Attribute2 transferFromAttr(Attribute origin) {
        String atype = origin.getType();
        Attribute2 a2 = null;
        if (Attribute.TYPE_ENUM.equals(atype)) {
            a2 = new EnumAttribute2();
            ((EnumAttribute) origin).transfer(a2);
        } else if (Attribute.TYPE_NUMBER.equals(atype)) {
            a2 = new NumericAttribute2();
            ((NumericAttribute) origin).transfer(a2);
        } else {
            a2 = new Attribute2();
            origin.transfer(a2);
        }
        return a2;
    }
