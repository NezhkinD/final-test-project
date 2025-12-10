package pages.mobile;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import java.time.Duration;
import java.util.List;

public class SearchScreen extends BaseScreen {

    @AndroidFindBy(id = "org.wikipedia:id/search_src_text")
    private WebElement searchInput;

    @AndroidFindBy(id = "org.wikipedia:id/search_close_btn")
    private WebElement clearSearchButton;

    @AndroidFindBy(id = "org.wikipedia:id/page_list_item_title")
    private List<WebElement> searchResults;

    @AndroidFindBy(id = "org.wikipedia:id/page_list_item_description")
    private List<WebElement> searchResultDescriptions;

    @AndroidFindBy(id = "org.wikipedia:id/search_empty_view")
    private WebElement emptySearchView;

    @AndroidFindBy(className = "android.widget.ImageButton")
    private WebElement backButton;

    public SearchScreen(AndroidDriver driver) {
        super(driver);
        PageFactory.initElements(new AppiumFieldDecorator(driver, Duration.ofSeconds(10)), this);
    }

    public boolean isSearchInputDisplayed() {
        return isDisplayed(searchInput);
    }

    public SearchScreen enterSearchQuery(String query) {
        waitForVisibility(searchInput);
        type(searchInput, query);
        return this;
    }

    public boolean hasSearchResults() {
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return !searchResults.isEmpty();
    }

    public int getResultsCount() {
        return searchResults.size();
    }

    public String getFirstResultTitle() {
        if (!searchResults.isEmpty()) {
            return getText(searchResults.get(0));
        }
        return "";
    }

    public ArticleScreen openFirstResult() {
        if (!searchResults.isEmpty()) {
            click(searchResults.get(0));
            return new ArticleScreen(driver);
        }
        throw new RuntimeException("No search results found");
    }

    public ArticleScreen openResultByIndex(int index) {
        if (index < searchResults.size()) {
            click(searchResults.get(index));
            return new ArticleScreen(driver);
        }
        throw new RuntimeException("Result index out of bounds");
    }

    public boolean resultContainsText(String text) {
        for (WebElement result : searchResults) {
            if (result.getText().toLowerCase().contains(text.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    public void clearSearch() {
        if (isDisplayedQuick(clearSearchButton)) {
            click(clearSearchButton);
        }
    }

    public MainScreen goBack() {
        click(backButton);
        return new MainScreen(driver);
    }
}
