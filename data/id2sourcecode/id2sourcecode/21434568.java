    public String getResources(String data, String regex, String att, char cBegin, char cEnd, ProjectDownloadLinks projDLink) {
        String startingAddress = projDLink.getUrl();
        String domainName = getDomainName(startingAddress);
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(data);
        while (matcher.find() == true) {
            int begin = matcher.group().indexOf('=');
            if (begin == -1) {
                begin = matcher.group().indexOf(cBegin);
            }
            begin += 1;
            int end = matcher.group().length() - 1;
            String url = matcher.group().substring(begin, end);
            url = url.replace(" ", "");
            url = url.replace("'", "");
            url = url.replace("\"", "");
            url = url.replace("(", "");
            url = url.replace(")", "");
            String resourceURL = url;
            String path = url;
            System.out.println(url);
            if (url.contains("http://") == true || url.contains("https://") == true) {
                begin = url.indexOf("//") + 2;
                end = url.indexOf("/", begin);
                path = url.substring(end + 1);
            } else if (url.startsWith("../")) {
                url = url.substring(3);
            } else if (url.startsWith("/")) {
                url = url.substring(1);
            } else {
                try {
                    resourceURL = domainName + '/' + url;
                    URL _url = new URL(resourceURL);
                    URLConnection urlCon = _url.openConnection();
                    DataInputStream in = new DataInputStream(urlCon.getInputStream());
                } catch (Exception ex) {
                    System.err.println(ex);
                    resourceURL = startingAddress + '/' + url;
                }
            }
            if (path.startsWith("/") == true) {
                path = path.substring(1);
            }
            path = path.replace("?", "-");
            String pathResource = projDLink.getSaveTo() + path;
            if (hasResourceURL(projDLink.getArrayResourceLinks(), url) == false) {
                projDLink.addToArrayResourceLinks(resourceURL);
                projDLink.addToArrayResoucePaths(pathResource);
            }
            data = data.replace(url, path);
        }
        return data;
    }
