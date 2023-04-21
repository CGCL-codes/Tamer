        private Source urlSource(String path) throws TransformerException {
            try {
                URL url = new URL(path);
                return new StreamSource(url.openStream());
            } catch (FileNotFoundException e) {
                throw new TransformerException(e.getMessage(), e);
            } catch (MalformedURLException e) {
                throw new TransformerException(e.getMessage(), e);
            } catch (IOException e) {
                throw new TransformerException(e.getMessage(), e);
            }
        }
