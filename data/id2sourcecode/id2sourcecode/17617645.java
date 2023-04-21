    public static Map filterSupportedFileFormats(Map requestedFormats, boolean read, boolean write) {
        Map resultFormats = new HashMap();
        Set formats = requestedFormats.keySet();
        Iterator iterator = formats.iterator();
        while (iterator.hasNext()) {
            String format = (String) iterator.next();
            Iterator writer = ImageIO.getImageWritersByFormatName(format);
            Iterator reader = ImageIO.getImageReadersByFormatName(format);
            if ((write && !writer.hasNext()) || (read && !reader.hasNext())) {
                continue;
            } else {
                resultFormats.put(format, requestedFormats.get(format));
            }
        }
        return resultFormats;
    }
