    private void cacheMovies() {
        _movies = new HashSet();
        Collection schedules = _xtvd.getSchedules();
        if (schedules != null) {
            Iterator schediter = schedules.iterator();
            while (schediter.hasNext()) {
                Schedule s = (Schedule) schediter.next();
                if (_matches.contains(s.getProgram())) {
                    Program p = (Program) _xtvd.getPrograms().get(s.getProgram());
                    Integer stationid = new Integer(s.getStation());
                    Station st = (Station) _xtvd.getStations().get(stationid);
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    _movies.add(new Movie(new String[] { sdf.format(s.getTime().getLocalDate()), getChannel(stationid) + "-" + st.getCallSign(), p.getTitle() + " (" + p.getYear() + ")", p.getStarRating().toString(), p.getMpaaRating().toString(), s.getDuration().getHours() + "h " + s.getDuration().getMinutes() + "m", p.getDescription() }));
                }
            }
        }
    }
