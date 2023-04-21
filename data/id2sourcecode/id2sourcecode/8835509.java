    static String not_applicable(String Id) {
        MARSHAL m = new MARSHAL("The read/write are not applicable for " + Id);
        m.minor = Minor.Inappropriate;
        throw m;
    }
