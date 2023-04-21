    @Override
    protected IStatus run(IProgressMonitor monitor) {
        IStatus result;
        IOclParser parser;
        parser = Ocl2Parser.INSTANCE;
        try {
            monitor.beginTask("Parse file " + this.url.toString() + " ...", 100);
            parser.doParse(this.model, new InputStreamReader(this.url.openStream()));
            monitor.worked(99);
            ModelBusUIUtility.setActiveView(ModelBusUIPlugin.MODELS_VIEW_ID);
            monitor.worked(1);
            monitor.done();
            result = new Status(IStatus.OK, ParserUIPlugin.ID, "Parsing finished successfully.");
        } catch (ParseException e) {
            String msg;
            msg = ParserUIMessages.ParseOCLWizard_ErrorOccuredDuringParsing;
            if (e.getMessage() != null) {
                msg += e.getMessage();
            } else {
                msg += ParserUIMessages.ParseOCLWizard_CheckLog;
            }
            LOGGER.error(msg, e);
            result = new Status(IStatus.ERROR, ParserUIPlugin.ID, msg);
        } catch (MalformedURLException e) {
            String msg;
            msg = ParserUIMessages.ParseOCLWizard_UnexpectedError;
            LOGGER.error(msg, e);
            result = new Status(IStatus.ERROR, ParserUIPlugin.ID, msg);
        } catch (IllegalStateException e) {
            String msg;
            msg = ParserUIMessages.ParseOCLWizard_UnexpectedError;
            LOGGER.error(msg, e);
            result = new Status(IStatus.ERROR, ParserUIPlugin.ID, msg);
        } catch (IOException e) {
            String msg;
            msg = ParserUIMessages.ParseOCLWizard_UnexpectedError;
            LOGGER.error(msg, e);
            result = new Status(IStatus.ERROR, ParserUIPlugin.ID, msg);
        }
        return result;
    }
