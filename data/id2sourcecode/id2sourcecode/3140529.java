    public void beginImport(Xtvd xtvd) {
        java.util.Map stations = xtvd.getStations();
        for (Iterator lineups = xtvd.getLineups().entrySet().iterator(); lineups.hasNext(); ) {
            Lineup lineup = (Lineup) ((java.util.Map.Entry) lineups.next()).getValue();
            for (Iterator maps = lineup.getMap().iterator(); maps.hasNext(); ) {
                Map map = (Map) maps.next();
                int stationId = map.getStation();
                String channel = map.getChannel();
                Station station = (Station) stations.get(new Integer(stationId));
                HashMap curChannel = new HashMap();
                curChannel.put(ChannelData.DISPLAYNAME, channel + " " + station.getCallSign());
                channelData.add(station.getCallSign(), curChannel);
            }
        }
        for (Iterator schedules = xtvd.getSchedules().iterator(); schedules.hasNext(); ) {
            HashMap programItems = new HashMap();
            Schedule schedule = (Schedule) schedules.next();
            DateTime start = schedule.getTime();
            programItems.put(ProgramData.START, Utilities.dateToXMLTVDate(start.getLocalDate()));
            Duration stop = schedule.getDuration();
            Calendar stopTime = Calendar.getInstance();
            stopTime.setTime(start.getLocalDate());
            stopTime.add(Calendar.HOUR_OF_DAY, Integer.parseInt(stop.getHours()));
            stopTime.add(Calendar.MINUTE, Integer.parseInt(stop.getMinutes()));
            programItems.put(ProgramData.STOP, Utilities.dateToXMLTVDate(stopTime.getTime()));
            programItems.put(ProgramData.STEREO, "" + schedule.getStereo());
            TvRatings tvRating = schedule.getTvRating();
            if (tvRating != null) {
                programItems.put(ProgramData.RATING, tvRating.toString());
            }
            programItems.put(ProgramData.SUBTITLES, "" + schedule.getSubtitled());
            Station station = (Station) xtvd.getStations().get(new Integer(schedule.getStation()));
            programItems.put(ProgramData.CHANNEL, station.getCallSign());
            for (Iterator lineups = xtvd.getLineups().entrySet().iterator(); lineups.hasNext(); ) {
                Lineup lineup = (Lineup) ((java.util.Map.Entry) lineups.next()).getValue();
                for (Iterator maps = lineup.getMap().iterator(); maps.hasNext(); ) {
                    Map map = (Map) maps.next();
                    if (map.getStation() == station.getId()) {
                    }
                }
            }
            Program program = (Program) xtvd.getPrograms().get(schedule.getProgram());
            programItems.put(ProgramData.TITLE, program.getTitle());
            try {
                if (start != null && program.getOriginalAirDate() != null && program.getShowType() != null && program.getShowType().equalsIgnoreCase("Series") && MyTellyMainFrame.getConfig().getFirstRunDate()) {
                    String dateStringFmt = "";
                    SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
                    try {
                        dateStringFmt = outputFormat.format(start.getDate()).toUpperCase();
                    } catch (Exception ee) {
                        System.err.println("CMF2007.09.20 Start Date error e:" + ee);
                        throw new Exception(ee);
                    }
                    if (dateStringFmt.equals(program.getOriginalAirDate().toString()) && schedule.getRepeat()) {
                        if (MyTellyMainFrame.getConfig().getDebug()) {
                            System.out.println("CMF2007.09.20 Maybe not a repeat " + program.getTitle() + ",  currentAirDate:" + dateStringFmt + ":, getOriginalAirDate:" + program.getOriginalAirDate());
                        }
                        schedule.setRepeat(false);
                    }
                }
            } catch (Exception eee) {
                System.err.println("CMF2007.09.20 Start Date error e:" + eee);
            }
            programItems.put(ProgramData.PREVIOUSLYSHOWN, "" + schedule.getRepeat());
            programItems.put(ProgramData.SUBTITLE, program.getSubtitle());
            MpaaRatings mpaaRatings = program.getMpaaRating();
            if (mpaaRatings != null) {
                programItems.put(ProgramData.MPAA, mpaaRatings.toString());
            }
            String description = program.getDescription();
            StringBuffer desc = new StringBuffer();
            if (description != null) {
                desc.append(description);
            }
            String year = program.getYear();
            if (year != null && year.length() != 0) {
                desc.append(" (" + year + ")");
            }
            if (mpaaRatings != null && mpaaRatings.toString().length() > 0) {
                desc.append(" Rated:" + mpaaRatings.toString());
                StarRating starRat = program.getStarRating();
                if (starRat != null) {
                    desc.append(' ' + starRat.toString());
                }
            }
            if (schedule.getCloseCaptioned()) {
                desc.append(" (CC)");
            }
            if (schedule.getHdtv()) {
                desc.append(" (HD)");
            }
            programItems.put(ProgramData.DESC, desc.toString());
            String category = null;
            String id = program.getId();
            if (program.getShowType() == null) {
                if (id.startsWith("MV")) {
                    category = "Movie";
                } else if (id.startsWith("SP")) {
                    category = "Sports";
                }
            } else {
                category = program.getShowType();
            }
            programItems.put(ProgramData.CATEGORY, category);
            Crew crew = (Crew) xtvd.getProductionCrew().get(id);
            if (crew != null) {
                for (Iterator iterator = crew.getMember().iterator(); iterator.hasNext(); ) {
                    CrewMember member = (CrewMember) iterator.next();
                }
            }
            ProgramGenre programGenre = (ProgramGenre) xtvd.getGenres().get(id);
            if (programGenre != null) {
                for (Iterator iterator = programGenre.getGenre().iterator(); iterator.hasNext(); ) {
                    Genre genre = (Genre) iterator.next();
                }
            }
            try {
                programData.addProgram(programItems);
            } catch (SQLException e) {
                System.err.println("beginImport e:" + e);
                e.printStackTrace();
            }
        }
    }
