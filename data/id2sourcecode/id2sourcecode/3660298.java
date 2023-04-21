        @Override
        public void run() {
            try {
                for (int i = 0; i < this.repetitions; i++) {
                    HttpGet httpget = new HttpGet(this.requestURI);
                    HttpResponse response = this.httpclient.execute(this.target, httpget);
                    if (this.forceClose) {
                        httpget.abort();
                    } else {
                        HttpEntity entity = response.getEntity();
                        if (entity != null) {
                            entity.consumeContent();
                        }
                    }
                }
            } catch (Exception ex) {
                this.exception = ex;
            }
        }
