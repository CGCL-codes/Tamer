    protected CrawlDataItem parseLine(String line) {
        if (line != null && line.length() > 42) {
            String[] lineParts = line.split("\\s+", 12);
            if (lineParts.length < 10) {
                return null;
            }
            String timestamp;
            try {
                timestamp = crawlDataItemFormat.format(crawlDateFormat.parse(lineParts[0]));
            } catch (ParseException e) {
                System.err.println("Error parsing date for: " + line);
                e.printStackTrace();
                return null;
            }
            String url = lineParts[3];
            String mime = lineParts[6];
            String digest = lineParts[9];
            if (digest.lastIndexOf(":") >= 0) {
                digest = digest.substring(digest.lastIndexOf(":") + 1);
            }
            String origin = null;
            boolean duplicate = false;
            if (lineParts.length == 12) {
                String annotation = lineParts[11];
                int startIndex = annotation.indexOf("duplicate:\"");
                if (startIndex >= 0) {
                    startIndex += 11;
                    int endIndex = annotation.indexOf('"', startIndex + 1);
                    origin = annotation.substring(startIndex, endIndex);
                    duplicate = true;
                } else if (annotation.contains("duplicate")) {
                    duplicate = true;
                }
            }
            return new CrawlDataItem(url, digest, timestamp, null, mime, origin, duplicate);
        }
        return null;
    }
