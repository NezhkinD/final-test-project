# Final Test Project

Проект автоматизированного тестирования веб-сайта Habr.com и мобильного приложения Wikipedia для Android.

## Требования к окружению

### Общие требования
- JDK 21 или выше
- Maven 3.8+
- Git
- Docker 28+

### Для веб-тестов
- Google Chrome (последняя версия)
- ChromeDriver (устанавливается автоматически через WebDriverManager)

### Для мобильных тестов
- Docker 28+
- KVM 1.2+

## Установка и настройка

### Конфигурация

Параметры тестов настраиваются в файле `src/test/resources/config.properties`:

```properties
# Веб-тесты
web.base.url=https://habr.com
web.browser=chrome
web.implicit.wait=10
web.explicit.wait=15

# Мобильные тесты
appium.server.url=http://127.0.0.1:4723
android.platform.name=Android
android.platform.version=14
android.device.name=emulator-5554
android.app.package=org.wikipedia
android.app.activity=org.wikipedia.main.MainActivity
android.automation.name=UiAutomator2
```

## Запуск тестов

### Запуск всех тестов

```bash
make run
```

### Запуск только web-тестов
```bash
make web
```

### Запуск только mobile-тестов
```bash
make mobile
```
Также можно наблюдать за эмулятором через VNC в браузере: http://127.0.0.1:6080

## Структура проекта

```
src/test/
├── java/
│   ├── pages/
│   │   ├── web/              # Page Objects для Habr.com
│   │   │   ├── BasePage.java
│   │   │   ├── MainPage.java
│   │   │   ├── SearchResultsPage.java
│   │   │   ├── ArticlePage.java
│   │   │   ├── HubsPage.java
│   │   │   └── HubPage.java
│   │   └── mobile/           # Page Objects для Wikipedia
│   │       ├── BaseScreen.java
│   │       ├── OnboardingScreen.java
│   │       ├── MainScreen.java
│   │       ├── SearchScreen.java
│   │       ├── ArticleScreen.java
│   │       └── SettingsScreen.java
│   ├── tests/
│   │   ├── web/
│   │   │   └── HabrTests.java
│   │   └── mobile/
│   │       └── WikipediaTests.java
│   └── utils/
│       ├── ConfigReader.java
│       └── DriverManager.java
└── resources/
    ├── config.properties
    ├── testng.xml
    ├── testng-web.xml
    └── testng-mobile.xml
```

## Тестовые сценарии

### Веб-тесты (Habr.com)

| # | Сценарий | Описание |
|---|----------|----------|
| 1 | Поиск статьи | Поиск статьи по ключевому слову и проверка результатов |
| 2 | Навигация по хабам | Переход на страницу хабов и проверка их отображения |
| 3 | Открытие статьи | Открытие статьи с главной страницы и проверка содержимого |
| 4 | Переход в хаб | Переход в конкретный хаб и проверка статей |
| 5 | Элементы страницы | Проверка отображения основных элементов (логотип, навигация) |
| 6 | DataProvider | Параметризованный поиск с разными запросами |

### Мобильные тесты (Wikipedia)

| # | Сценарий | Описание |
|---|----------|----------|
| 1 | Поиск статьи | Поиск статьи по ключевому слову |
| 2 | Открытие статьи | Открытие статьи и проверка заголовка |
| 3 | Прокрутка | Прокрутка статьи и проверка содержимого |
| 4 | Навигация | Навигация по приложению и возврат назад |
| 5 | DataProvider | Параметризованный поиск разных статей |

## Технологии

- **Java 21** - язык программирования
- **Maven** - система сборки
- **Selenium WebDriver 4.27** - автоматизация веб-тестов
- **Appium 9.3** - автоматизация мобильных тестов
- **TestNG 7.10** - тестовый фреймворк
- **WebDriverManager 5.9** - управление драйверами браузеров