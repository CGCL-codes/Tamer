    private void addImageTag(String url, List<String> params, String wikitext) {
        String realUrl = "";
        if (url.startsWith(":")) {
            url = url.substring(1);
            Tag parent = tagtree.peekTagStack();
            AnchorTag aTag = new AnchorTag(parent);
            String alias = "";
            if (!params.isEmpty()) {
                alias = params.get(params.size() - 1);
            } else {
                alias = url;
            }
            tagtree.pushToStack(aTag);
            parseRecursively(alias);
            tagtree.reduceTagStack();
            realUrl = rootURL + url;
            aTag.addAttribute("href", realUrl);
            aTag.setWikitext(wikitext);
            parent.addChild(aTag);
            return;
        }
        String filename = url.substring(url.indexOf(":") + 1).replaceAll(" ", "_");
        try {
            md.update(filename.getBytes("UTF-8"));
            String digest = InterpreterUtils.byteArray2Hex(md.digest());
            realUrl = mediaUrl + digest.charAt(0) + "/" + digest.charAt(0) + digest.charAt(1) + "/" + URLEncoder.encode(filename, "UTF-8");
            Tag parent = tagtree.peekTagStack();
            ImageTag imgTag = new ImageTag(parent);
            imgTag.setWikitext(wikitext);
            imgTag.addAttribute("src", realUrl);
            imgTag.setLink(rootURL + url);
            ArrayList<String> params2 = new ArrayList<String>(params);
            for (String p : params2) {
                if (p.equals("thumb") || p.equals("thumbnail")) {
                    imgTag.setType(ImageTag.TYPE_THUMB);
                    params.remove(p);
                } else if (p.equals("frame")) {
                    imgTag.setType(ImageTag.TYPE_FRAME);
                    params.remove(p);
                } else if (p.equals("frameless")) {
                    imgTag.setType(ImageTag.TYPE_FRAMELESS);
                    params.remove(p);
                } else if (p.equals("border")) {
                    imgTag.setBorder(true);
                    params.remove(p);
                } else if (p.equals("right")) {
                    imgTag.setLocation(ImageTag.LOCATION_RIGHT);
                    params.remove(p);
                } else if (p.equals("left")) {
                    imgTag.setLocation(ImageTag.LOCATION_LEFT);
                    params.remove(p);
                } else if (p.equals("center")) {
                    imgTag.setLocation(ImageTag.LOCATION_CENTER);
                    params.remove(p);
                } else if (p.equals("none")) {
                    imgTag.setLocation(ImageTag.LOCATION_NONE);
                    params.remove(p);
                } else if (p.equals("baseline")) {
                    imgTag.setAlignment(ImageTag.ALIGN_BASELINE);
                    params.remove(p);
                } else if (p.equals("middle")) {
                    imgTag.setAlignment(ImageTag.ALIGN_MIDDLE);
                    params.remove(p);
                } else if (p.equals("sub")) {
                    imgTag.setAlignment(ImageTag.ALIGN_SUB);
                    params.remove(p);
                } else if (p.equals("super")) {
                    imgTag.setAlignment(ImageTag.ALIGN_SUPER);
                    params.remove(p);
                } else if (p.equals("text-top")) {
                    imgTag.setAlignment(ImageTag.ALIGN_TEXT_TOP);
                    params.remove(p);
                } else if (p.equals("text-bottom")) {
                    imgTag.setAlignment(ImageTag.ALIGN_TEXT_BOTTOM);
                    params.remove(p);
                } else if (p.equals("top")) {
                    imgTag.setAlignment(ImageTag.ALIGN_TOP);
                    params.remove(p);
                } else if (p.equals("bottom")) {
                    imgTag.setAlignment(ImageTag.ALIGN_BOTTOM);
                    params.remove(p);
                } else if (p.equals("upright")) {
                    imgTag.setUpright(true);
                    params.remove(p);
                } else if (p.matches("^upright=\\d+(.\\d+)?$")) {
                    String[] s = p.split("=");
                    float factor = 1.0f;
                    try {
                        factor = Float.parseFloat(s[1]);
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid float number " + s[1] + " in addImageTag() factor.");
                    }
                    imgTag.setUprightFactor(factor);
                    params.remove(p);
                } else if (p.matches("^\\d+px$")) {
                    String[] s = p.split("px");
                    long width = Long.parseLong(s[0]);
                    imgTag.setWidth(width);
                    params.remove(p);
                } else if (p.matches("^x\\d+px$")) {
                    String[] s = p.split("px");
                    long height = Long.parseLong(s[0].substring(1));
                    imgTag.setHeight(height);
                    params.remove(p);
                } else if (p.matches("^\\d+x\\d+px$")) {
                    String[] s = p.split("px");
                    String[] s2 = s[0].split("x");
                    long width = Long.parseLong(s2[0]);
                    long height = Long.parseLong(s2[1]);
                    imgTag.setWidth(width);
                    imgTag.setHeight(height);
                    params.remove(p);
                } else if (p.equals("link=")) {
                    imgTag.setEnableLink(false);
                    params.remove(p);
                } else if (p.startsWith("link=")) {
                    String link = p.substring(5);
                    imgTag.setLink(link);
                    params.remove(p);
                }
            }
            if (!params.isEmpty()) {
                String caption = params.get(params.size() - 1);
                imgTag.setCaption(caption);
                tagtree.pushToStack(imgTag);
                parseRecursively(caption);
                tagtree.reduceTagStack();
            }
            parent.addChild(imgTag);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
