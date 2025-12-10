package pages.mobile;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import java.time.Duration;

public class OnboardingScreen extends BaseScreen {

    @AndroidFindBy(id = "org.wikipedia:id/fragment_onboarding_skip_button")
    private WebElement skipButton;

    @AndroidFindBy(id = "org.wikipedia:id/fragment_onboarding_forward_button")
    private WebElement continueButton;

    @AndroidFindBy(id = "org.wikipedia:id/primaryTextView")
    private WebElement titleText;

    @AndroidFindBy(id = "org.wikipedia:id/fragment_onboarding_done_button")
    private WebElement doneButton;

    public OnboardingScreen(AndroidDriver driver) {
        super(driver);
        PageFactory.initElements(new AppiumFieldDecorator(driver, Duration.ofSeconds(10)), this);
    }

    public boolean isOnboardingDisplayed() {
        return isDisplayedQuick(skipButton) || isDisplayedQuick(continueButton);
    }

    public MainScreen skipOnboarding() {
        if (isDisplayedQuick(skipButton)) {
            click(skipButton);
        }
        return new MainScreen(driver);
    }

    public void clickContinue() {
        if (isDisplayedQuick(continueButton)) {
            click(continueButton);
        }
    }

    public MainScreen completeOnboarding() {
        while (isDisplayedQuick(continueButton)) {
            click(continueButton);
        }
        if (isDisplayedQuick(doneButton)) {
            click(doneButton);
        }
        return new MainScreen(driver);
    }

    public String getTitleText() {
        return getText(titleText);
    }
}
