    public static void write(org.omg.CORBA.portable.OutputStream ostream, org.omg.PersonIdService.TraitSpec value) {
        org.omg.PersonIdService.TraitNameHelper.write(ostream, value.trait);
        ostream.write_boolean(value.mandatory);
        ostream.write_boolean(value.read_only);
        ostream.write_boolean(value.searchable);
    }
