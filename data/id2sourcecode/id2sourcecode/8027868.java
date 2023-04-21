    public void export(Article article, boolean overwriteFile) throws FileAlreadyExistsException {
        File outputFile = null;
        FileOutputStream outputStream = null;
        try {
            Transformer transformer = getTransformerForArticle(article);
            outputFile = getResultDestination(article, overwriteFile);
            outputStream = new FileOutputStream(outputFile);
            Document articleDocument = getDocumentFromArticle(article);
            transformer.transform(new DOMSource(articleDocument), new StreamResult(outputStream));
        } catch (TransformerException e) {
            throw new AppInfrastructureException(e);
        } catch (FileNotFoundException e) {
            throw new AppInfrastructureException(e);
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    logger.log(Level.SEVERE, "DAO error closing file:", e);
                }
            }
        }
    }
