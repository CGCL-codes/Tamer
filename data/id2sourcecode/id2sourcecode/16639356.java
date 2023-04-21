    protected static File verifyFile(String filename, Symbol sym, boolean write) throws ObolException, IOException {
        File _f = new File(filename);
        boolean _exists = _f.exists();
        if (_exists) {
            if (_f.isDirectory()) {
                throw new ObolException(__me + ".verifyFile(): \"" + filename + "\" is a directory, not a file!");
            }
            if (false == _f.isFile()) {
                throw new ObolException(__me + ".verifyFile(): \"" + filename + "\" is not a file!");
            }
        }
        boolean _mustExist = sym.existProperty(SymbolProperties.FileMustExistKeyword);
        boolean _mustNotExist = sym.existProperty(SymbolProperties.FileMustNotExistKeyword);
        if (_mustExist && _mustNotExist) {
            throw new ObolException(__me + ".verifyFile(): logic error - " + "file \"" + filename + "\" cannot be expected to both " + "exist and not exist!");
        }
        if (_mustExist && (false == _exists)) {
            throw new ObolException(__me + ".verifyFile(): file \"" + filename + "\" does not exist but is expected to!");
        }
        if (_mustNotExist && _exists) {
            throw new ObolException(__me + ".verifyFile(): " + "file \"" + filename + "\" does exist when expected not to!");
        }
        boolean _create = sym.existProperty(SymbolProperties.FileCreateKeyword);
        boolean _overwrite = sym.existProperty(SymbolProperties.FileOverwriteKeyword);
        boolean _append = sym.existProperty(SymbolProperties.FileAppendKeyword);
        boolean _rename = sym.existProperty(SymbolProperties.FileRenameKeyword);
        if (_mustExist && _create) {
            throw new ObolException(__me + ".verifyFile(): logic error - " + "cannot simultaneously create and require existence " + "of file \"" + filename + "\"!");
        }
        if (_append && _overwrite) {
            throw new ObolException(__me + ".verifyFile(): logic error - " + "cannot both append to and overwrite file \"" + filename + "\"!");
        }
        if (_rename && (_append || _overwrite || _create)) {
            throw new ObolException(__me + ".verifyFile(): logic error - " + "cannot combine rename with append, overwrite or " + "create for file \"" + filename + "\"!");
        }
        if (_create && (_append || _overwrite || _rename)) {
            throw new ObolException(__me + ".verifyFile(): logic error - " + "cannot combine create with append, overwrite or " + "rename for file \"" + filename + "\"!");
        }
        if (write) {
            if (_exists) {
                if (_rename) {
                    File _renamedFile = null;
                    for (long _i = 0; ; _i++) {
                        if (_i > 1000) {
                            log.error("[NONFATAL] verifyFile(): number of " + "file-rename versions exceeds 1000 for " + "file \"" + filename + "\" -- " + "filesystem housekeeping seriously " + "recommended!");
                        }
                        _renamedFile = new File(filename + "." + _i);
                        if (false == _renamedFile.exists()) {
                            if (_f.renameTo(_renamedFile)) {
                                log.info("verifyFile() [write rename]: renamed " + "old file to \"" + _renamedFile + "\"");
                                File _newFile = new File(filename);
                                if (_newFile.createNewFile()) {
                                    log.info("verifyFile() [write rename]: " + "created new file \"" + filename + "\"");
                                    _f = _newFile;
                                } else {
                                    log.error("verifyFile() [write rename]: " + "failed created new file \"" + filename + "\", attempting rename rollback");
                                    if (_f.renameTo(new File(filename))) {
                                        log.error("verifyFile() [write rename]: " + "sucessful rename rollback to " + "old filename \"" + filename + "\"");
                                    }
                                    throw new ObolException(__me + "verifyFile() " + "[write, rename]: FAILED to rename-create " + "file \"" + filename + "\"");
                                }
                            }
                        }
                    }
                } else {
                    if (false == (_overwrite || _append)) {
                        throw new ObolException(__me + ".verifyFile() \"" + filename + "\" already exists, and no overwrite " + "or append keyword. ");
                    }
                }
            } else {
                if (_f.createNewFile()) {
                    log.info("verifyFile() [write]: " + "created new file \"" + filename + "\"");
                } else {
                    throw new ObolException(__me + "verifyFile() " + "[write]: FAILED to create new file \"" + filename + "\"");
                }
            }
            if (false == _f.canWrite()) {
                throw new ObolException(__me + ".verifyFile() [write mode]: " + "no write-permission to file \"" + filename + "\"!");
            }
        } else {
            if (false == _exists) {
                if (_create) {
                    if (_f.createNewFile()) {
                        log.info("verifyFile()[read, create]: created file " + "\"" + filename + "\"");
                    } else {
                        throw new ObolException(__me + "verifyFile() " + "[read, create]: FAILED to created file " + "\"" + filename + "\"");
                    }
                }
            }
            if (false == _f.canRead()) {
                throw new ObolException(__me + ".verifyFile() [read mode]: " + "no read-permission to file \"" + filename + "\"!");
            }
        }
        return _f;
    }
