package pages.web;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

public class HubsPage extends BasePage {

    @FindBy(css = ".tm-hubs-list__category-wrapper")
    private List<WebElement> hubItems;

    @FindBy(css = "a.tm-hub__title")
    private List<WebElement> hubTitles;

    @FindBy(css = "h1.tm-section-name__text")
    private WebElement pageTitle;

    @FindBy(css = ".tm-tabs__tab-link")
    private List<WebElement> filterTabs;

    @FindBy(css = "input.tm-input-text-decorated__input")
    private WebElement searchHubsInput;

    @FindBy(css = ".tm-hub")
    private List<WebElement> hubCards;

    public HubsPage(WebDriver driver) {
        super(driver);
    }

    public boolean isPageLoaded() {
        return driver.getCurrentUrl().contains("/hubs");
    }

    public boolean hasHubs() {
        return !hubCards.isEmpty() || !hubItems.isEmpty();
    }

    public int getHubsCount() {
        return hubCards.isEmpty() ? hubItems.size() : hubCards.size();
    }

    public HubPage openHubByName(String hubName) {
        for (WebElement title : hubTitles) {
            if (title.getText().toLowerCase().contains(hubName.toLowerCase())) {
                click(title);
                return new HubPage(driver);
            }
        }
        throw new RuntimeException("Hub not found: " + hubName);
    }

    public HubPage openFirstHub() {
        if (!hubTitles.isEmpty()) {
            String currentUrl = driver.getCurrentUrl();
            click(hubTitles.get(0));
            // Wait for navigation to complete
            wait.until(d -> !d.getCurrentUrl().equals(currentUrl));
            return new HubPage(driver);
        }
        throw new RuntimeException("No hubs found on page");
    }

    public void searchHub(String query) {
        if (isDisplayed(searchHubsInput)) {
            type(searchHubsInput, query);
        }
    }

    public boolean hasFilterTabs() {
        return !filterTabs.isEmpty();
    }

    public void clickFilterTab(int index) {
        if (index < filterTabs.size()) {
            click(filterTabs.get(index));
        }
    }

    public String getPageTitle() {
        return getText(pageTitle);
    }
}
