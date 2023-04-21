    protected void writeHtml(Writer writer) {
        PrintWriter pw = new PrintWriter(writer);
        pw.println("<head><title>Crawl beans in " + cj.getShortName() + "</title></head>");
        pw.println("<h1>Crawl beans in built job <i><a href='/engine/job/" + TextUtils.urlEscape(cj.getShortName()) + "'>" + cj.getShortName() + "</a></i></h1>");
        pw.println("Enter a bean path of the form <i>beanName</i>, <i>beanName.property</i>, <i>beanName.property[indexOrKey]</i>, etc.");
        pw.println("<form method='POST'><input type='text' name='beanPath' style='width:400px' value='" + beanPath + "'/>");
        pw.println("<input type='submit' value='view'/></form>");
        if (StringUtils.isNotBlank(beanPath)) {
            pw.println("<h2>Bean path <i>" + beanPath + "</i></h2>");
            try {
                int i = beanPath.indexOf(".");
                String beanName = i < 0 ? beanPath : beanPath.substring(0, i);
                Object namedBean = appCtx.getBean(beanName);
                Object target;
                if (i < 0) {
                    target = namedBean;
                    writeObject(pw, null, target, beanPath);
                } else {
                    BeanWrapperImpl bwrap = new BeanWrapperImpl(namedBean);
                    String propPath = beanPath.substring(i + 1);
                    target = bwrap.getPropertyValue(propPath);
                    Class<?> type = bwrap.getPropertyType(propPath);
                    if (bwrap.isWritableProperty(propPath) && (bwrap.getDefaultEditor(type) != null || type == String.class) && !Collection.class.isAssignableFrom(type)) {
                        pw.println(beanPath + " = ");
                        writeObject(pw, null, target);
                        pw.println("<a href=\"javascript:document.getElementById('editform').style.display='inline';void(0);\">edit</a>");
                        pw.println("<span id='editform' style=\'display:none\'>Note: it may not be appropriate/effective to change this value in an already-built crawl context.<br/>");
                        pw.println("<form  id='editform' method='POST'>");
                        pw.println("<input type='hidden' name='beanPath' value='" + beanPath + "'/>");
                        pw.println(beanPath + " = <input type='text' name='newVal' style='width:400px' value='" + target + "'/>");
                        pw.println("<input type='submit' value='update'/></form></span>");
                    } else {
                        writeObject(pw, null, target, beanPath);
                    }
                }
            } catch (BeansException e) {
                pw.println("<i style='color:red'>problem: " + e.getMessage() + "</i>");
            }
        }
        pw.println("<h2>All named crawl beans</h2");
        pw.println("<ul>");
        Set<Object> alreadyWritten = new HashSet<Object>();
        writeNestedNames(pw, appCtx.getBean("crawlController"), getBeansRefPath(), alreadyWritten);
        for (String name : appCtx.getBeanDefinitionNames()) {
            writeNestedNames(pw, appCtx.getBean(name), getBeansRefPath(), alreadyWritten);
        }
        pw.println("</ul>");
    }
