    public static String Hora(String hora, boolean mostrar, boolean HoraCompleta) throws Exception {
        Date date;
        SimpleDateFormat formatter;
        if (hora.length() > 0) {
            if (mostrar) {
                formatter = new SimpleDateFormat("HH:mm:ss");
                date = (Date) formatter.parse(hora);
                String fecharet = formatter.format(date);
                if (HoraCompleta) {
                    formatter.applyPattern("HH:mm:ss");
                } else {
                    formatter.applyPattern("HH:mm");
                }
            } else {
                formatter = (hora.length() == 8 ? new SimpleDateFormat("HH:mm:ss") : new SimpleDateFormat("HH:mm"));
                date = (Date) formatter.parse(hora);
                String fecharet = formatter.format(date);
                formatter.applyPattern("HH:mm:ss");
            }
            return formatter.format(date).toString();
        } else {
            return "";
        }
    }
