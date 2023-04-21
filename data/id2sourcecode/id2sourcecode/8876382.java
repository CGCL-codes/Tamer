    public void export(AbstractPage page, Notifiable notifiable) {
        if (page == null) return;
        if (!this.locationManager.exportable(page)) {
            this.message(notifiable, "msg.locked-page", null, page, null);
            return;
        }
        this.debug("msg.exporting-page", null, page, null);
        try {
            final VelocityContext context = new VelocityContext();
            final Template template = this.templatesManager.getTemplate(page.getSpaceKey());
            final String body = this.wikiStyleRenderer.convertWikiToXHtml(page.toPageContext(), page.getContent());
            final String styleUri = this.locationManager.getLocation(page.getSpace(), "space.css").toString();
            final String confluenceUrl = this.configurationManager.getConfluenceUrl();
            final ViewPageAction action = new ViewPageAction();
            ContainerManager.autowireComponent(action);
            action.setPage(page);
            context.put("generalUtil", new GeneralUtil());
            context.put("webwork", new TextUtils());
            context.put("autoexport", new ExportUtils(this.configurationManager, this.wikiStyleRenderer, this.pluginAccessor));
            context.put("pageManager", this.pageManager);
            context.put("confluenceUri", confluenceUrl);
            context.put("stylesheet", styleUri);
            context.put("action", action);
            context.put("page", page);
            context.put("body", body);
            context.put("req", ActionContext.getContext().get(AC_REQUEST_KEY));
            final File pageFile = this.locationManager.getFile(page);
            final File spaceDir = pageFile.getParentFile();
            if (!spaceDir.isDirectory()) spaceDir.mkdirs();
            try {
                final StringWriter writer = new StringWriter();
                template.merge(context, writer);
                writer.flush();
                writer.close();
                final ExportBeautifier beautifier = new ExportBeautifier(page, this.configurationManager, this.pageManager, this.spaceManager, this.locationManager);
                beautifier.beautify(writer.toString(), pageFile);
            } catch (MethodInvocationException exception) {
                Throwable throwable = exception.getWrappedThrowable();
                if (throwable != null) this.error(notifiable, throwable, "err.invoking-method", null, page, null);
                this.error(notifiable, exception, "err.exporting-page", null, page, null);
                System.err.println(exception.getReferenceName());
            } catch (Exception exception) {
                this.error(notifiable, exception, "err.exporting-page", null, page, null);
            }
            final Iterator iterator = page.getAttachments().iterator();
            while (iterator.hasNext()) {
                final Attachment attachment = (Attachment) iterator.next();
                final File aFile = this.locationManager.getFile(attachment, false);
                final File aDir = aFile.getParentFile();
                if (!aDir.isDirectory()) aDir.mkdirs();
                try {
                    final InputStream aInput = attachment.getContentsAsStream();
                    FileUtils.copyFile(aInput, aFile, true);
                    aInput.close();
                    this.debug("msg.exported-attachment", null, page, aFile.getName());
                } catch (IOException exception) {
                    this.error(notifiable, exception, "err.exporting-attachment", null, page, aFile.getName());
                }
                if (!this.thumbnailManager.isThumbnailable(attachment)) continue;
                this.thumbnailManager.getThumbnail(attachment);
                final File sFile = this.thumbnailManager.getThumbnailFile(attachment);
                if (sFile.exists()) try {
                    final File tFile = this.locationManager.getFile(attachment, true);
                    final File tDir = tFile.getParentFile();
                    if (!tDir.isDirectory()) tDir.mkdirs();
                    if (tFile.exists()) tFile.delete();
                    FileUtils.copyFile(sFile, tFile);
                    this.debug("msg.exported-thumbnail", null, page, tFile.getName());
                } catch (IOException exception) {
                    this.error(notifiable, exception, "err.exporting-thumbnail", null, page, aFile.getName());
                }
            }
        } catch (Exception exception) {
            this.error(notifiable, exception, "err.exporting-page", null, page, null);
        }
        this.message(notifiable, "msg.exported-page", null, page, null);
    }
