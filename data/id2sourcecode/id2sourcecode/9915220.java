    protected boolean createImpl() throws GLException {
        long eglDisplay = ((EGLDrawable) drawable).getDisplay();
        EGLGraphicsConfiguration config = ((EGLDrawable) drawable).getGraphicsConfiguration();
        GLProfile glProfile = drawable.getGLProfile();
        long eglConfig = config.getNativeConfig();
        long shareWith = EGL.EGL_NO_CONTEXT;
        if (eglDisplay == 0) {
            throw new GLException("Error: attempted to create an OpenGL context without a display connection");
        }
        if (eglConfig == 0) {
            throw new GLException("Error: attempted to create an OpenGL context without a graphics configuration");
        }
        try {
            if (!EGL.eglBindAPI(EGL.EGL_OPENGL_ES_API)) {
                throw new GLException("eglBindAPI to ES failed , error 0x" + Integer.toHexString(EGL.eglGetError()));
            }
        } catch (GLException glex) {
            if (DEBUG) {
                glex.printStackTrace();
            }
        }
        EGLContext other = (EGLContext) GLContextShareSet.getShareContext(this);
        if (other != null) {
            shareWith = other.getHandle();
            if (shareWith == 0) {
                throw new GLException("GLContextShareSet returned an invalid OpenGL context");
            }
        }
        int[] contextAttrs = new int[] { EGL.EGL_CONTEXT_CLIENT_VERSION, -1, EGL.EGL_NONE };
        if (glProfile.usesNativeGLES2()) {
            contextAttrs[1] = 2;
        } else if (glProfile.usesNativeGLES1()) {
            contextAttrs[1] = 1;
        } else {
            throw new GLException("Error creating OpenGL context - invalid GLProfile: " + glProfile);
        }
        contextHandle = EGL.eglCreateContext(eglDisplay, eglConfig, shareWith, contextAttrs, 0);
        if (contextHandle == 0) {
            throw new GLException("Error creating OpenGL context: eglDisplay " + toHexString(eglDisplay) + ", eglConfig " + toHexString(eglConfig) + ", " + glProfile + ", error " + toHexString(EGL.eglGetError()));
        }
        GLContextShareSet.contextCreated(this);
        if (DEBUG) {
            System.err.println(getThreadName() + ": !!! Created OpenGL context 0x" + Long.toHexString(contextHandle) + ",\n\twrite surface 0x" + Long.toHexString(drawable.getHandle()) + ",\n\tread  surface 0x" + Long.toHexString(drawableRead.getHandle()) + ",\n\t" + this + ",\n\tsharing with 0x" + Long.toHexString(shareWith));
        }
        if (!EGL.eglMakeCurrent(((EGLDrawable) drawable).getDisplay(), drawable.getHandle(), drawableRead.getHandle(), contextHandle)) {
            throw new GLException("Error making context 0x" + Long.toHexString(contextHandle) + " current: error code " + EGL.eglGetError());
        }
        int ctp = CTX_PROFILE_ES | CTX_OPTION_ANY;
        int major;
        if (glProfile.usesNativeGLES2()) {
            ctp |= CTX_PROFILE_ES2_COMPAT;
            major = 2;
        } else {
            major = 1;
        }
        setGLFunctionAvailability(true, major, 0, ctp);
        return true;
    }
