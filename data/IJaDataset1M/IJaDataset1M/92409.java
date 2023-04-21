package espider.gui.mainWindow.guielements;

import java.sql.SQLException;
import java.util.ArrayList;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import espider.Espider;
import espider.gui.mainWindow.guielements.news.AddNews;
import espider.news.SpiderNews;

public class NewsTab extends CTabItem {

    private static NewsTab instance;

    private ScrolledComposite contentPane;

    Image iMovie = new Image(Espider.getInstance().getDisplay(), new ImageData("./images/filetype/video.png"));

    Image iMusic = new Image(Espider.getInstance().getDisplay(), new ImageData("./images/filetype/music.png"));

    Image iCompress = new Image(Espider.getInstance().getDisplay(), new ImageData("./images/filetype/compress.png"));

    Image iImage = new Image(Espider.getInstance().getDisplay(), new ImageData("./images/filetype/image.png"));

    Image iUnknow = new Image(Espider.getInstance().getDisplay(), new ImageData("./images/filetype/unknow.png"));

    Image iNoNews = new Image(Espider.getInstance().getDisplay(), new ImageData("./images/system/nonews.png"));

    Image iAdd = new Image(Espider.getInstance().getDisplay(), new ImageData("./images/system/add.png"));

    Image iRefresh = new Image(Espider.getInstance().getDisplay(), new ImageData("./images/system/refresh.png"));

    Image iRemove = new Image(Espider.getInstance().getDisplay(), new ImageData("./images/system/remove.png"));

    Image iKicked = new Image(Espider.getInstance().getDisplay(), new ImageData("./images/system/banned.png"));

    CTabFolder tabFolder = null;

    public static NewsTab getInstance(CTabFolder tabFolder) {
        if (instance == null) instance = new NewsTab(tabFolder);
        return instance;
    }

    public static NewsTab getInstance() {
        return instance;
    }

    public NewsTab(CTabFolder tabFolder) {
        super(tabFolder, SWT.NONE);
        this.tabFolder = tabFolder;
        createContents(tabFolder);
    }

    private void createContents(CTabFolder tabFolder) {
        setToolTipText("Network news");
        setText(".: News :.");
        Composite globalComposite = new Composite(tabFolder, SWT.NONE);
        globalComposite.setLayout(new FormLayout());
        setControl(globalComposite);
        Button addNews = new Button(globalComposite, SWT.NONE);
        addNews.setImage(iAdd);
        addNews.setToolTipText("Add a release");
        FormData data = new FormData();
        data.right = new FormAttachment(100, -5);
        data.bottom = new FormAttachment(100, -5);
        addNews.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                new AddNews(new Shell(Espider.getInstance().getDisplay()));
            }
        });
        addNews.setLayoutData(data);
        Button refreshNews = new Button(globalComposite, SWT.NONE);
        refreshNews.setImage(iRefresh);
        refreshNews.setToolTipText("Refresh");
        data = new FormData();
        data.right = new FormAttachment(addNews, -5, SWT.LEFT);
        data.top = new FormAttachment(addNews, 0, SWT.TOP);
        data.bottom = new FormAttachment(100, -5);
        refreshNews.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                displayNews();
            }
        });
        refreshNews.setLayoutData(data);
        contentPane = new ScrolledComposite(globalComposite, SWT.V_SCROLL | SWT.BORDER);
        data = new FormData();
        data.top = new FormAttachment(0, 0);
        data.left = new FormAttachment(0, 0);
        data.right = new FormAttachment(100, 0);
        data.bottom = new FormAttachment(addNews, -10, SWT.TOP);
        contentPane.setLayoutData(data);
        contentPane.setExpandHorizontal(true);
        displayNews();
    }

    public void displayNews() {
        try {
            Label newsType, title, body, info, noNewsImage, noNews;
            Composite news = null, lastNews = null;
            Button remove = null, banned = null;
            Composite main = new Composite(contentPane, SWT.NONE);
            main.setLayout(new FormLayout());
            contentPane.setContent(main);
            ArrayList<SpiderNews> newsList = SpiderNews.getAllNews();
            if (newsList.size() == 0) {
                news = new Composite(main, SWT.NONE);
                FormData formDataNews = new FormData();
                formDataNews.left = new FormAttachment(0, 0);
                formDataNews.right = new FormAttachment(100, 0);
                formDataNews.top = new FormAttachment(0, 0);
                news.setLayoutData(formDataNews);
                news.setLayout(new FormLayout());
                noNewsImage = new Label(news, SWT.CENTER);
                noNewsImage.setImage(iNoNews);
                FormData data = new FormData();
                data.top = new FormAttachment(0, 0);
                data.left = new FormAttachment(0, 5);
                data.right = new FormAttachment(100, -5);
                noNewsImage.setLayoutData(data);
                noNews = new Label(news, SWT.CENTER);
                noNews.setText("No news yet");
                data = new FormData();
                data.top = new FormAttachment(noNewsImage, 10, SWT.BOTTOM);
                data.left = new FormAttachment(0, 5);
                data.right = new FormAttachment(100, -5);
                noNews.setLayoutData(data);
                news.pack();
            } else for (final SpiderNews spiderNews : newsList) {
                news = new Composite(main, SWT.NONE);
                FormData formDataNews = new FormData();
                formDataNews.left = new FormAttachment(0, 0);
                formDataNews.right = new FormAttachment(100, 0);
                if (lastNews == null) formDataNews.top = new FormAttachment(0, 0); else formDataNews.top = new FormAttachment(lastNews, 5);
                news.setLayoutData(formDataNews);
                news.setLayout(new FormLayout());
                newsType = new Label(news, SWT.LEFT);
                String type = spiderNews.getType();
                if (type.equals("Video")) {
                    newsType.setImage(iMovie);
                    newsType.setToolTipText("Video file");
                } else if (type.equals("Audio")) {
                    newsType.setImage(iMusic);
                    newsType.setToolTipText("Audio file");
                } else if (type.equals("Image")) {
                    newsType.setImage(iImage);
                    newsType.setToolTipText("Image file");
                } else if (type.equals("Compress")) {
                    newsType.setImage(iCompress);
                    newsType.setToolTipText("Archive file");
                } else {
                    newsType.setImage(iUnknow);
                    newsType.setToolTipText("Unknow format");
                }
                FormData data = new FormData();
                newsType.setLayoutData(data);
                title = new Label(news, SWT.CENTER);
                title.setFont(new Font(this.getDisplay(), "Arial", 10, SWT.BOLD));
                title.setText(spiderNews.getTitle());
                data = new FormData();
                data.top = new FormAttachment(newsType, 0, SWT.TOP);
                data.left = new FormAttachment(newsType, 5);
                title.setLayoutData(data);
                remove = new Button(news, SWT.NONE);
                remove.setImage(iRemove);
                remove.setToolTipText("Delete this news");
                data = new FormData();
                data.top = new FormAttachment(newsType, 0, SWT.TOP);
                data.right = new FormAttachment(100, -5);
                remove.setLayoutData(data);
                remove.addSelectionListener(new SelectionAdapter() {

                    public void widgetSelected(SelectionEvent e) {
                        try {
                            spiderNews.delete();
                            displayNews();
                        } catch (SQLException sqle) {
                            System.out.println("Impossible de supprimer la news... \n" + sqle.getLocalizedMessage());
                        }
                    }
                });
                body = new Label(news, SWT.LEFT | SWT.BOLD | SWT.WRAP);
                body.setFont(new Font(this.getDisplay(), "Arial", 8, SWT.ITALIC));
                body.setText(spiderNews.getBody());
                data = new FormData();
                data.top = new FormAttachment(newsType, 10, SWT.BOTTOM);
                data.left = new FormAttachment(0, 5);
                data.right = new FormAttachment(100, -5);
                body.setLayoutData(data);
                info = new Label(news, SWT.RIGHT | SWT.BOLD | SWT.WRAP);
                info.setFont(new Font(this.getDisplay(), "Arial", 8, SWT.NORMAL));
                info.setText("posted on " + spiderNews.getDate() + " by " + spiderNews.getAuthor() + "   ");
                info.setBackground(new Color(Espider.getInstance().getDisplay(), 182, 207, 132));
                data = new FormData();
                data.top = new FormAttachment(body, 10, SWT.BOTTOM);
                data.right = new FormAttachment(100, -5);
                data.left = new FormAttachment(0, 5);
                info.setLayoutData(data);
                Label emptyLabel = new Label(news, SWT.RIGHT);
                emptyLabel.setText("   ");
                data = new FormData();
                data.top = new FormAttachment(info, 5, SWT.BOTTOM);
                data.right = new FormAttachment(100, -5);
                data.left = new FormAttachment(0, 5);
                emptyLabel.setLayoutData(data);
                news.pack();
                lastNews = news;
            }
            main.setSize(main.computeSize(SWT.DEFAULT, SWT.DEFAULT));
            main.pack();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
