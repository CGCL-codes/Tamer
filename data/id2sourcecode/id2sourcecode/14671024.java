    public DateChooser(Calendar date) {
        DateFormatSymbols sym = new DateFormatSymbols();
        months = sym.getShortMonths();
        String[] wkd = sym.getShortWeekdays();
        for (int i = 0; i < 7; i++) {
            int l = Math.min(wkd[i + 1].length(), 2);
            labels[i] = wkd[i + 1].substring(0, l);
        }
        highlightColor = UIManager.getColor("List.selectionBackground");
        disabledColor = Color.red;
        setBorder(BorderFactory.createEtchedBorder());
        setLayout(new BorderLayout(5, 5));
        JPanel top = new JPanel();
        top.setLayout(new BorderLayout(0, 0));
        top.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
        JPanel p1 = new JPanel();
        p1.setLayout(new BorderLayout());
        top.add(p1, BorderLayout.CENTER);
        b_lmonth = new JButton("<");
        b_lmonth.addActionListener(this);
        b_lmonth.setMargin(new Insets(0, 0, 0, 0));
        p1.add(b_lmonth, BorderLayout.WEST);
        l_month = new JLabel();
        l_date = new JLabel("Date");
        l_date.setAlignmentX(0);
        p1.add(l_date, BorderLayout.CENTER);
        b_rmonth = new JButton(">");
        b_rmonth.addActionListener(this);
        b_rmonth.setMargin(new Insets(0, 0, 0, 0));
        p1.add(b_rmonth, BorderLayout.EAST);
        add("North", top);
        calendarPane = new CalendarPane();
        calendarPane.setOpaque(false);
        add("Center", calendarPane);
        int fd = date.getFirstDayOfWeek();
        weekendCols[0] = (Calendar.SUNDAY - fd + 7) % 7;
        weekendCols[1] = (Calendar.SATURDAY - fd + 7) % 7;
        setSelectedDate(date);
    }
