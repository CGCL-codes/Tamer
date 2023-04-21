    public BlogCalendar(Calendar calendar, String blogurl, Locale locale) {
        _locale = locale;
        _calendar = calendar;
        _today = new GregorianCalendar(_locale);
        _today.setTime(new Date());
        _symbols = new DateFormatSymbols(_locale);
        _blogURL = blogurl;
        _currentmonth = calendar.get(Calendar.MONTH);
        _currentyear = calendar.get(Calendar.YEAR);
        _currentday = calendar.get(Calendar.DAY_OF_MONTH);
        _dayswithentry = new Boolean[_calendar.getActualMaximum(Calendar.DAY_OF_MONTH)];
        Arrays.fill(_dayswithentry, Boolean.FALSE);
        _shortdownames = new String[7];
        String[] downames = _symbols.getShortWeekdays();
        for (int x = 0; x < _shortdownames.length; x++) {
            _shortdownames[x] = downames[x + 1];
        }
    }
