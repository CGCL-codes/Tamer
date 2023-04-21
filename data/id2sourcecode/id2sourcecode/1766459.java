    private static boolean test(String pattern, String fromDateString, String expectedResultString) throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date fromDate = dateFormat.parse(fromDateString);
        CronInterval c = new CronInterval(pattern);
        Date result = c.getNextTimeAfter(fromDate);
        String resultString = dateFormat.format(result);
        boolean ok = resultString.equals(expectedResultString);
        System.out.print(ok ? "  " : "X ");
        System.out.print(pattern);
        System.out.print(", ");
        System.out.print(fromDateString);
        System.out.print(" -> ");
        System.out.print(resultString);
        if (ok) System.out.println(", ok"); else {
            System.out.print(", exp ");
            System.out.println(expectedResultString);
        }
        return ok;
    }
