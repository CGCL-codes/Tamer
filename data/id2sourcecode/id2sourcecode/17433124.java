        protected URLConnection openConnection(URL u) throws IOException {
            System.out.println("ACMAPS, url host=" + u.getHost());
            System.out.println("ACMAPS, url path=" + u.getPath());
            System.out.println("ACMAPS, url file=" + u.getFile());
            String newurl;
            String base = Configuration.getConfiguration().get(ACMapsBase.ACMAPS_URLBASE_OPTION, ACMapsBase.ACMAPS_URLBASE_DEFAULT_VALUE);
            if (base.endsWith("/")) newurl = base + u.getFile(); else newurl = base + "/" + u.getFile();
            return new URL(newurl).openConnection();
        }
