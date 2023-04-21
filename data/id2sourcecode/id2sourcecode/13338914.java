    Object _clone() throws CloneNotSupportedException {
        try {
            this._context.startCreation();
        } catch (ShuttingDownException ex) {
            throw new CloneNotSupportedException("Shutting down");
        }
        synchronized (this.lock) {
            boolean success = false;
            try {
                this.checkValidation();
                this.checkActive();
                try {
                    this.dispatchEvent(new CloneEvent(AgletEvent.nextID(), this.proxy, EventType.CLONING));
                } catch (SecurityException ex) {
                    throw ex;
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                this.suspendMessageManager();
                LocalAgletRef clone_ref = new LocalAgletRef(this._context, this._secure);
                Certificate owner = this._owner;
                Name new_name = AgletRuntime.newName(owner);
                clone_ref.setName(new_name);
                clone_ref.info = new AgletInfo(MAFUtil.toAgletID(new_name), this.info.getAgletClassName(), this.info.getCodeBase(), this._context.getHostingURL().toString(), System.currentTimeMillis(), this.info.getAPIMajorVersion(), this.info.getAPIMinorVersion(), owner);
                AgletWriter writer = new AgletWriter();
                writer.writeAglet(this);
                clone_ref.createResourceManager(writer.getClassNames());
                AgletReader reader = new AgletReader(writer.getBytes());
                reader.readAglet(clone_ref);
                Aglet clone = clone_ref.aglet;
                clone_ref.protections = cloneProtections(this.protections);
                clone.setStub(clone_ref);
                clone_ref.proxy = new AgletProxyImpl(clone_ref);
                clone_ref.startClonedAglet(this._context, this.proxy);
                success = true;
                return clone_ref.proxy;
            } catch (ClassNotFoundException ex) {
                throw new CloneNotSupportedException("Class Not Found :" + ex.getMessage());
            } catch (IOException ex) {
                ex.printStackTrace();
                throw new CloneNotSupportedException("IO Exception :" + ex.getMessage());
            } catch (AgletException ex) {
                throw new CloneNotSupportedException("Aglet Exception :" + ex.getMessage());
            } catch (RuntimeException ex) {
                this.logger.error("Exception caught while processing a message", ex);
                throw ex;
            } finally {
                this.resumeMessageManager();
                if (success) {
                    this._context.log("Clone", this.info.getAgletClassName());
                } else {
                    this._context.log("Clone", "Failed to clone the aglet [" + this.info.getAgletClassName() + "]");
                }
                this.dispatchEvent(new CloneEvent(AgletEvent.nextID(), this.proxy, EventType.CLONED));
                this._context.endCreation();
            }
        }
    }
