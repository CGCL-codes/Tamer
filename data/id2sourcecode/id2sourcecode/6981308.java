    private void createEntry(MavenProjectInformation mpi, MavenRepositoryExtension mavenRepoExtension, String columnId, String text) throws SAXException {
        if (text == null) {
            writeTagText(TagNames.TD, null);
            return;
        }
        URL href;
        try {
            href = context.createNexusUrl(mpi, mavenRepoExtension.getFileExtension(), mavenRepoExtension.getClassifier()).toURL();
            System.out.println("  Nexus URL created  " + href);
            URLConnection urlConnection = href.openConnection();
            urlConnection.connect();
            String contentEncoding = urlConnection.getContentEncoding();
            System.out.println("  content  " + contentEncoding + "  " + urlConnection.getContentType() + "  " + urlConnection.getContentLength());
            if (urlConnection.getContentType() != null && urlConnection.getContentType().equals(mavenRepoExtension.getContentType()) && urlConnection.getContentLength() > 0) {
                if (mavenRepoExtension.getIcon() != null) {
                    writeImageLink(href, mavenRepoExtension);
                } else {
                    writeLink(href, mavenRepoExtension.getTooltip());
                }
            } else {
                writeTagText(TagNames.TD, null);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
            writeTagText(TagNames.TD, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
