        public ExistingElementException(String element) {
            super("Project already has a <" + element + "> which matches, use -Doverwrite or -o to replace it");
        }
