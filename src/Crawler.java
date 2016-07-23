import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Created by recepinanc on 23/07/16.
 */
public class Crawler {

    private static final int MAX_PAGES_TO_SEARCH = 1; // Only incicaps
    private Set<String> pagesVisited = new HashSet<String>();
    private List<String> pagesToVisit = new LinkedList<String>();


    /**
     * Remove the first (element) url and store it in nextUrl variable
     * now the second element will be the first element since the first is gone
     * and go in the loop if our url is in pagesVisited - meaning that we've visited that
     * After the loop add that url into visited to mark it as visited
     *
     * @return when we exit the loop we'd have a non-visited page's url in next url
     */
    private String nextUrl() {
        String nextUrl;
        do {
            nextUrl = this.pagesToVisit.remove(0);
        } while (this.pagesVisited.contains(nextUrl));
        this.pagesVisited.add(nextUrl);
        return nextUrl;
    }

    public void search(String url, String searchWord) {
        while (pagesVisited.size() < MAX_PAGES_TO_SEARCH) {
            String currentUrl;
            CrawlerLeg leg = new CrawlerLeg();

            if (pagesToVisit.isEmpty()) {
                currentUrl = url;
                pagesVisited.add(currentUrl);
            } else {
                currentUrl = nextUrl();
            }

            leg.crawl(currentUrl);

            boolean isFound = leg.doesContainWord(searchWord);
            if (isFound) {
                System.out.println(String.format("**Success** Word %s found at %s", searchWord, currentUrl));
                break;
            }

            pagesToVisit.addAll(leg.getLinks());
        }
        System.out.println(String.format("**Done** Visited %s web page(s)", this.pagesVisited.size()));
    }
}
