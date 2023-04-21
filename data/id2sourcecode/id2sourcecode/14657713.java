    public SwDataGenerator(IKernel kernel) throws InvocationTargetException {
        super(kernel);
        try {
            _md = MessageDigest.getInstance("SHA-512");
            try {
                IEventDispatchingChannel ev = (IEventDispatchingChannel) kernel().getChannel(IEventDispatchingChannel.class);
                ev.event(new AlgorithmEvent(null, getClass().getCanonicalName(), "SHA-512", EventCode.DigestAlgorithmUsed.getCode()));
            } catch (ModuleNotFoundException m) {
            }
        } catch (NoSuchAlgorithmException e) {
            try {
                _md = MessageDigest.getInstance("SHA-256");
                try {
                    IEventDispatchingChannel ev = (IEventDispatchingChannel) kernel().getChannel(IEventDispatchingChannel.class);
                    ev.event(new AlgorithmEvent(null, getClass().getCanonicalName(), "SHA-256", EventCode.DigestAlgorithmUsed.getCode()));
                } catch (ModuleNotFoundException m) {
                }
            } catch (NoSuchAlgorithmException e1) {
                try {
                    _md = MessageDigest.getInstance("SHA-1");
                    try {
                        IEventDispatchingChannel ev = (IEventDispatchingChannel) kernel().getChannel(IEventDispatchingChannel.class);
                        ev.event(new AlgorithmEvent(null, getClass().getCanonicalName(), "SHA-1", EventCode.DigestAlgorithmUsed.getCode()));
                    } catch (ModuleNotFoundException m) {
                    }
                } catch (NoSuchAlgorithmException e2) {
                    try {
                        _md = MessageDigest.getInstance("MD5");
                        try {
                            IEventDispatchingChannel ev = (IEventDispatchingChannel) kernel().getChannel(IEventDispatchingChannel.class);
                            ev.event(new AlgorithmEvent(null, getClass().getCanonicalName(), "MD5", EventCode.DigestAlgorithmUsed.getCode()));
                        } catch (ModuleNotFoundException m) {
                        }
                    } catch (NoSuchAlgorithmException e3) {
                        SecurityException s = new SecurityException(e3, getClass().getCanonicalName(), ErrorCode.NoAlgorithmError.getCode());
                        throw new InvocationTargetException(new ModuleException(s, getClass().getCanonicalName(), org.swemas.core.ErrorCode.ObjectConstructionError.getCode()));
                    }
                }
            }
        }
    }
