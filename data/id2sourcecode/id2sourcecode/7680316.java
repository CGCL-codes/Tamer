    protected Resolution handleExecute(ActionBeanContext actionBeanContext, PropertyDescriptor pd) {
        if (data == null) {
            actionBeanContext.getValidationErrors().add("command.data", new SimpleError("Submitted file contains no data !"));
            return actionBeanContext.getSourcePageResolution();
        } else {
            Blob blob;
            try {
                blob = Hibernate.createBlob(data.getInputStream());
            } catch (IOException e) {
                String msg = "IOException caught while trying to create Blob from FileBean";
                logger.error(msg, e);
                throw new RuntimeException(msg, e);
            }
            if (blob == null) {
                String msg = "Hibernate returned null when trying to create Blob from FileBean !";
                logger.error(msg);
                throw new RuntimeException(msg);
            }
            Method writeMethod = pd.getWriteMethod();
            if (writeMethod == null) {
                String msg = "Property " + getTargetObject().getClass().getSimpleName() + "." + getBlobPropertyName() + " is read only (no write method found)";
                logger.error(msg);
                throw new RuntimeException(msg);
            }
            try {
                writeMethod.invoke(getTargetObject(), new Object[] { blob });
            } catch (Exception e) {
                String msg = "Error while setting Blob property " + getTargetObject().getClass().getSimpleName() + "." + getBlobPropertyName() + " using write method";
                logger.error(msg);
                throw new RuntimeException(msg);
            }
            try {
                getSession().update(getTargetObject());
                getPersistenceUtil().commit();
                actionBeanContext.getMessages().add(new SimpleMessage("Data transmitted"));
                return Util.redirectToView(getTargetObject(), getPersistenceUtil());
            } catch (Throwable t) {
                logger.error("Error while saving data", t);
                actionBeanContext.getValidationErrors().addGlobalError(new SimpleError("Error while transmitting the file. Maybe it's too long ?"));
                return actionBeanContext.getSourcePageResolution();
            }
        }
    }
