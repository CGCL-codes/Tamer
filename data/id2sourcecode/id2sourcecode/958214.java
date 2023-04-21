    protected String formatFilename(String src, String filename) {
        String actualFilename = filename;
        if (filename.indexOf('{') > -1 && filename.indexOf('}') > filename.indexOf('{')) {
            int startIdx = filename.indexOf('{');
            int endIdx = filename.indexOf('}');
            String dateFormat = filename.substring(startIdx + 1, endIdx);
            if (filename.indexOf('[') > -1 && filename.indexOf(']') > filename.indexOf('[')) {
                startIdx = filename.indexOf('[');
                endIdx = filename.indexOf(']');
                String dateSymbol = filename.substring(startIdx + 1, endIdx);
                String dateStr = DateSymbolParser.parse(dateSymbol, new SimpleDateFormat(dateFormat));
                actualFilename = filename.replace("{" + dateFormat + "}", "").replace("[" + dateSymbol + "]", dateStr);
            } else {
                String dateStr = new SimpleDateFormat(dateFormat).format(Now.get());
                actualFilename = filename.replace("{" + dateFormat + "}", dateStr);
            }
        }
        if (filename.charAt(0) == '_') {
            String srcname = new File(src).getName();
            int index = srcname.lastIndexOf('.');
            if (index > 0) srcname = srcname.substring(0, index);
            actualFilename = srcname + actualFilename;
        }
        return actualFilename;
    }
