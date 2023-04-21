    public FileChannel createInputChannel(TPath path) throws TIoException {
        return ((FileInputStream) createInputStream(path)).getChannel();
    }
