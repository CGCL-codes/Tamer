        protected Object parse(String path, URL url) throws Exception {
            try {
                return parse0(url.openStream(), Interpreter.getContentType(url.getPath()));
            } catch (Exception ex) {
                if (log.debugable()) log.realCauseBriefly("Failed to parse " + url, ex); else log.error("Failed to parse " + url + "\nCause: " + Exceptions.getMessage(ex));
                return null;
            }
        }
