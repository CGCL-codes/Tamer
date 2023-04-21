        public ExistingInstructionException(String directive) {
            super("Bnd file already has a " + directive + " directive, use -Doverwrite or -o to replace it");
        }
