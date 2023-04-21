    public boolean load(URL url) {
        String line = null;
        try {
            URLConnection yc = url.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
            while ((line = in.readLine()) != null) {
                Matcher matcher;
                if ((matcher = rect.matcher(line)).find()) {
                    ecCoords.put(matcher.group(5), new ECCoord(matcher.group(5), matcher.group(1), matcher.group(2), matcher.group(3), matcher.group(4)));
                }
            }
            in.close();
        } catch (IOException ex) {
            ex.printStackTrace();
            return false;
        }
        return true;
    }
