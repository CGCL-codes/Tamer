    public Dataset(URI dataset_uri) {
        super();
        HttpURLConnection.setFollowRedirects(false);
        HttpURLConnection con = null;
        try {
            URL dataset_url = dataset_uri.toURL();
            con = (HttpURLConnection) dataset_url.openConnection();
            con.setDoInput(true);
            con.setDoOutput(true);
            con.setUseCaches(false);
            con.addRequestProperty("Accept", "application/rdf+xml");
            jenaModel = OT.createModel();
            jenaModel.read(con.getInputStream(), null);
        } catch (MalformedURLException ex) {
            errorRep.append(ex, "The dataset_uri cannot be cast as a valid URL!", Status.CLIENT_ERROR_BAD_REQUEST);
        } catch (IllegalArgumentException ex) {
            errorRep.append(ex, "Check the dataset URI you posted !", Status.CLIENT_ERROR_BAD_REQUEST);
        } catch (SecurityException ex) {
            errorRep.append(ex, "A security exception occured! It is possible that a resource" + "requires user authentication.", Status.SERVER_ERROR_INTERNAL);
        } catch (IllegalStateException ex) {
            errorRep.append(ex, "HTTP connection cannot be configured correctly!", Status.SERVER_ERROR_INTERNAL);
        } catch (FileNotFoundException ex) {
            errorRep.append(ex, "The requested dataset resource could not be found!", Status.CLIENT_ERROR_BAD_REQUEST);
        } catch (IOException ex) {
            errorRep.append(ex, "Input/Output Error while trying to open a connection!", Status.SERVER_ERROR_INTERNAL);
        } catch (Exception ex) {
            errorRep.append(ex, "The dataset uri you provided seems that does not correspond to an existing resource!", Status.CLIENT_ERROR_BAD_REQUEST);
            Logger.getLogger(Dataset.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
