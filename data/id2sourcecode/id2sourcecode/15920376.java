    public VarList(final VarList list1, final VarList list2, final boolean assign) {
        if (assign || list1.read.length == 0) {
            read = list2.read;
        } else if (list2.read.length == 0) {
            read = list1.read;
        } else {
            final HashSet merge = new HashSet();
            for (int i = 0; i < list1.read.length; i++) {
                merge.add(list1.read[i]);
            }
            for (int i = 0; i < list2.read.length; i++) {
                merge.add(list2.read[i]);
            }
            read = new VarAST[merge.size()];
            merge.toArray(read);
        }
        if (list1.write.length == 0 && (!assign || list1.read.length == 0)) {
            write = list2.write;
        } else if (list2.write.length == 0 && (!assign || list1.read.length == 0)) {
            write = list1.write;
        } else {
            final HashSet merge = new HashSet();
            for (int i = 0; i < list1.write.length; i++) {
                merge.add(list1.write[i]);
            }
            for (int i = 0; i < list2.write.length; i++) {
                merge.add(list2.write[i]);
            }
            if (assign) {
                for (int i = 0; i < list1.read.length; i++) {
                    merge.add(list1.read[i]);
                }
            }
            write = new VarAST[merge.size()];
            merge.toArray(write);
        }
        if (list1.decl.length == 0) {
            decl = list2.decl;
        } else if (list2.decl.length == 0) {
            decl = list1.decl;
        } else {
            final HashSet merge = new HashSet();
            for (int i = 0; i < list1.decl.length; i++) {
                merge.add(list1.decl[i]);
            }
            for (int i = 0; i < list2.decl.length; i++) {
                merge.add(list2.decl[i]);
            }
            decl = new VarAST[merge.size()];
            merge.toArray(decl);
        }
    }
