    public static GBPageDTO getSingleGBPage(int id, int pageNumber) {
        String site = null;
        GBPageDTO pageDTO = new GBPageDTO(id, pageNumber);
        try {
            String line;
            StringBuffer readBuffer = new StringBuffer();
            URL url = new URL(guestBookUrl + id + "&seite=" + pageNumber);
            HttpURLConnection connect = (HttpURLConnection) url.openConnection();
            connect.addRequestProperty("User-Agent", userAgent);
            BufferedReader input = new BufferedReader(new InputStreamReader(connect.getInputStream()));
            while ((line = input.readLine()) != null) {
                readBuffer.append("\n" + line);
            }
            input.close();
            connect.disconnect();
            site = readBuffer.toString();
        } catch (MalformedURLException mue) {
            System.err.println("getSingleGBPage::Bad URL-->" + mue);
            return null;
        } catch (IOException ioe) {
            System.err.println("getSingleGBPage::IOError-->" + ioe);
            return null;
        }
        if ((!site.contains("<td style=\"text-align:left;\" width=\"35\">")) || (site.contains("gp_negativ.png"))) {
            pageDTO.setMorePages(false);
        } else {
            pageDTO.setMorePages(true);
        }
        pageDTO.setThePage(site);
        return pageDTO;
    }
