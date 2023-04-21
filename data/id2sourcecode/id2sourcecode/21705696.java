    public synchronized LoadedScriptInfo loadScript(String name, String version, InputStream in) throws ObolException, IOException {
        ScriptInputStream _scriptIn = null;
        if (in instanceof ScriptInputStream) {
            _scriptIn = (ScriptInputStream) in;
        } else {
            _scriptIn = new ScriptInputStream(in);
        }
        byte[] _rawscript = _scriptIn.findScript(name, version);
        if (null == _rawscript) {
            log.debug(".loadScript(): returning null!");
            return null;
        }
        ByteArrayInputStream _bin = new ByteArrayInputStream(_rawscript);
        Parser _parser = ObolParser.getInstance(_bin);
        _parser.setParseOnly(true);
        DummyContext _ctx = new DummyContext();
        _parser.setCurrentContext(_ctx);
        _parser.parse("scriptHeader");
        _ctx.clear();
        _bin.close();
        String _name = _parser.getScriptName();
        String _documentation = _parser.getScriptDocumentation();
        String _version = _parser.getScriptVersion();
        if (null != version && null != _version) {
            if (false == version.equals(_version)) {
                log.debug(".loadScript(): version mismatch, returning null!");
                return null;
            }
        }
        byte[] _digestBinary = null;
        try {
            MessageDigest _md = MessageDigest.getInstance(this.scriptDigestAlgo);
            _digestBinary = _md.digest(_rawscript);
        } catch (GeneralSecurityException e) {
            throw (ObolException) new ObolException(__me + ".loadScript():" + " error generating " + this.scriptDigestAlgo + " digest of loaded script!").initCause(e);
        }
        String _digest = (this.scriptDigestAlgo + ":" + Hex.toString(_digestBinary) + "/" + _rawscript.length);
        LoadedScriptInfo _info = new LoadedScriptInfoImpl(_name, _version, _documentation, _digest, _scriptIn.getScriptLineNumber());
        boolean _checkDuplicates = true;
        if (false == this.loadedScripts.containsKey(_name)) {
            this.loadedScripts.put(_name, _rawscript);
            this.loadedScriptsInfo.put(_name, _info);
            _checkDuplicates = false;
        }
        String _versionedKey = _name;
        if (null != _version) {
            _versionedKey = this.makeVersionedKey(_name, _version);
            if (false == this.loadedScripts.containsKey(_versionedKey)) {
                this.loadedScripts.put(_versionedKey, _rawscript);
                this.loadedScriptsInfo.put(_versionedKey, _info);
                _checkDuplicates = false;
            }
        }
        if (_checkDuplicates && this.loadedScripts.containsKey(_versionedKey)) {
            LoadedScriptInfo _existing = (LoadedScriptInfo) loadedScriptsInfo.get(_versionedKey);
            if (false == _digest.equals(_existing.getDigest())) {
                throw new ObolException(__me + ".loadScript(): **ERROR** " + "script named \"" + _name + "\" and version \"" + _version + "\" already exists with  digest=" + _existing.getDigest() + ", while loaded script has DIFFERENT digest=" + _digest);
            }
        }
        return _info;
    }
