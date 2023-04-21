    ExtendedOutputStream(File file, boolean append, boolean lock) throws FileLockedException, ReadWriteException {
        Checker.checkNull(file, "file");
        _path = file.getAbsolutePath();
        _std = false;
        if (Locks.isLocked(_path)) {
            throw new FileLockedException(_path, null);
        }
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(file, append);
        } catch (FileNotFoundException fnfe) {
            _ensureStreamIsClosed(out);
            throw Translator.translateFNFE(fnfe, _path);
        }
        _out = new BufferedOutputStream(out, Streams.getDefaultBufferSize());
        _isOpen = true;
        if (lock) {
            try {
                _lock = out.getChannel().tryLock();
                if (_lock == null) {
                    _ensureStreamIsClosed(out);
                    throw new FileLockedException(_path, null);
                }
                Locks.lock(_path);
            } catch (OverlappingFileLockException ofle) {
                _ensureStreamIsClosed(out);
                throw new FileLockedException(_path, ofle);
            } catch (FileLockedException fle) {
                throw fle;
            } catch (IOException io) {
                _ensureStreamIsClosed(out);
                throw Translator.translateIOE(io, _path);
            }
        } else {
            _lock = null;
        }
    }
