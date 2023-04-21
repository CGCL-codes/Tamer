            public BufferedReader newReader() {
                try {
                    return buffer(UrlRepo.this.url.openStream());
                } catch (IOException e) {
                    throw new CongaRuntimeException(e);
                }
            }
