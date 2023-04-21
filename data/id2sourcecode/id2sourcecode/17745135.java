    public void execute() throws MojoExecutionException, MojoFailureException {
        try {
            URL url = getUploadUrl();
            HttpEntity body = getRequestBody();
            WagonManager manager = (WagonManager) container.lookup(WagonManager.ROLE);
            DefaultHttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(url.toExternalForm());
            post.setEntity(body);
            getLog().info(post.getRequestLine().toString());
            if (site != null && site.getId() != null) {
                AuthenticationInfo info = manager.getAuthenticationInfo(site.getId());
                if (info != null) {
                    getLog().info("Using user " + info.getUserName());
                    client.getCredentialsProvider().setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(info.getUserName(), info.getPassword()));
                }
            }
            if (!test) {
                HttpResponse response = client.execute(post);
                if (response.getStatusLine().getStatusCode() != 200) {
                    throw new MojoExecutionException("Http Transfer failed." + response.getStatusLine());
                }
                client.getConnectionManager().shutdown();
            }
        } catch (UnsupportedEncodingException e) {
            throw new MojoExecutionException("Cannot build http request.", e);
        } catch (ComponentLookupException e) {
            throw new MojoExecutionException("Cannot find the wagon manager.", e);
        } catch (MalformedURLException e) {
            throw new MojoExecutionException("Cannot build http request.", e);
        } catch (ClientProtocolException e) {
            throw new MojoExecutionException("Http Transfer failed.", e);
        } catch (IOException e) {
            throw new MojoExecutionException("Http Transfer failed.", e);
        }
    }
