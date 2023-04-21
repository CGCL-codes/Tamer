    @Override
    protected List<ClassFetcher> find0(ExceptionHandler handler) {
        List<ClassFetcher> classes = new ArrayList<ClassFetcher>();
        try {
            URLConnection connection = url.openConnection();
            BufferedReader input = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder buffer = new StringBuilder();
            List<String> lines = new LinkedList<String>();
            String inputline;
            try {
                while ((inputline = input.readLine()) != null) {
                    lines.add(inputline);
                    buffer.append(inputline);
                }
            } finally {
                input.close();
            }
            if (buffer.indexOf("<a ") != -1) {
                Pattern pattern = Pattern.compile("href=\"([^\"]*)\"");
                Matcher matcher = pattern.matcher(buffer);
                while (matcher.find()) {
                    String name = matcher.group(1);
                    if (name.indexOf(EXTENSION_SEPARATOR) != -1) {
                        try {
                            classes.addAll(getFinder(new URL(url, matcher.group(1))).find(handler));
                        } catch (MalformedURLException e) {
                            handler.handle(e);
                        }
                    }
                }
            } else {
                for (String line : lines) {
                    try {
                        classes.addAll(getFinder(new URL(url, line)).find(handler));
                    } catch (MalformedURLException e) {
                        handler.handle(e);
                    }
                }
            }
        } catch (IOException e) {
            handler.handle(e);
        }
        return classes;
    }
