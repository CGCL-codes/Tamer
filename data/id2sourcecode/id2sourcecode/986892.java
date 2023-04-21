    public static String generateMapFromURL(String dataURL, String fieldToMap, StringBuilder errors) {
        if (errors == null) {
            errors = new StringBuilder();
        }
        try {
            URL url = new URL(dataURL);
            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
            String[] fileInfo = analyseGeographicFile(br, null);
            if (fileInfo == null) {
                throw new Exception("generateMapfromURL: No geographic data column (e.g. " + "a column with OA codes) could be found in the data");
            }
            if (fileInfo.length < 5) {
                throw new Exception("analyseFile() has not returned enough information " + "(array should have at least 5 elements, but only has " + fileInfo.length + ". The array looks like: " + Arrays.toString(fileInfo));
            }
            boolean foundCol = false;
            for (int i = 4; i < fileInfo.length; i++) {
                if (fileInfo[i].equals(fieldToMap)) {
                    foundCol = true;
                }
            }
            if (!foundCol) {
                throw new Exception("The field to map (" + fieldToMap + ") does not appear in " + "the data: " + Arrays.toString(fileInfo));
            }
            String mapTitle = ("Map of " + fieldToMap + " from " + url.getPath()).replace(' ', '+');
            return "http://www.maptube.org/mapcsv.aspx?" + "g=" + fileInfo[2] + "&ti=" + mapTitle + "&ak=" + fileInfo[3] + "&dc=" + fieldToMap + "&csv=" + dataURL;
        } catch (MalformedURLException ex) {
            errors.append("MalformedURLException from input url: '" + dataURL + "': " + ex.getMessage() + "\n");
        } catch (IOException ex) {
            errors.append("IOException reading the data: " + ex.getMessage() + "\n");
        } catch (Exception ex) {
            errors.append("Caught an exception: " + ex.toString() + ". Exception message is: " + ex.getMessage() + ". Stack trace is:\n");
            for (StackTraceElement s : ex.getStackTrace()) {
                errors.append("\t" + s.toString() + "\n");
            }
        }
        return null;
    }
