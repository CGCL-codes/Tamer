    @Override
    public byte[] getBinaryData(Content c) {
        logger.debug("getBinaryData(Content) called for " + c.getContentUrl());
        try {
            URL url = new URL(c.getContentUrl());
            Page page = new Page(url.openConnection());
            Source source = page.getSource();
            boolean reading = true;
            while (reading) {
                reading = (source.read() != Source.EOF);
            }
            return page.getText().getBytes();
        } catch (MalformedURLException e) {
            logger.error("Marformed url - " + e.getMessage());
        } catch (IOException e) {
            logger.error("Couldm't read source - " + e.getMessage());
        } catch (ParserException e) {
            logger.error("Couldm't parse source - " + e.getMessage());
        }
        return null;
    }
