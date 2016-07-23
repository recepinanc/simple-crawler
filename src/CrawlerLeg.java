import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by recepinanc on 23/07/16.
 */
public class CrawlerLeg {

    private static final CharSequence VALIDATOR = "tofilteryourimages";
    private List<String> pagesToVisitLinks = new LinkedList<String>();
    private Document htmlDocument;

    // We'll use a fake USER_AGENT so the web server thinks the robot is a normal web browser.
    private static final String USER_AGENT =
            "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.1 (KHTML, like Gecko) Chrome/13.0.782.112 Safari/535.1";

    /**
     * Make HTTP Request to the given url
     *
     * @param url the url to be visited
     */
    public void crawl(String url) {
        Connection connection = Jsoup.connect(url).userAgent(USER_AGENT);
        try {
            Document document = connection.get();
            htmlDocument = document;

            System.out.println("Received web page at " + url);

            Elements linksOnPage = htmlDocument.select("img[src]");
            System.out.println("Found (" + linksOnPage.size() + ") images");

            PrintWriter writer = new PrintWriter("links.txt", "UTF-8");
            for (Element link : linksOnPage) {
                pagesToVisitLinks.add(link.absUrl("src"));
                if (link.attributes().get("src").contains(VALIDATOR)) {
                    writer.write(link.attributes().get("src") + "\n");
                    System.out.println(String.format("Link : %s", link.attributes().get("src")));
                }
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error in out HTTP request " + e);
        }
    }

    /**
     * Checks whether a specific word is on that page
     *
     * @param word to be checked
     * @return true if the page contains
     */
    public boolean doesContainWord(String word) {
        System.out.println("Searching for the word " + word + "...");
        String bodyText = htmlDocument.body().text();
        return bodyText.toLowerCase().contains(word.toLowerCase());
    }


    /**
     * Retrieve all the links from the page
     *
     * @return a list with filled with links
     */
    public List<String> getLinks() {
        return pagesToVisitLinks;
    }

}
