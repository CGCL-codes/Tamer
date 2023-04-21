        private void test(URL url) throws IOException {
            String header = null;
            InputStream is = null;
            try {
                BufferedReader br = null;
                URLConnection connection = url.openConnection();
                is = connection.getInputStream();
                try {
                    br = new BufferedReader(new InputStreamReader(is));
                } finally {
                    if (br != null) {
                        br.close();
                    }
                }
            } finally {
                if (is != null) {
                    is.close();
                }
            }
        }
