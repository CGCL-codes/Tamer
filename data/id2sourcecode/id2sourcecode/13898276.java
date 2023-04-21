    private void copyAttachmentsToQuarantine() throws Exception {
        if (ctx.settings.isArchiveAttachments()) {
            String path = ctx.archiveFilePath;
            FileUtils.copyFile(new File(path), new File(ctx.quarantineFolder + "/" + FilenameUtils.getName(path)));
            originalCtx.numberOfQuarantinedFiles += 1;
        } else for (String path : ctx.attachments) {
            FileUtils.copyFile(new File(path), new File(ctx.quarantineFolder + "/" + FilenameUtils.getName(path)));
            originalCtx.numberOfQuarantinedFiles += 1;
        }
    }
