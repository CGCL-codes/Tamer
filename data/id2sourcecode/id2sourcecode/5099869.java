    public synchronized Class resolveScript(IScriptReference script) {
        Class result = findDeployedScript(script);
        if (result != null) {
            return result;
        }
        URL url = script.getResourceLocation().getResourceURL();
        if (url == null) {
            return null;
        }
        String urlString = url.toString();
        Long modifiedInExternalStorage = new Long(getLastModified(url));
        Long lastModified = (Long) loadedScripts.get(urlString);
        if (lastModified != null && lastModified.longValue() < modifiedInExternalStorage.longValue()) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Creating new groovy loader because " + url + " changed");
            }
            this.groovyLoader = new GroovyClassLoader(this.parent);
            this.loadedScripts = new HashMap();
        }
        try {
            InputStream is = url.openStream();
            try {
                BufferedInputStream bis = new BufferedInputStream(is);
                result = this.groovyLoader.parseClass(bis, script.getResourceLocation().getName());
                if (LOG.isDebugEnabled() && this.loadedScripts.containsKey(urlString) == false) {
                    LOG.debug("New script loaded" + script);
                    LOG.debug("name=" + result.getName());
                    LOG.debug("scriptClass=" + script.getScriptClassName());
                }
                if (script.getScriptClassName().equals(result.getName()) == false) {
                    ILocation loc = new Location(script.getResourceLocation(), 0, 0);
                    throw new ApplicationRuntimeException(Messages.format("ScriptResolver.bad-classname", script.getScriptClassName(), result.getName()), script.getComponent(), loc, null);
                }
                loadedScripts.put(urlString, modifiedInExternalStorage);
                return result;
            } finally {
                is.close();
            }
        } catch (ApplicationRuntimeException are) {
            throw are;
        } catch (CompilationFailedException ex) {
            throw convertToApplicationRuntimeException(ex, script.getComponent(), script.getResourceLocation());
        } catch (GroovyRuntimeException ex) {
            ASTNode node = ex.getNode();
            Location location = new Location(script.getResourceLocation(), node.getLineNumber(), node.getColumnNumber());
            throw new ApplicationRuntimeException(ex.getMessage(), script.getComponent(), location, ex);
        } catch (Throwable t) {
            throw new ApplicationRuntimeException(Messages.format("ScriptResolver.script-not-loaded", script.getResourceLocation().getName(), t.getMessage()), t);
        }
    }
