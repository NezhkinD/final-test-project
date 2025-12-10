package pages.mobile;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;
import org.openqa.selenium.support.PageFactory;

import java.time.Duration;
import java.util.Collections;
import java.util.List;

public class ArticleScreen extends BaseScreen {

    @AndroidFindBy(id = "org.wikipedia:id/page_web_view")
    private WebElement articleWebView;

    @AndroidFindBy(id = "org.wikipedia:id/page_contents_container")
    private WebElement pageContentsContainer;

    @AndroidFindBy(id = "org.wikipedia:id/page_fragment")
    private WebElement pageFragment;

    @AndroidFindBy(accessibility = "Navigate up")
    private WebElement backButton;

    @AndroidFindBy(id = "org.wikipedia:id/page_toolbar_button_show_overflow_menu")
    private WebElement overflowMenuButton;

    @AndroidFindBy(id = "org.wikipedia:id/page_save")
    private WebElement saveButton;

    @AndroidFindBy(id = "org.wikipedia:id/page_find_in_article")
    private WebElement findInArticleButton;

    @AndroidFindBy(className = "android.view.View")
    private List<WebElement> articleElements;

    @AndroidFindBy(xpath = "//android.view.View[@content-desc]")
    private List<WebElement> contentDescElements;

    public ArticleScreen(AndroidDriver driver) {
        super(driver);
        PageFactory.initElements(new AppiumFieldDecorator(driver, Duration.ofSeconds(10)), this);
    }

    public boolean isArticleDisplayed() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return isDisplayedQuick(articleWebView)
                || isDisplayedQuick(pageContentsContainer)
                || isDisplayedQuick(pageFragment)
                || isDisplayedQuick(backButton);
    }

    public String getArticleTitle() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        for (WebElement element : contentDescElements) {
            String contentDesc = element.getAttribute("content-desc");
            if (contentDesc != null && !contentDesc.isEmpty() && contentDesc.length() > 3) {
                return contentDesc;
            }
        }
        return "";
    }

    public boolean articleContainsText(String text) {
        for (WebElement element : contentDescElements) {
            String contentDesc = element.getAttribute("content-desc");
            if (contentDesc != null && contentDesc.toLowerCase().contains(text.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    public void scrollDown() {
        Dimension size = driver.manage().window().getSize();
        int startX = size.width / 2;
        int startY = (int) (size.height * 0.7);
        int endY = (int) (size.height * 0.3);

        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence scroll = new Sequence(finger, 1);
        scroll.addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), startX, startY));
        scroll.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        scroll.addAction(finger.createPointerMove(Duration.ofMillis(500), PointerInput.Origin.viewport(), startX, endY));
        scroll.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

        driver.perform(Collections.singletonList(scroll));
    }

    public void scrollUp() {
        Dimension size = driver.manage().window().getSize();
        int startX = size.width / 2;
        int startY = (int) (size.height * 0.3);
        int endY = (int) (size.height * 0.7);

        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence scroll = new Sequence(finger, 1);
        scroll.addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), startX, startY));
        scroll.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        scroll.addAction(finger.createPointerMove(Duration.ofMillis(500), PointerInput.Origin.viewport(), startX, endY));
        scroll.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

        driver.perform(Collections.singletonList(scroll));
    }

    public boolean hasContentAfterScroll() {
        scrollDown();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return !articleElements.isEmpty();
    }

    public MainScreen goBack() {
        click(backButton);
        return new MainScreen(driver);
    }

    public void clickSaveButton() {
        click(saveButton);
    }

    public boolean isSaveButtonDisplayed() {
        return isDisplayedQuick(saveButton);
    }
}
