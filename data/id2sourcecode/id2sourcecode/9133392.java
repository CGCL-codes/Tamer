        public URLResourceAttributes(String name) throws Exception {
            urlConnection = lookupURL(name).openConnection();
            this.name = name;
        }
