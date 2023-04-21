    public void run() throws IOException {
        InputStream in;
        String cl;
        String fn;
        int n;
        while (!stack_.empty()) {
            cl = (String) stack_.pop();
            if (!closure_.contains(cl)) {
                closure_.add(cl);
                fn = cl + ".class";
                for (n = 0; n < classpath_.length; n++) {
                    in = classpath_[n].getInputStream(fn);
                    if (in == null) {
                        continue;
                    }
                    try {
                        cf_.clear();
                        cf_.load(in);
                        push(cf_);
                    } finally {
                        in.close();
                    }
                    if (mfd_ != null) {
                        in = cf_.toInputStream();
                        mfd_.digest(fn, in);
                        in.close();
                    }
                    if (export_ == null) {
                        break;
                    }
                    in = cf_.toInputStream();
                    try {
                        export(in, fn);
                    } finally {
                        in.close();
                    }
                    if (java_) {
                        fn = cl + ".java";
                        in = classpath_[n].getInputStream(fn);
                        if (in != null) {
                            try {
                                export(in, fn);
                            } finally {
                                in.close();
                            }
                        }
                    }
                    break;
                }
            }
        }
    }
