    public static UserPageDTO getUserPage(int userID) {
        String line;
        StringBuffer readBuffer = new StringBuffer();
        UserPageDTO pageDTO = new UserPageDTO();
        pageDTO.setId(userID);
        try {
            URL url = new URL(userPageUrl + userID);
            HttpURLConnection connect = (HttpURLConnection) url.openConnection();
            connect.addRequestProperty("User-Agent", userAgent);
            System.out.println("DaCrawler::getUserPage:userid-->" + userID);
            BufferedReader input = new BufferedReader(new InputStreamReader(connect.getInputStream()));
            while ((line = input.readLine()) != null) {
                readBuffer.append(line);
            }
            input.close();
            connect.disconnect();
            pageDTO.setThepage(readBuffer.toString());
            return pageDTO;
        } catch (MalformedURLException mue) {
            System.err.println("crawlUserPage::Bad URL-->" + mue);
            return null;
        } catch (IOException ioe) {
            System.err.println("DaCrawler::crawlUserPage::IOException-->" + ioe);
            return null;
        }
    }
