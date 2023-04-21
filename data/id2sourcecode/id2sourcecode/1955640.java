    public static String Fecha2(String fecha, boolean mostrar) {
        try {
            Date date;
            SimpleDateFormat formatter;
            if (fecha.length() > 0) {
                if (mostrar) {
                    formatter = new SimpleDateFormat("yyyy-MM-dd");
                    date = (Date) formatter.parse(fecha);
                    String fecharet = formatter.format(date);
                    formatter.applyPattern("dd/MM/yyyy");
                } else {
                    formatter = new SimpleDateFormat("dd/MM/yyyy");
                    date = (Date) formatter.parse(fecha);
                    String fecharet = formatter.format(date);
                    formatter.applyPattern("yyyy-MM-dd");
                }
                return formatter.format(date).toString();
            } else {
                return "";
            }
        } catch (Exception x) {
            OP_Proced.Mensaje("Error al convertir la fecha", "Error");
            return "";
        }
    }
