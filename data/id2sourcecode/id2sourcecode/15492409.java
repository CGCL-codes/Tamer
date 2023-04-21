    public void replaceChar(Char character, String savingPath) throws WrongPositionException {
        for (AssociationPageStains belka : character.getAssociationPageStains()) {
            if (name.compareTo(belka.getPageName()) == 0) {
                if (getWidth() <= (character.getWidth() + belka.getPositionCharInPage().x) || getHeight() <= (character.getHeight() + belka.getPositionCharInPage().y)) throw new WrongPositionException();
                for (int y = belka.getPositionCharInPage().y, yy = 0; yy < character.getHeight(); y++, yy++) for (int x = belka.getPositionCharInPage().x, xx = 0; xx < character.getWidth(); x++, xx++) if (character.getMatrix()[xx][yy]) image.writeGrayData(x, y, character.getImage().readGray(xx, yy));
                save(savingPath);
            }
        }
    }
