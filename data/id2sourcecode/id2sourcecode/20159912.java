        void assertRepoURLs(String[] urls) {
            if (urls == null) {
                throw new RuntimeException("No URLs set");
            }
            StringBuffer sb = new StringBuffer();
            int nConnectionErrs = 0;
            for (int i = 0; i < urls.length; i++) {
                URLConnection conn = null;
                try {
                    URL url = new URL(urls[i]);
                    conn = url.openConnection();
                    conn.connect();
                } catch (Exception e) {
                    sb.append(" " + urls[i] + ": " + e);
                    sb.append("\n");
                    nConnectionErrs++;
                } finally {
                }
            }
            if (nConnectionErrs > 0) {
                String msg = "URL connection errors:\n" + sb.toString();
                throw new RuntimeException(msg);
            }
        }
