<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!doctype html>
<html lang="uk">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <title>Бригада — Jakarta EE</title>
    <link
      rel="stylesheet"
      href="${pageContext.request.contextPath}/styles.css"
    />
  </head>
  <body>
    <div class="container">
      <main class="landing-section">
        <section class="left-panel">
          <h1>Розробка застосунків на платформі Jakarta EE</h1>
          <p class="lead">
            Лабораторний проєкт команди: тема варіанту — система
            онлайн-голосування.
          </p>
          <p class="lead">
            <a
              class="btn btn-primary"
              href="${pageContext.request.contextPath}/voting"
              >Перейти до голосування</a
            >
          </p>
          <img
            class="framework-img"
            src="${pageContext.request.contextPath}/images/jakarta-logo.png"
            alt="Логотип Jakarta EE"
          />
          <a
            class="btn btn-secondary"
            href="https://jakarta.ee"
            target="_blank"
            rel="noopener"
            >Офіційний сайт Jakarta EE ↗</a
          >
        </section>

        <section class="right-panel">
          <h2>Учасники бригади</h2>
          <p class="panel-sub">
            Оберіть учасника, щоб відкрити сторінку профілю.
          </p>
          <ul class="member-cards">
            <li>
              <a href="${pageContext.request.contextPath}/member-chyrkov">
                <span class="avatar" aria-hidden="true">МЧ</span>
                <span>Чирков Максим</span>
              </a>
            </li>
            <li>
              <a href="${pageContext.request.contextPath}/member-kreslavskyi">
                <span class="avatar" aria-hidden="true">МК</span>
                <span>Креславський Михайло</span>
              </a>
            </li>
            <li>
              <a href="${pageContext.request.contextPath}/member-kushch">
                <span class="avatar" aria-hidden="true">АК</span>
                <span>Кущ Артем</span>
              </a>
            </li>
            <li>
              <a href="${pageContext.request.contextPath}/member-omelchenko">
                <span class="avatar" aria-hidden="true">ІО</span>
                <span>Іван Омельченко</span>
              </a>
            </li>
          </ul>
        </section>
      </main>

      <section class="variant-section">
        <h2>Тема за варіантом: онлайн-голосування</h2>
        <p class="section-intro">
          Короткий опис предметної області для подальших лабораторних робіт.
        </p>

        <div class="variant-grid">
          <div class="variant-card">
            <h3>Сутності</h3>
            <ul>
              <li>Голосування</li>
              <li>Кандидати</li>
              <li>Голоси</li>
            </ul>
          </div>
          <div class="variant-card">
            <h3>Користувач</h3>
            <ul>
              <li>Голосує за кандидата</li>
              <li>Обмеження: один голос на голосування</li>
              <li>Може створювати власні голосування</li>
            </ul>
          </div>
        </div>

        <div class="actors-block">
          <h3>Інші актори та сценарії</h3>
          <p>
            <strong>Ведучий голосування:</strong> запускає й завершує
            голосування, отримує унікальне посилання для роздачі учасникам,
            переглядає підсумки.
          </p>
          <p>
            <strong>Система:</strong> зберігає голоси та забезпечує коректність
            підрахунку.
          </p>
        </div>
      </section>
    </div>
    <footer class="site-footer">
      <p>
        Зображення на сайті можуть походити з джерел на кшталт
        <a href="https://wikipedia.org" target="_blank" rel="noopener"
          >Wikipedia</a
        >
        та
        <a href="https://wikimedia.org" target="_blank" rel="noopener"
          >Wikimedia Commons</a
        >
        (<a href="https://creativecommons.org" target="_blank" rel="noopener"
          >Creative Commons</a
        >).
      </p>
    </footer>
  </body>
</html>
