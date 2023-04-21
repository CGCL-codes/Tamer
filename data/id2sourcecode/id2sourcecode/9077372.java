    public List<Program> getPrograms(Channel channel, Date date, int day) throws TransportException {
        String s = getTransport().getText(buildListUrl(channel, day));
        Type t1 = new TypeToken<Map<String, List<JsonProgram>>>() {
        }.getType();
        Type t2 = new TypeToken<Map<String, Map<String, JsonProgram>>>() {
        }.getType();
        Collection<JsonProgram> jsonPrograms;
        try {
            s = StringEscapeUtils.unescapeJavaScript(s);
            Gson gson = new Gson();
            try {
                jsonPrograms = ((Map<String, List<JsonProgram>>) gson.fromJson(s, t1)).get(channel.getChannelId());
                log.debug("Succesfully parsed json using strat 1.");
            } catch (JsonParseException e) {
                log.debug("Parsing json using strat 1 fails, falling back to strat 2.");
                jsonPrograms = ((Map<String, Map<String, JsonProgram>>) gson.fromJson(s, t2)).get(channel.getChannelId()).values();
            }
        } catch (JsonParseException e) {
            log.error("Error parsing json:\n" + s, e);
            throw new TransportException("Could not parse data.");
        }
        List<Program> programs = new ArrayList<Program>();
        for (JsonProgram jsonProgram : jsonPrograms) {
            Program p = new Program();
            p.setChannelId(channel.getChannelId());
            StringBuilder sb = new StringBuilder(getTvgidsurl());
            sb.append("/");
            if (jsonProgram.getArtikel_id() != null) {
                sb.append("artikel/");
                sb.append(jsonProgram.getArtikel_id());
            } else {
                sb.append("programma/");
                sb.append(jsonProgram.getDb_id());
            }
            sb.append("/");
            p.setUrl(sb.toString());
            p.setFullyLoaded(false);
            p.setTitle(jsonProgram.getTitel());
            SimpleDateFormat sdf = new SimpleDateFormat(JSON_DATE_PATTERN);
            try {
                p.setStartDate(sdf.parse(jsonProgram.getDatum_start()));
                p.setEndDate(sdf.parse(jsonProgram.getDatum_end()));
            } catch (ParseException e) {
                throw new RuntimeException(jsonProgram.getDatum_start() + " or " + jsonProgram.getDatum_end() + " not valid.");
            }
            p.setGanre(getMappedGanre(jsonProgram.getGenre()));
            if (jsonProgram.getKijkwijzer() != null) {
                Rating r = new Rating();
                r.setValue(jsonProgram.getKijkwijzer());
                p.getRating().add(r);
            }
            programs.add(p);
        }
        fixProgramTimes(programs);
        return programs;
    }
