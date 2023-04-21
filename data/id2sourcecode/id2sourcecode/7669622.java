        public void insertAdditionalUrl(String tit, URL url) {
            for (Tutorial tut : data.values()) {
                if (tut == null) {
                    continue;
                }
                URL val = tut.getStore().getUrl();
                String path = url.getPath();
                int lastIndexOf = path.lastIndexOf('/');
                if (lastIndexOf != -1) {
                    path = path.substring(lastIndexOf + 1);
                }
                String valPath = val.getPath();
                if (valPath.endsWith("!/")) {
                    valPath = valPath.substring(0, valPath.length() - 2);
                }
                if (valPath.endsWith(path)) {
                    return;
                }
            }
            for (URL val : urls.values()) {
                if (val == null) {
                    continue;
                }
                String path = url.getPath();
                int lastIndexOf = path.lastIndexOf('/');
                if (lastIndexOf != -1) {
                    path = path.substring(lastIndexOf + 1);
                }
                if (val.getPath().endsWith(path)) {
                    return;
                }
            }
            boolean canInstantiate = true;
            URLConnection conn = null;
            try {
                conn = url.openConnection();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            if (conn instanceof JarURLConnection) {
                JarURLConnection jarCon = (JarURLConnection) conn;
                URL jarFileURL = jarCon.getJarFileURL();
                if (jarFileURL.getProtocol().startsWith("http")) {
                    canInstantiate = false;
                }
            } else {
                if (url.getProtocol().startsWith("http")) {
                    canInstantiate = false;
                }
            }
            Tutorial tutorial = null;
            if (canInstantiate) {
                try {
                    tutorial = new Tutorial(new URLStore(url), false);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
            if (tutorial != null) {
                for (ResourceBundle extRes : externalResourceBundles) {
                    tutorial.addExternalResourceBundle(extRes);
                }
                data.put(tutorial.toString(), tutorial);
                titles.add(tutorial.toString());
                urls.put(tutorial.toString(), null);
            } else {
                String tutTitle = tit;
                tutTitle = tutTitle.replace('_', ' ');
                if (tutTitle.toLowerCase().endsWith(".tut")) {
                    tutTitle = tutTitle.substring(0, tutTitle.length() - 4);
                }
                StringTokenizer tokenizer = new StringTokenizer(tutTitle, " ");
                StringBuilder b = new StringBuilder();
                while (tokenizer.hasMoreTokens()) {
                    String token = tokenizer.nextToken();
                    if (b.length() > 0) {
                        b.append(' ');
                    }
                    if (token.length() > 0) {
                        b.append(Character.toTitleCase(token.charAt(0)));
                    }
                    if (token.length() > 1) {
                        b.append(token.substring(1));
                    }
                }
                tutTitle = b.toString();
                titles.add(tutTitle);
                data.put(tutTitle, null);
                urls.put(tutTitle, url);
            }
            Collections.sort(titles);
        }
