    @Override
    public void add(genomeObject obj) {
        String readsLine = String.format("%s\t%d\t%d", obj.getChr(), obj.getStart(), ((bedObject) obj).getEnd());
        try {
            mWriter.write(readsLine);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Iterator<Object> it = ((bedObject) obj).getValueObject().iterator();
        while (it.hasNext()) {
            try {
                mWriter.write("\t" + it.next());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            mWriter.write("\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
