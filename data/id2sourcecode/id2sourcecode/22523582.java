    void digestInstrument(Element root) {
        for (Iterator i = root.getChildren().iterator(); i.hasNext(); ) {
            Element child = (Element) i.next();
            Instrument instrument = getInstrument(child.getName());
            if (instrument != null) {
                instrument.digest(child);
                setInstrument(instrument);
                break;
            }
        }
    }
