    public List<Event> getEvents(Query query) {
        GregorianCalendar date = new GregorianCalendar();
        String lookup = "";
        if (query.getDay() == Day.TOMORROW) {
            date.add(Calendar.DATE, 1);
        }
        lookup = String.valueOf(date.get(Calendar.YEAR)) + "/" + addZeroes(date.get(Calendar.MONTH) + 1) + "/" + addZeroes(date.get(Calendar.DATE)) + "/";
        ArrayList<Event> list = new ArrayList<Event>();
        Event event = new Event();
        try {
            Document document = Jsoup.connect("http://esporte.uol.com.br/programacao-de-tv/data/" + lookup).get();
            Elements elements = document.getElementsByTag("tbody");
            for (Element element : elements) {
                Elements items = element.getElementsByTag("tr");
                for (Element item : items) {
                    Elements tabular = item.getElementsByTag("td");
                    event = new Event();
                    event.setDate(getDateFromString(date, tabular.get(0).text()));
                    event.setGenre(tabular.get(1).text());
                    event.setName(tabular.get(2).text());
                    event.setDescription(tabular.get(2).text());
                    event.setChannel(tabular.get(3).text());
                    if (!query.getChannel().equals("#")) {
                        if (!query.getGenre().equals("#")) {
                            if (event.getChannel().toLowerCase().equals(query.getChannel().toLowerCase())) {
                                if (event.getGenre().toLowerCase().equals(query.getGenre().toLowerCase())) {
                                    list.add(event);
                                }
                            }
                        } else {
                            if (event.getChannel().toLowerCase().equals(query.getChannel().toLowerCase())) {
                                list.add(event);
                            }
                        }
                    } else {
                        if (!query.getGenre().equals("#")) {
                            if (event.getGenre().toLowerCase().equals(query.getGenre().toLowerCase())) {
                                list.add(event);
                            }
                        } else {
                            list.add(event);
                        }
                    }
                }
            }
        } catch (Exception e) {
            list.clear();
        }
        return list;
    }
