<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8" %> <% final String cp = request.getContextPath(); %>
<!DOCTYPE html>
<html lang="uk">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <title>Креславський Михайло — профіль</title>
    <link rel="stylesheet" href="<%= cp %>/styles.css" />
  </head>
  <body class="profile-page">
    <div class="container">
      <a class="back-link" href="<%= cp %>/">&larr; На головну</a>

      <article class="profile-card">
        <header class="profile-header">
          <h1>Креславський Михайло</h1>
        </header>

        <div class="profile-meta-row">
          <div class="meta-item">
            <span class="meta-label">Група</span>
            <span class="meta-value">ІМ-51мн</span>
          </div>
          <div class="meta-item">
            <span class="meta-label">Контакт</span>
            <div class="contacts-panel">
              <a
                href="https://github.com/kreslavskiy"
                target="_blank"
                rel="noopener"
                title="GitHub — kreslavskiy"
              >
                <img
                  class="contact-icon"
                  src="<%= cp %>/images/github.png"
                  alt="GitHub"
                />
              </a>
            </div>
          </div>
        </div>
      </article>
    </div>
    <footer class="site-footer">
      <p>
        <a href="https://wikipedia.org" target="_blank" rel="noopener"
          >Wikipedia</a
        >
        ·
        <a href="https://creativecommons.org" target="_blank" rel="noopener"
          >Creative Commons</a
        >
      </p>
    </footer>
  </body>
</html>
