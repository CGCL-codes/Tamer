    private boolean updateKinopoiskMediaInfo(Movie movie, String kinopoiskId) {
        try {
            String originalTitle = movie.getTitle();
            String newTitle = originalTitle;
            String xml = webBrowser.request("http://www.kinopoisk.ru/level/1/film/" + kinopoiskId);
            xml = xml.replace((CharSequence) "&#133;", (CharSequence) "&hellip;");
            xml = xml.replace((CharSequence) "&#151;", (CharSequence) "&mdash;");
            if (!movie.isOverrideTitle()) {
                newTitle = HTMLTools.extractTag(xml, "class=\"moviename-big\">", 0, "<>");
                if (!newTitle.equals(Movie.UNKNOWN)) {
                    int i = newTitle.indexOf("(сериал");
                    if (i >= 0) {
                        newTitle = newTitle.substring(0, i);
                        movie.setMovieType(Movie.TYPE_TVSHOW);
                    }
                    newTitle = newTitle.replace(' ', ' ').trim();
                    if (movie.getSeason() != -1) newTitle = newTitle + ", сезон " + String.valueOf(movie.getSeason());
                    String s = HTMLTools.extractTag(xml, "<span style=\"font-size:13px;color:#666\">", 0, "><");
                    if (!s.equals(Movie.UNKNOWN) && !s.equalsIgnoreCase("/span")) {
                        originalTitle = s;
                        newTitle = newTitle + " / " + originalTitle;
                    } else originalTitle = newTitle;
                }
            }
            String plot = HTMLTools.extractTag(xml, "<td colspan=3 style=\"padding:10px;padding-left:20px;\" class=\"news\">", 0, "<>");
            if (plot.equals(Movie.UNKNOWN)) plot = movie.getPlot();
            if (plot.length() > preferredPlotLength) plot = plot.substring(0, preferredPlotLength) + "...";
            movie.setPlot(plot);
            LinkedList<String> newGenres = new LinkedList<String>();
            for (String genre : HTMLTools.extractTags(xml, ">жанр</td>", "</td>", "<a href=\"/level/10", "</a>")) {
                genre = genre.substring(0, 1).toUpperCase() + genre.substring(1, genre.length());
                if (genre.equalsIgnoreCase("мультфильм")) newGenres.addFirst(genre); else newGenres.add(genre);
            }
            if (newGenres.size() > 0) {
                int maxGenres = 9;
                try {
                    maxGenres = Integer.parseInt(PropertiesUtil.getProperty("genres.max", "9"));
                } catch (Exception ignore) {
                }
                while (newGenres.size() > maxGenres) newGenres.removeLast();
                movie.setGenres(newGenres);
            }
            for (String director : HTMLTools.extractTags(xml, ">режиссер</td>", "</td>", "<a href=\"/level/4", "</a>")) {
                movie.setDirector(director);
                break;
            }
            Collection<String> newCast = new ArrayList<String>();
            for (String actor : HTMLTools.extractTags(xml, ">В главных ролях:", "</table>", "<a href=\"/level/4", "</a>")) {
                newCast.add(actor);
            }
            if (newCast.size() > 0) movie.setCast(newCast);
            for (String country : HTMLTools.extractTags(xml, ">страна</td>", "</td>", "<a href=\"/level/10", "</a>")) {
                movie.setCountry(country);
                break;
            }
            if (movie.getYear().equals(Movie.UNKNOWN)) {
                for (String year : HTMLTools.extractTags(xml, ">год</td>", "</td>", "<a href=\"/level/10", "</a>")) {
                    movie.setYear(year);
                    break;
                }
            }
            int kinopoiskRating = -1;
            String rating = HTMLTools.extractTag(xml, "<a href=\"/level/83/film/" + kinopoiskId + "/\" class=\"continue\">", 0, "<");
            if (!rating.equals(Movie.UNKNOWN)) {
                try {
                    kinopoiskRating = (int) (Float.parseFloat(rating) * 10);
                } catch (Exception ignore) {
                }
            }
            int imdbRating = movie.getRating();
            if (imdbRating == -1) {
                rating = HTMLTools.extractTag(xml, ">IMDB:", 0, "<(");
                if (!rating.equals(Movie.UNKNOWN)) {
                    try {
                        imdbRating = (int) (Float.parseFloat(rating) * 10);
                    } catch (Exception ignore) {
                    }
                }
            }
            int r = kinopoiskRating;
            if (imdbRating != -1) {
                if (preferredRating.equals("imdb")) r = imdbRating; else if (preferredRating.equals("average")) r = (kinopoiskRating + imdbRating) / 2;
            }
            movie.setRating(r);
            if (movie.getPosterURL() == null || movie.getPosterURL().equalsIgnoreCase(Movie.UNKNOWN)) {
                movie.setTitle(originalTitle);
                movie.setPosterURL(getPosterURL(movie, ""));
            }
            if (movie.getRuntime().equals(Movie.UNKNOWN)) {
                movie.setRuntime(getPreferredValue(HTMLTools.extractTags(xml, ">время</td>", "</td>", "<td", "</td>")));
            }
            movie.setTitle(newTitle);
        } catch (Exception e) {
            logger.severe("Failed retreiving movie data from Kinopoisk : " + kinopoiskId);
            e.printStackTrace();
        }
        return true;
    }
