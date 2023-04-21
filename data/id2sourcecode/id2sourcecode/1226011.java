    public DateUtil(Date data) {
        this();
        DateFormat df = new SimpleDateFormat("yyyyMMdd");
        try {
            this.data = df.parse(df.format(data));
        } catch (ParseException e) {
        }
        calendari.setTime(data);
        this.dia = calendari.get(Calendar.DAY_OF_MONTH);
        this.mes = calendari.get(Calendar.MONTH) + 1;
        this.any = calendari.get(Calendar.YEAR);
    }
