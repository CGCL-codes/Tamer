    public boolean initReferences(Writer writer) {
        for (Outputmethod om : list) {
            if (!om.isReferenced()) {
                for (Outputmethod compare : list) {
                    if (om != compare && compare.isReferenced() && om.getCargName().equals(compare.getCargName()) && om.getArgName().equals(compare.getArgName())) {
                        om.setShareOutput();
                    }
                }
            }
        }
        for (int i = 0; i < list.size(); i++) {
            Outputmethod om = list.get(i);
            if (om.isClassDefinition()) {
                for (int j = i + 1; j < list.size(); j++) {
                    Outputmethod om2 = list.get(j);
                    if (om2.isClassDefinition() && om.getClassName().equals(om2.getClassName())) {
                        writer.writeln("\n\"" + om.getClassName() + "\" already defined in row " + om.getRow() + ". Error in row " + om2.getRow() + ":\n" + om.getRow() + ". " + om.getStatement() + "\n" + om2.getRow() + ". " + om2.getStatement());
                        return false;
                    }
                }
            }
        }
        for (int i = 0; i < list.size(); i++) {
            Outputmethod om = list.get(i);
            for (int j = i + 1; j < list.size(); j++) {
                Outputmethod om2 = list.get(j);
                if (om.getArgumentSignature().equals(om2.getArgumentSignature())) {
                    writer.writeln("\nElement \"" + om.getArgName() + "\" already defined for \"" + om.getCargName() + "\" in row " + om.getRow() + "\nwith the same argument signature, error in row " + om2.getRow() + ":\n" + om.getRow() + ". " + om + "\n" + om2.getRow() + ". " + om2);
                    return false;
                }
            }
        }
        for (Outputmethod om : list) {
            if (!om.isReferenced() && !om.isShareOutput()) {
                writer.writeln("Element \"" + om.getArgName() + "\" not found in \"" + om.getCargName() + "\", error in row " + om.getRow() + ":\n   " + om.getStatement());
                return false;
            }
        }
        return true;
    }
