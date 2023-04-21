    @Override
    protected final Object getNextUntested() {
        if (soFar > 0 && currThreads > 0 && soFar % currThreads == 0) {
            try {
                BufferedWriter done = new BufferedWriter(new FileWriter(doneFilesLocation));
                done.write("" + (soFar - currThreads));
                done.close();
            } catch (IOException e) {
                SimpleLog.getInstance().writeLog(3, "Não foi possivel contabilizar quantidade" + " de páginas baixadas até o momento.");
            }
        }
        Object[] next = null;
        if (!untested.isLast()) {
            next = untested.get();
            untested.next();
        }
        return next;
    }
