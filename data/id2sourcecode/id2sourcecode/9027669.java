    private static void print(Object obj, OutPort out, boolean readable) {
        boolean saveReadable = out.printReadable;
        AbstractFormat saveFormat = out.objectFormat;
        try {
            out.printReadable = readable;
            AbstractFormat format = readable ? Scheme.writeFormat : Scheme.displayFormat;
            out.objectFormat = format;
            format.writeObject(obj, (gnu.lists.Consumer) out);
        } finally {
            out.printReadable = saveReadable;
            out.objectFormat = saveFormat;
        }
    }
