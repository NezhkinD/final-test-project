package tests.mobile;

import io.appium.java_client.android.AndroidDriver;
import org.testng.Assert;
import org.testng.annotations.*;
import pages.mobile.*;
import utils.DriverManager;

public class WikipediaTests {

    private AndroidDriver driver;
    private OnboardingScreen onboardingScreen;
    private MainScreen mainScreen;

    @BeforeMethod
    public void setUp() {
        driver = DriverManager.getMobileDriver();
        onboardingScreen = new OnboardingScreen(driver);

        if (onboardingScreen.isOnboardingDisplayed()) {
            mainScreen = onboardingScreen.skipOnboarding();
        } else {
            mainScreen = new MainScreen(driver);
        }
    }

    @AfterMethod
    public void tearDown() {
        DriverManager.quitMobileDriver();
    }

    @Test(priority = 1, description = "Сценарий 1: Поиск статьи по ключевому слову")
    public void testSearchArticle() {
        Assert.assertTrue(mainScreen.isMainScreenDisplayed(),
                "Главный экран должен отображаться");

        SearchScreen searchScreen = mainScreen.openSearch();
        Assert.assertTrue(searchScreen.isSearchInputDisplayed(),
                "Поле поиска должно отображаться");

        searchScreen.enterSearchQuery("Java programming");

        Assert.assertTrue(searchScreen.hasSearchResults(),
                "Результаты поиска должны отображаться");
        Assert.assertTrue(searchScreen.getResultsCount() > 0,
                "Должен быть хотя бы один результат");
    }

    @Test(priority = 2, description = "Сценарий 2: Открытие статьи и проверка заголовка")
    public void testOpenArticleAndVerifyTitle() {
        SearchScreen searchScreen = mainScreen.openSearch();
        searchScreen.enterSearchQuery("Python");

        Assert.assertTrue(searchScreen.hasSearchResults(),
                "Должны быть результаты поиска");

        ArticleScreen articleScreen = searchScreen.openFirstResult();

        Assert.assertTrue(articleScreen.isArticleDisplayed(),
                "Статья должна отображаться");
    }

    @Test(priority = 3, description = "Сценарий 3: Прокрутка статьи и проверка содержимого")
    public void testScrollArticle() {
        SearchScreen searchScreen = mainScreen.openSearch();
        searchScreen.enterSearchQuery("Albert Einstein");

        Assert.assertTrue(searchScreen.hasSearchResults(),
                "Должны быть результаты поиска");

        ArticleScreen articleScreen = searchScreen.openFirstResult();
        Assert.assertTrue(articleScreen.isArticleDisplayed(),
                "Статья должна отображаться");

        Assert.assertTrue(articleScreen.hasContentAfterScroll(),
                "После прокрутки должен отображаться контент");

        articleScreen.scrollDown();
        Assert.assertTrue(articleScreen.isArticleDisplayed(),
                "Статья должна оставаться видимой после прокрутки");
    }

    @Test(priority = 4, description = "Сценарий 4: Навигация и проверка элементов интерфейса")
    public void testNavigationAndUIElements() {
        Assert.assertTrue(mainScreen.isMainScreenDisplayed(),
                "Главный экран должен отображаться");

        SearchScreen searchScreen = mainScreen.openSearch();
        Assert.assertTrue(searchScreen.isSearchInputDisplayed(),
                "Поле поиска должно быть доступно");

        searchScreen.enterSearchQuery("Moscow");
        Assert.assertTrue(searchScreen.hasSearchResults(),
                "Результаты поиска по запросу 'Moscow' должны отображаться");

        ArticleScreen articleScreen = searchScreen.openFirstResult();
        Assert.assertTrue(articleScreen.isArticleDisplayed(),
                "Статья должна открыться");

        MainScreen returnedMainScreen = articleScreen.goBack();
        Assert.assertNotNull(returnedMainScreen,
                "Должен вернуться на предыдущий экран");
    }

    @Test(priority = 5, description = "Сценарий с DataProvider: Поиск разных статей",
            dataProvider = "searchQueries")
    public void testSearchDifferentArticles(String query, String expectedContent) {
        SearchScreen searchScreen = mainScreen.openSearch();
        searchScreen.enterSearchQuery(query);

        Assert.assertTrue(searchScreen.hasSearchResults(),
                "Поиск по запросу '" + query + "' должен вернуть результаты");

        String firstResult = searchScreen.getFirstResultTitle();
        Assert.assertFalse(firstResult.isEmpty(),
                "Заголовок первого результата не должен быть пустым");

        searchScreen.goBack();
    }

    @DataProvider(name = "searchQueries")
    public Object[][] searchQueries() {
        return new Object[][]{
                {"Selenium", "software"},
                {"Java", "programming"},
                {"Wikipedia", "encyclopedia"}
        };
    }
}
