        public Atom call(List<Atom> argv) {
            Lists.assertRem(argv, 2);
            Atom a = Lists.read(argv);
            Atoms.assertIsa(a, LIST);
            return Lists.write((List<Atom>) a, Lists.read(argv));
        }
