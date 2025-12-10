package pages.web;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

public class ArticlePage extends BasePage {

    @FindBy(css = "h1")
    private WebElement articleTitle;

    @FindBy(css = "article, .article-formatted-body, .tm-article-body")
    private WebElement articleBody;

    @FindBy(css = "a.tm-user-info__username")
    private WebElement authorLink;

    @FindBy(css = "time[datetime]")
    private WebElement publicationDate;

    @FindBy(css = ".tm-publication-hub__link")
    private List<WebElement> hubLinks;

    @FindBy(css = ".tm-page__main, .article-snippet")
    private WebElement articleHeader;

    @FindBy(css = ".tm-votes-meter__value")
    private WebElement votesCount;

    @FindBy(css = ".bookmarks-button__counter")
    private WebElement bookmarksCount;

    @FindBy(css = ".article-comments-counter-link .value")
    private WebElement commentsCount;

    public ArticlePage(WebDriver driver) {
        super(driver);
    }

    public String getTitle() {
        return getText(articleTitle);
    }

    public boolean isTitleDisplayed() {
        return isDisplayed(articleTitle);
    }

    public boolean isArticleBodyDisplayed() {
        return isDisplayed(articleBody);
    }

    public String getAuthor() {
        return getText(authorLink);
    }

    public boolean hasHubs() {
        return !hubLinks.isEmpty();
    }

    public List<String> getHubNames() {
        return hubLinks.stream()
                .map(WebElement::getText)
                .toList();
    }

    public boolean isArticleHeaderDisplayed() {
        return isDisplayed(articleHeader);
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    public boolean titleContains(String text) {
        return getTitle().toLowerCase().contains(text.toLowerCase());
    }
}
