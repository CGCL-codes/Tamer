    public String getGbrowse(String position, String[] tracks) {
        Calendar cal1 = Calendar.getInstance();
        StringBuilder gbrowseHtml = new StringBuilder();
        String urlString = customParameters.getGbrowseImgUrl();
        urlString += "/cnv_gbrowse_hg";
        urlString += customParameters.getBuild();
        urlString += "/?name=";
        urlString += position;
        if (tracks.length > 0) {
            urlString += "&type=";
        }
        for (int i = 0; i < tracks.length - 1; i++) {
            urlString += tracks[i] + "+";
        }
        urlString += tracks[tracks.length - 1];
        urlString += "&embed=1";
        urlString += "&width=" + getWidth();
        URL url = null;
        try {
            url = new URL(urlString);
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            String line = "";
            while ((line = in.readLine()) != null) {
                line = line.replaceAll("hmap\"", "hmap" + position + "\"");
                if (line.startsWith("<area shape")) {
                    if (line.indexOf("href=\"../..") < 0) {
                        gbrowseHtml.append(customizeHrefHtml(line));
                    }
                } else if (line.indexOf("<img") >= 0) {
                    gbrowseHtml.append(line.replace("src=\"", "src=\"" + customParameters.getGbrowseHost()));
                }
            }
            gbrowseHtml.append("</map>");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Calendar cal2 = Calendar.getInstance();
        String gbrowseHtmlString = gbrowseHtml.toString();
        gbrowseHtmlString = gbrowseHtmlString.replaceAll("alt=\"[^\"]*\"", "");
        gbrowseHtmlString = gbrowseHtmlString.replaceAll("(\\s)+", " ");
        return gbrowseHtmlString;
    }
