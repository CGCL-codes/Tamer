    public static void main(String[] args) {
        System.out.println("Hi");
        Gui g = new Gui();
        t = new JTextArea(20, 35);
        JScrollPane scr = new JScrollPane(t);
        scr.setFocusable(false);
        t.setEditable(false);
        t.setFont(new Font("Courier New", Font.PLAIN, 12));
        t.setWrapStyleWord(true);
        t.setTabSize(3);
        t.setLineWrap(true);
        t.setFocusable(false);
        g.addKeyListener(g);
        g.getContentPane().add(scr);
        g.setFocusable(true);
        g.setDefaultCloseOperation(g.EXIT_ON_CLOSE);
        g.pack();
        g.setLocationRelativeTo(null);
        g.setVisible(true);
        rows = 1;
        cols = 1;
        g.updateTitle();
        String mess = "<p>Pro's regex engine is fully " + "functional in the demo version. As a quick test, copy and paste the text of this page into EditPad Pro. </p>" + "<p >Then select Search|Show Search Panel from the menu. In the search pane that appears near the bottom, </p>" + "<p id=\"garbage\">type in regex in the box labeled \"Search Text\". Mark the \"Regular expression\" checkbox, and click the Find First button. This is</p>" + "<h1><a href=\"http://d1029336.u158.fluidhosting.com/\">Limeromics</a></h1>    <div class=\"description\">Limeromics.com is an interactive web site for cartoonists, writers, illustrators, and web surfers who enjoy both creating and reading limericks and comic strips.</div>   </div>  </div>  <hr>    <div id=\"content\" class=\"widecolumn\">        <h2>WordPress �</h2>  <p>This is a <a href=\"http://mu.wordpress.org/\">WordPress Mu</a> powered site.</p>  <p>You can: </p><ul><li><a href=\"http://d1029336.u158.fluidhosting.com/wp-login.php?action=register\">Register</a></li><li> <a href=\"http://d1029336.u158.fluidhosting.com/wp-login.php\">Log in</a></li><li> <a href=\"wp-signup.php\">Create a new blog</a></li><li> Edit this file at <code>wp-content/themes/home/home.php</code> with your favourite text editor and customize this screen.</li></ul>    <h3>The Latest News</h3>  <ul>  <strong>Site News</strong>  <li><a href=\"http://d1029336.u158.fluidhosting.com/blog/2009/07/29/another-post/\" rel=\"bookmark\" title=\"Permanent Link to Another Post\">Another Post </a></li>  <li><a href=\"http://d1029336.u158.fluidhosting.com/blog/2009/07/29/hello-world/\" rel=\"bookmark\" title=\"Permanent Link to Hello world!\">Hello world! </a></li>  </ul>   <ul>   <strong>Updated Blogs</strong>   <li><a href=\"http://d1029336.u158.fluidhosting.com/\">Limeromics</a></li> </ul>     </div>      <hr>  <div id=\"footer\">  <!-- If you'd like to support WordPress, having the \"powered by\" link somewhere on your blog is the best way; it's our only promotion or advertising. -->   <p>    Limeromics is proudly powered by    <a href=\"http://mu.wordpress.org/\">WordPress MU</a> running on <a href=\"http://d1029336.u158.fluidhosting.com/\">Limeromics</a>. <a href=\"http://d1029336.u158.fluidhosting.com/wp-signup.php\" title=\"Create a new blog\">Create a new blog</a> and join in the fun!  ";
        String clean = HtmlLite.clean(mess);
        t.setText(clean);
    }
