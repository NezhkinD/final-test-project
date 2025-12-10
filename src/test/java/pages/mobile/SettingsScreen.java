package pages.mobile;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import java.time.Duration;
import java.util.List;

public class SettingsScreen extends BaseScreen {

    @AndroidFindBy(id = "org.wikipedia:id/main_drawer_settings_container")
    private WebElement settingsMenuItem;

    @AndroidFindBy(xpath = "//android.widget.TextView[@text='Settings']")
    private WebElement settingsText;

    @AndroidFindBy(xpath = "//android.widget.TextView[@text='Wikipedia languages']")
    private WebElement languagesOption;

    @AndroidFindBy(id = "org.wikipedia:id/wiki_language_title")
    private List<WebElement> languageTitles;

    @AndroidFindBy(id = "org.wikipedia:id/menu_item_add_language")
    private WebElement addLanguageButton;

    @AndroidFindBy(xpath = "//android.widget.TextView[@text='Add language']")
    private WebElement addLanguageText;

    @AndroidFindBy(id = "org.wikipedia:id/preference_languages_filter")
    private WebElement languageSearchInput;

    @AndroidFindBy(id = "org.wikipedia:id/language_subtitle")
    private List<WebElement> languageOptions;

    @AndroidFindBy(accessibility = "Navigate up")
    private WebElement backButton;

    public SettingsScreen(AndroidDriver driver) {
        super(driver);
        PageFactory.initElements(new AppiumFieldDecorator(driver, Duration.ofSeconds(10)), this);
    }

    public void openSettings() {
        if (isDisplayedQuick(settingsMenuItem)) {
            click(settingsMenuItem);
        }
    }

    public boolean isSettingsDisplayed() {
        return isDisplayedQuick(settingsText) || isDisplayedQuick(languagesOption);
    }

    public void openLanguageSettings() {
        if (isDisplayedQuick(languagesOption)) {
            click(languagesOption);
        }
    }

    public boolean hasLanguageOptions() {
        return !languageTitles.isEmpty() || !languageOptions.isEmpty();
    }

    public int getLanguagesCount() {
        return languageTitles.size();
    }

    public String getFirstLanguageTitle() {
        if (!languageTitles.isEmpty()) {
            return getText(languageTitles.get(0));
        }
        return "";
    }

    public void clickAddLanguage() {
        if (isDisplayedQuick(addLanguageButton)) {
            click(addLanguageButton);
        } else if (isDisplayedQuick(addLanguageText)) {
            click(addLanguageText);
        }
    }

    public boolean isAddLanguageDisplayed() {
        return isDisplayedQuick(addLanguageButton) || isDisplayedQuick(addLanguageText);
    }

    public void searchLanguage(String language) {
        if (isDisplayed(languageSearchInput)) {
            type(languageSearchInput, language);
        }
    }

    public void selectLanguageByIndex(int index) {
        if (index < languageOptions.size()) {
            click(languageOptions.get(index));
        }
    }

    public void goBack() {
        click(backButton);
    }
}
