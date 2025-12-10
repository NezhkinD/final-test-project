package pages.web;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

public class SearchResultsPage extends BasePage {

    @FindBy(css = "article.tm-articles-list__item")
    private List<WebElement> searchResults;

    @FindBy(css = "a.tm-title__link")
    private List<WebElement> resultTitles;

    @FindBy(css = "input.tm-input-text-decorated__input, input.tm-search__input")
    private WebElement searchInput;

    @FindBy(css = ".tm-empty-placeholder__text, .tm-empty-state")
    private WebElement emptyResultsMessage;

    @FindBy(css = ".tm-pagination__page, .tm-tabs__tab-link")
    private List<WebElement> paginationPages;

    public SearchResultsPage(WebDriver driver) {
        super(driver);
    }

    public boolean hasResults() {
        try {
            wait.until(ExpectedConditions.or(
                    ExpectedConditions.visibilityOfAllElements(searchResults),
                    ExpectedConditions.visibilityOf(emptyResultsMessage)
            ));
            return !searchResults.isEmpty();
        } catch (Exception e) {
            return false;
        }
    }

    public int getResultsCount() {
        return searchResults.size();
    }

    public String getFirstResultTitle() {
        if (!resultTitles.isEmpty()) {
            return getText(resultTitles.get(0));
        }
        return "";
    }

    public ArticlePage openFirstResult() {
        if (!resultTitles.isEmpty()) {
            click(resultTitles.get(0));
            return new ArticlePage(driver);
        }
        throw new RuntimeException("No search results to open");
    }

    public boolean resultContainsText(String text) {
        for (WebElement title : resultTitles) {
            if (title.getText().toLowerCase().contains(text.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    public String getSearchInputValue() {
        return searchInput.getAttribute("value");
    }

    public boolean hasPagination() {
        return !paginationPages.isEmpty();
    }
}
