    public int read(String name) {
        _prof.prof.cnt[754]++;
        {
            _prof.prof.cnt[755]++;
            status = STATUS_OK;
        }
        {
            _prof.prof.cnt[756]++;
            try {
                {
                    _prof.prof.cnt[757]++;
                    name = name.trim().toLowerCase();
                }
                {
                    _prof.prof.cnt[758]++;
                    if ((name.indexOf("file:") >= 0) || (name.indexOf(":/") > 0)) {
                        _prof.prof.cnt[759]++;
                        URL url = new URL(name);
                        {
                            _prof.prof.cnt[760]++;
                            in = new BufferedInputStream(url.openStream());
                        }
                    } else {
                        {
                            _prof.prof.cnt[761]++;
                            in = new BufferedInputStream(new FileInputStream(name));
                        }
                    }
                }
                {
                    _prof.prof.cnt[762]++;
                    status = read(in);
                }
            } catch (IOException e) {
                {
                    _prof.prof.cnt[763]++;
                    status = STATUS_OPEN_ERROR;
                }
            }
        }
        {
            _prof.prof.cnt[764]++;
            return status;
        }
    }
