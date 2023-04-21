    public void renderInfo(Renderer renderer) {
        System.out.println("!!RENDER INFO CALLED!!");
        Graphics g = renderer.getGraphics();
        g.setFont(new Font(fontFace, fontStyle1 + fontStyle2, fontSize));
        FontMetrics fm = g.getFontMetrics();
        g.setColor(fColor);
        if (renderer.title) {
            System.out.println("Found title tag");
            renderer.getGUI().setTitle("Rendava -  " + text);
            return;
        }
        if (text != null) {
            int tempX = renderer.getX();
            System.out.println("X is " + tempX);
            int stringWidth = fm.stringWidth(text);
            int stringHeight = fm.getHeight();
            int tempRoom = (renderer.getCanvasWidth() - renderer.getX());
            System.out.println("String width is: " + stringWidth);
            System.out.println("Room left is " + tempRoom);
            if ((renderer.getCanvasWidth() - renderer.getX()) > stringWidth) {
                System.out.println("Rendering string straight away as we can fit it in the window");
                System.out.println("String is: " + text);
                g.drawString(text, renderer.getX(), renderer.getY());
                renderer.setX(renderer.getX() + stringWidth);
            } else {
                while (renderPoint < text.length()) {
                    int charsLeft = text.length() - renderPoint;
                    renderSpace = renderer.getCanvasWidth() - renderer.getX();
                    charsFit = Math.round(renderSpace / fm.stringWidth(oneChar));
                    System.out.println("We have room for: " + charsFit);
                    if (charsFit < charsLeft) {
                        System.out.println("Text left to render is larger than space left");
                        int tempXc = renderer.getX();
                        System.out.println("X is " + tempXc);
                        if (text.charAt((renderPoint + charsFit)) == ' ') {
                            nextLine = text.substring(renderPoint, (renderPoint + charsFit));
                            System.out.println("nextLine = " + nextLine);
                            System.out.println("No need for space alterartion");
                            g.drawString(nextLine, renderer.getX(), renderer.getY());
                            renderer.setX(0);
                            renderer.setY(renderer.getY() + stringHeight);
                            renderPoint = renderPoint + charsFit;
                        } else {
                            while (text.charAt((renderPoint + charsFit)) != ' ') {
                                charsFit--;
                            }
                            nextLine = text.substring(renderPoint, (renderPoint + charsFit));
                            System.out.println("nextLine = " + nextLine);
                            System.out.println("Split at space");
                            g.drawString(nextLine, renderer.getX(), renderer.getY());
                            renderer.setX(0);
                            renderer.setY(renderer.getY() + stringHeight);
                            renderPoint = renderPoint + charsFit;
                        }
                    }
                    if (charsFit > charsLeft) {
                        System.out.println("Text left to render is smaller than space left");
                        int tempXb = renderer.getX();
                        System.out.println("X is " + tempXb);
                        nextLine = text.substring(renderPoint, text.length());
                        System.out.println("nextLine = " + nextLine);
                        g.drawString(nextLine, renderer.getX(), renderer.getY());
                        renderPoint = renderPoint + charsFit;
                        int substringWidth = fm.stringWidth(nextLine);
                        renderer.setX(renderer.getX() + substringWidth);
                    }
                }
                int tempXc = renderer.getX();
                System.out.println("X is " + tempXc);
                System.out.println("END OF LOOP REACHED!");
            }
        }
    }
