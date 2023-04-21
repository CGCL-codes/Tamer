        protected URLConnection openConnection(URL url) {
            return new FileDefinitionURLConnection(url, fileDefinition, src);
        }
