    private String getNextEvents() {
        final StringBuilder builder = new StringBuilder();
        builder.append("\n");
        try {
            final String url = getStringProperty("url") + "?mode=next&hash=" + createHash();
            final URLConnection urlConnection = (new java.net.URL(url)).openConnection();
            final long newModified = urlConnection.getLastModified();
            Event event = null;
            if (newModified == lastModified) {
                event = eventCached;
            } else {
                final EventXmlParser handler = new EventXmlParser();
                final SAXParser saxParser = SAXParserFactory.newInstance().newSAXParser();
                saxParser.parse(urlConnection.getInputStream(), handler);
                event = handler.getResult();
                eventCached = event;
                lastModified = newModified;
            }
            builder.append("Die naechsten Feten:\n\n");
            if (event != null) {
                do {
                    builder.append(eventToString(event));
                    if (event.hasNextEvent()) {
                        builder.append("\n---------------------------------\n");
                    }
                    event = event.getNextEvent();
                } while (event != null);
            } else {
                builder.append("Im Moment sind leider keine Daten verfuegbar.");
            }
        } catch (final SAXException e) {
            builder.append("Leider ist ein Fehler aufgetreten.\n").append("Versuch es spaeter nocheinmal.");
            e.printStackTrace();
            bot.getLogger().log(e);
        } catch (final IOException e) {
            builder.append("Leider ist ein Fehler aufgetreten.\n").append("Versuch es spaeter nocheinmal.");
            e.printStackTrace();
            bot.getLogger().log(e);
        } catch (final ParserConfigurationException e) {
            builder.append("Leider ist ein Fehler aufgetreten.\n").append("Versuch es spaeter nocheinmal.");
            e.printStackTrace();
            bot.getLogger().log(e);
        }
        return builder.toString();
    }
