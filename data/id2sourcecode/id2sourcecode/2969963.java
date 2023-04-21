    public static void main(String args[]) {
        SimpleRFC1123DateFormat sr = new SimpleRFC1123DateFormat();
        java.text.ParsePosition pp = new java.text.ParsePosition(0);
        Date dt = null;
        String arg1 = null;
        arg1 = "Sunday, 05-Jan-2000 08:42:03 GMT";
        dt = sr.parse(arg1, new java.text.ParsePosition(0));
        System.out.println(arg1 + " gives " + dt);
        arg1 = "Sun, 05-Jan-98 08:42:03 GMT";
        dt = sr.parse(arg1, new java.text.ParsePosition(0));
        System.out.println(arg1 + " gives " + dt);
        StringBuffer sbb = new StringBuffer();
        sr.format(dt, sbb, new java.text.FieldPosition(0));
        System.out.println(sbb);
        System.exit(0);
    }
