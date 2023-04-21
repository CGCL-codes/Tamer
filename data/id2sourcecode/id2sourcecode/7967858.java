    private DayTransactionsPaper parseDia(String idPapel, String reg) {
        double aber, fech, med, min, max;
        int tit;
        if (reg == null) return null;
        int start = 0;
        int end = reg.indexOf(",", start);
        Date data = parseDateFormat2(reg.substring(start, end));
        start = end + 1;
        end = reg.indexOf(",", start);
        aber = Double.parseDouble(reg.substring(start, end));
        start = end + 1;
        end = reg.indexOf(",", start);
        max = Double.parseDouble(reg.substring(start, end));
        start = end + 1;
        end = reg.indexOf(",", start);
        min = Double.parseDouble(reg.substring(start, end));
        med = (max + min) / 2;
        start = end + 1;
        end = reg.indexOf(",", start);
        fech = Double.parseDouble(reg.substring(start, end));
        start = end + 1;
        end = reg.indexOf(",", start);
        tit = TextParser.getInteger(reg.substring(start, end));
        return new DayTransactionsPaper(idPapel, data, aber, fech, max, med, min, tit, moeda);
    }
