package pages.web;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

public class HubPage extends BasePage {

    @FindBy(css = "h1.tm-section-name__text, h1.tm-title")
    private WebElement hubTitle;

    @FindBy(css = "article.tm-articles-list__item")
    private List<WebElement> articles;

    @FindBy(css = "a.tm-title__link")
    private List<WebElement> articleTitles;

    @FindBy(css = ".tm-tabs__tab-link")
    private List<WebElement> sortTabs;

    @FindBy(css = ".tm-hub__info-wrapper")
    private WebElement hubInfo;

    @FindBy(css = ".tm-hub__description")
    private WebElement hubDescription;

    public HubPage(WebDriver driver) {
        super(driver);
    }

    public String getHubTitle() {
        return getText(hubTitle);
    }

    public boolean isPageLoaded() {
        String url = driver.getCurrentUrl();
        return url.contains("/hubs/") && !url.endsWith("/hubs/");
    }

    public boolean hasArticles() {
        return !articles.isEmpty();
    }

    public int getArticlesCount() {
        return articles.size();
    }

    public ArticlePage openFirstArticle() {
        if (!articleTitles.isEmpty()) {
            click(articleTitles.get(0));
            return new ArticlePage(driver);
        }
        throw new RuntimeException("No articles in hub");
    }

    public boolean hasSortTabs() {
        return !sortTabs.isEmpty();
    }

    public void clickSortTab(int index) {
        if (index < sortTabs.size()) {
            click(sortTabs.get(index));
        }
    }

    public String getSortTabText(int index) {
        if (index < sortTabs.size()) {
            return sortTabs.get(index).getText();
        }
        return "";
    }
}
