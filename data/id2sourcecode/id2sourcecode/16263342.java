    protected void parseLineups() throws DataDirectException {
        try {
            log.write(sdf.format(new java.util.Date()));
            log.write("\tParsing lineups top-level element");
            log.write(Parser.END_OF_LINE);
        } catch (IOException ioex) {
            ioex.printStackTrace();
        }
        try {
            writer.writeStartElement("lineups");
            writer.writeCharacters(Parser.END_OF_LINE);
            reader.next();
            toStartTag();
            Lineup lineup = new Lineup();
            while (reader.getLocalName().equals("lineup")) {
                getLineup(lineup);
                writer.writeStartElement("lineup");
                writer.writeAttribute("id", lineup.getId());
                writer.writeAttribute("name", lineup.getName());
                writer.writeAttribute("location", lineup.getLocation());
                writer.writeAttribute("type", lineup.getType().toString());
                if (lineup.getDevice() != null && lineup.getDevice().length() > 0) {
                    writer.writeAttribute("device", lineup.getDevice());
                }
                if (lineup.getPostalCode() != null && lineup.getPostalCode().length() > 0) {
                    writer.writeAttribute("postalCode", lineup.getPostalCode());
                }
                writer.writeCharacters(Parser.END_OF_LINE);
                for (Iterator iterator = lineup.getMaps().iterator(); iterator.hasNext(); ) {
                    Map map = (Map) iterator.next();
                    writer.writeStartElement("map");
                    writer.writeAttribute("station", String.valueOf(map.getStation()));
                    writer.writeAttribute("channel", String.valueOf(map.getChannel()));
                    if (map.getChannelMinor() != 0) {
                        writer.writeAttribute("channelMinor", String.valueOf(map.getChannelMinor()));
                    }
                    if (map.getFrom() != null) {
                        writer.writeAttribute("from", map.getFrom().toString());
                    }
                    if (map.getTo() != null) {
                        writer.writeAttribute("to", map.getTo().toString());
                    }
                    writer.writeEndElement();
                    writer.writeCharacters(Parser.END_OF_LINE);
                }
                writer.writeEndElement();
                writer.writeCharacters(Parser.END_OF_LINE);
                lineup.reset();
            }
            writer.writeEndElement();
            writer.writeCharacters(Parser.END_OF_LINE);
        } catch (XMLStreamException xsex) {
            throw new DataDirectException(xsex.getMessage(), xsex);
        }
    }
