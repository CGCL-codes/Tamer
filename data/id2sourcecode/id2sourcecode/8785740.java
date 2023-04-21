    public BlogCalendar(Calendar calendar, String blogurl, Locale locale) {
        _locale = locale;
        _calendar = calendar;
        _today = Calendar.getInstance(_locale);
        _today.setTime(new Date());
        _symbols = new DateFormatSymbols(_locale);
        _blogURL = blogurl;
        currentmonth = calendar.get(Calendar.MONTH);
        currentyear = calendar.get(Calendar.YEAR);
        currentday = calendar.get(Calendar.DAY_OF_MONTH);
        _dayswithentry = new Boolean[_calendar.getActualMaximum(Calendar.DAY_OF_MONTH)];
        Arrays.fill(_dayswithentry, Boolean.FALSE);
        _shortdownames = new String[7];
        String[] downames = _symbols.getShortWeekdays();
        if (_calendar.getFirstDayOfWeek() == Calendar.SUNDAY) {
            for (int x = 0; x < _shortdownames.length; x++) {
                _shortdownames[x] = downames[x + 1];
            }
        } else {
            for (int x = 2; x <= _shortdownames.length; x++) {
                _shortdownames[x - 2] = downames[x];
            }
            _shortdownames[6] = downames[1];
        }
    }
