package pages.web;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import utils.ConfigReader;

import java.util.List;

public class MainPage extends BasePage {

    @FindBy(css = "a.tm-header-user-menu__login")
    private WebElement loginButton;

    @FindBy(css = "a.tm-header-user-menu__search")
    private WebElement searchButton;

    @FindBy(css = "input.tm-input-text-decorated__input")
    private WebElement searchInput;

    @FindBy(css = "a.tm-header__logo")
    private WebElement logo;

    @FindBy(css = "article.tm-articles-list__item, article.tm-feed-publication")
    private List<WebElement> articles;

    @FindBy(css = "article.tm-articles-list__item h2 a, a.tm-title__link")
    private List<WebElement> articleTitles;

    @FindBy(css = "a[href='/ru/hubs/'], a.tm-header__all-flows[href*='hubs']")
    private WebElement hubsLink;

    @FindBy(css = ".tm-header a, .tm-header-user-menu__item")
    private List<WebElement> navigationLinks;

    @FindBy(css = ".tm-section-name__text")
    private WebElement selectedTab;

    public MainPage(WebDriver driver) {
        super(driver);
    }

    public MainPage open() {
        driver.get(ConfigReader.get("web.base.url"));
        return this;
    }

    public boolean isLogoDisplayed() {
        return isDisplayed(logo);
    }

    public void clickSearchButton() {
        click(searchButton);
    }

    public SearchResultsPage searchFor(String query) {
        // Navigate to search page
        driver.get("https://habr.com/ru/search/?q=" + query + "&target_type=posts");
        return new SearchResultsPage(driver);
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
        throw new RuntimeException("No articles found on main page");
    }

    public HubsPage openHubsPage() {
        driver.get("https://habr.com/ru/hubs/");
        return new HubsPage(driver);
    }

    public boolean isNavigationDisplayed() {
        return !navigationLinks.isEmpty();
    }

    public int getNavigationLinksCount() {
        return navigationLinks.size();
    }

    public String getSelectedTabText() {
        return getText(selectedTab);
    }
}
