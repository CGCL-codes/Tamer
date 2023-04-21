        protected URLConnection openConnection(URL url) {
            return new IncludeProcessFileHandler.FileDefinitionURLConnection(url, fileDefinition, src);
        }
