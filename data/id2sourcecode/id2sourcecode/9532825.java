    private void process(final PsiFile file) {
        if (!file.isWritable() && !FileDocumentManager.fileForDocumentCheckedOutSuccessfully(PsiDocumentManager.getInstance(myProject).getDocument(file), myProject)) {
            Messages.showMessageDialog(myProject, "Cannot modify process read-only file.", "File Is Read-Only", Messages.getErrorIcon());
        } else {
            final Runnable[] resultRunnable = new Runnable[1];
            Runnable read = new Runnable() {

                public void run() {
                    if (!isWritable(file)) {
                        return;
                    }
                    try {
                        resultRunnable[0] = preprocessFile(file);
                    } catch (IncorrectOperationException incorrectoperationexception) {
                        logger.error(incorrectoperationexception);
                    }
                }
            };
            Runnable write = new Runnable() {

                public void run() {
                    if (resultRunnable[0] != null) {
                        resultRunnable[0].run();
                    }
                }
            };
            execute(read, write);
        }
    }
