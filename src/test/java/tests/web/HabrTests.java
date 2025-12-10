package tests.web;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.*;
import pages.web.*;
import utils.DriverManager;

public class HabrTests {

    private WebDriver driver;
    private MainPage mainPage;

    @BeforeClass
    public void setUp() {
        driver = DriverManager.getWebDriver();
        mainPage = new MainPage(driver);
    }

    @AfterClass
    public void tearDown() {
        DriverManager.quitWebDriver();
    }

    @BeforeMethod
    public void openMainPage() {
        mainPage.open();
    }

    @Test(description = "Сценарий 1: Поиск статьи по ключевому слову")
    public void testSearchArticle() {
        String searchQuery = "Java";

        SearchResultsPage searchResults = mainPage.searchFor(searchQuery);

        Assert.assertTrue(searchResults.hasResults(),
                "Результаты поиска должны отображаться");
        Assert.assertTrue(searchResults.getResultsCount() > 0,
                "Должен быть хотя бы один результат поиска");
    }

    @Test(description = "Сценарий 2: Навигация по хабам")
    public void testNavigateToHubs() {
        HubsPage hubsPage = mainPage.openHubsPage();

        Assert.assertTrue(hubsPage.isPageLoaded(),
                "Страница хабов должна загрузиться");
        Assert.assertTrue(hubsPage.hasHubs(),
                "На странице должны отображаться хабы");
    }

    @Test(description = "Сценарий 3: Открытие статьи с главной страницы")
    public void testOpenArticle() {
        Assert.assertTrue(mainPage.hasArticles(),
                "На главной странице должны быть статьи");

        ArticlePage articlePage = mainPage.openFirstArticle();

        Assert.assertTrue(articlePage.isTitleDisplayed(),
                "Заголовок статьи должен отображаться");
        Assert.assertTrue(articlePage.isArticleBodyDisplayed(),
                "Тело статьи должно отображаться");
    }

    @Test(description = "Сценарий 4: Переход в хаб и фильтрация статей")
    public void testHubNavigation() {
        HubsPage hubsPage = mainPage.openHubsPage();
        Assert.assertTrue(hubsPage.hasHubs(), "На странице должны быть хабы");

        HubPage hubPage = hubsPage.openFirstHub();

        Assert.assertTrue(hubPage.isPageLoaded(),
                "Страница хаба должна загрузиться");
        Assert.assertTrue(hubPage.hasArticles(),
                "В хабе должны быть статьи");
    }

    @Test(description = "Сценарий 5: Проверка элементов главной страницы")
    public void testMainPageElements() {
        Assert.assertTrue(mainPage.isLogoDisplayed(),
                "Логотип должен отображаться");
        Assert.assertTrue(mainPage.isNavigationDisplayed(),
                "Навигация должна отображаться");
        Assert.assertTrue(mainPage.hasArticles(),
                "Статьи должны отображаться на главной странице");
        Assert.assertTrue(mainPage.getArticlesCount() > 0,
                "Количество статей должно быть больше 0");
    }

    @Test(description = "Сценарий с DataProvider: Поиск с разными запросами",
            dataProvider = "searchQueries")
    public void testSearchWithDifferentQueries(String query) {
        SearchResultsPage searchResults = mainPage.searchFor(query);

        Assert.assertTrue(searchResults.hasResults() || query.length() < 3,
                "Поиск по запросу '" + query + "' должен вернуть результаты");
    }

    @DataProvider(name = "searchQueries")
    public Object[][] searchQueries() {
        return new Object[][]{
                {"Python"},
                {"Selenium"},
                {"Testing"}
        };
    }
}
