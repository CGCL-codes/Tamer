    public static void write(org.omg.CORBA.portable.OutputStream out, AlreadyMasked _ob_v) {
        out.write_string(id());
        org.omg.CosTrading.ServiceTypeNameHelper.write(out, _ob_v.name);
    }
