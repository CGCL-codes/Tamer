        @Override
        public void run() {
            System.out.println(id + " - about to get something from " + httpget.getURI());
            try {
                HttpResponse response = httpClient.execute(httpget, context);
                System.out.println(id + " - get executed");
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    byte[] bytes = EntityUtils.toByteArray(entity);
                    System.out.println(id + " - " + bytes.length + " bytes read");
                }
            } catch (Exception e) {
                httpget.abort();
                System.out.println(id + " - error: " + e);
            }
        }
