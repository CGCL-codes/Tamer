                @Override
                public void run() {
                    try {
                        final DefaultHttpClient httpClient = new DefaultHttpClient();
                        final HttpGet request = new HttpGet(url);
                        final HttpResponse response = httpClient.execute(request);
                        final InputStream inputStream = response.getEntity().getContent();
                        final Drawable drawable = Drawable.createFromStream(inputStream, "src");
                        map.put(url, drawable);
                    } catch (final MalformedURLException e) {
                        map.remove(url);
                    } catch (final IOException e) {
                        map.remove(url);
                    }
                    handler.post(notify);
                }
