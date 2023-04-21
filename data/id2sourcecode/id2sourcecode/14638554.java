    private boolean removeKeywordsFromPicture(List keywordsPicture, Picture picture) {
        Iterator itFaces = picture.getFaces().getFaces().iterator();
        boolean isPictureNeedsWriting = false;
        while (itFaces.hasNext()) {
            Face face = (Face) itFaces.next();
            boolean b = face.isToBeRemoved();
            if (b) {
                boolean hasRemoved = false;
                String keywordToRemove = face.getName();
                boolean contains = keywordsPicture.contains(keywordToRemove);
                if (contains) {
                    this.logger.log(Level.FINER, "Will remove the Picasa (full) name ''{0}''.", keywordToRemove);
                    keywordsPicture.remove(keywordToRemove);
                    isPictureNeedsWriting = true;
                    hasRemoved = true;
                }
                this.logger.log(Level.FINER, "Will remove the Picasa (full) name ''{0}''.", keywordToRemove);
                keywordToRemove = face.getDisplay();
                contains = keywordsPicture.contains(keywordToRemove);
                if (contains) {
                    this.logger.log(Level.FINER, "Will remove the Picasa display name ''{0}''.", keywordToRemove);
                    keywordsPicture.remove(keywordToRemove);
                    isPictureNeedsWriting = true;
                    hasRemoved = true;
                }
                if (!hasRemoved) {
                    this.logger.warning("The programm flow should never come here. Faild to remove a face from the keywords list. This list is later used be exiftool to write the keywords to file. What has done befor? The picasa faces of this picture where read. The keywords in the picture where read. At least one face in the keywords are marked as to be removed from the picture. Now it should be removed from an internal keywords list but this fails.");
                }
            }
        }
        return isPictureNeedsWriting;
    }
