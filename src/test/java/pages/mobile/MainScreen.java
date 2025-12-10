package pages.mobile;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import java.time.Duration;
import java.util.List;

public class MainScreen extends BaseScreen {

    @AndroidFindBy(id = "org.wikipedia:id/search_container")
    private WebElement searchContainer;

    @AndroidFindBy(accessibility = "Search Wikipedia")
    private WebElement searchButton;

    @AndroidFindBy(id = "org.wikipedia:id/main_toolbar_wordmark")
    private WebElement toolbarLogo;

    @AndroidFindBy(id = "org.wikipedia:id/nav_more_container")
    private WebElement moreButton;

    @AndroidFindBy(id = "org.wikipedia:id/horizontal_scroll_list_item_text")
    private List<WebElement> feedItems;

    @AndroidFindBy(id = "org.wikipedia:id/view_card_header_title")
    private List<WebElement> cardTitles;

    @AndroidFindBy(id = "org.wikipedia:id/nav_tab_explore")
    private WebElement exploreTab;

    @AndroidFindBy(id = "org.wikipedia:id/nav_tab_reading_lists")
    private WebElement savedTab;

    @AndroidFindBy(id = "org.wikipedia:id/nav_tab_search")
    private WebElement searchTab;

    public MainScreen(AndroidDriver driver) {
        super(driver);
        PageFactory.initElements(new AppiumFieldDecorator(driver, Duration.ofSeconds(10)), this);
    }

    public boolean isMainScreenDisplayed() {
        return isDisplayedQuick(searchContainer) || isDisplayedQuick(searchButton);
    }

    public SearchScreen openSearch() {
        if (isDisplayedQuick(searchContainer)) {
            click(searchContainer);
        } else if (isDisplayedQuick(searchTab)) {
            click(searchTab);
        }
        return new SearchScreen(driver);
    }

    public boolean isLogoDisplayed() {
        return isDisplayed(toolbarLogo);
    }

    public SettingsScreen openSettings() {
        click(moreButton);
        return new SettingsScreen(driver);
    }

    public boolean hasFeedContent() {
        return !cardTitles.isEmpty() || !feedItems.isEmpty();
    }

    public void clickExploreTab() {
        click(exploreTab);
    }

    public void clickSavedTab() {
        click(savedTab);
    }
}
