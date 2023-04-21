    private byte check(Attribute attribute, Child child, BaseDn baseDn, Method readMethod, Method writeMethod) {
        byte check = BYPASS_CASE;
        if (attribute != null) {
            check += ATTRIBUTE_CASE;
        }
        if (child != null) {
            check += CHILLD_CASE;
        }
        if (baseDn != null) {
            check += BASEDN_CASE;
        }
        if (readMethod == null || writeMethod == null) {
            check *= INVALID_CASE;
        }
        return check;
    }
