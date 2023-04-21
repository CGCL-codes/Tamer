    private String getSearchPage(int start, String target) {
        URL url;
        try {
            String encodedTarget = URLEncoder.encode(target, "UTF-8");
            url = new URL(searchUrl + "&start=" + start + "&q=site:twitter.com%20inurl:status%20" + encodedTarget);
            String result = null;
            URLConnection connection = null;
            connection = url.openConnection();
            Scanner scanner = new Scanner(connection.getInputStream());
            scanner.useDelimiter(END_OF_INPUT);
            result = scanner.next();
            Thread.sleep(10000);
            return result;
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
