    public static String Fecha(String fecha, boolean mostrar) throws Exception {
        Date date;
        SimpleDateFormat formatter;
        if (fecha.length() > 0) {
            if (mostrar) {
                formatter = new SimpleDateFormat("yy-MM-dd");
                date = (Date) formatter.parse(fecha);
                String fecharet = formatter.format(date);
                formatter.applyPattern("dd/MM/yy");
            } else {
                formatter = new SimpleDateFormat("dd/MM/yy");
                date = (Date) formatter.parse(fecha);
                String fecharet = formatter.format(date);
                formatter.applyPattern("yyyy-MM-dd");
            }
            return formatter.format(date).toString();
        } else {
            return "";
        }
    }
