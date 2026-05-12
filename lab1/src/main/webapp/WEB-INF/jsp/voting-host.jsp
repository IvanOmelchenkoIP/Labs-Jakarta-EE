<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8" %> <%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="uk">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <title>Керування голосуваннями</title>
    <link
      rel="stylesheet"
      href="${pageContext.request.contextPath}/styles.css"
    />
  </head>
  <body>
    <div class="container">
      <nav class="voting-nav" aria-label="Навігація модуля">
        <a class="btn btn-secondary" href="${pageContext.request.contextPath}/"
          >&larr; До головної</a
        >
        <a
          class="btn btn-secondary"
          href="${pageContext.request.contextPath}/voting"
          >Список голосувань</a
        >
      </nav>

      <h1>Керування голосуваннями</h1>
      <p class="voting-detail-meta">
        Навчальна заглушка без входу в систему: змінювати статус можна лише для
        записів з ownerId =
        <c:out value="${demoOwnerId}" />. У подальших лабораторних це заміниться
        автентифікацією та перевіркою прав (лаб. 3–4).
      </p>

      <c:choose>
        <c:when test="${param.notice == 'created'}">
          <div class="flash flash-ok" role="status">
            Створено нове голосування.
          </div>
        </c:when>
        <c:when test="${param.notice == 'started'}">
          <div class="flash flash-ok" role="status">
            Голосування запущено (ACTIVE).
          </div>
        </c:when>
        <c:when test="${param.notice == 'stopped'}">
          <div class="flash flash-warn" role="status">
            Голосування зупинено (CLOSED).
          </div>
        </c:when>
        <c:when test="${param.notice == 'createbad'}">
          <div class="flash flash-err" role="alert">
            Потрібні назва та принаймні два претенденти.
          </div>
        </c:when>
        <c:when test="${param.notice == 'forbidden'}">
          <div class="flash flash-err" role="alert">
            Немає прав на це голосування (ідентифікатор організатора не
            збігається).
          </div>
        </c:when>
      </c:choose>

      <h2>Існуючі голосування</h2>
      <table class="voting-table">
        <thead>
          <tr>
            <th>ID</th>
            <th>Назва</th>
            <th>Статус</th>
            <th>Owner</th>
            <th>Дії</th>
          </tr>
        </thead>
        <tbody>
          <c:forEach var="v" items="${votings}">
            <tr>
              <td><c:out value="${v.id}" /></td>
              <td><c:out value="${v.title}" escapeXml="true" /></td>
              <td>
                <c:choose>
                  <c:when test="${v.active}"
                    ><span class="badge badge-active">ACTIVE</span></c:when
                  >
                  <c:when test="${v.draft}"
                    ><span class="badge badge-draft">DRAFT</span></c:when
                  >
                  <c:otherwise
                    ><span class="badge badge-closed">CLOSED</span></c:otherwise
                  >
                </c:choose>
              </td>
              <td><c:out value="${v.ownerId}" /></td>
              <td>
                <div class="host-actions">
                  <a
                    class="btn btn-secondary btn-sm"
                    href="${pageContext.request.contextPath}/voting/detail?id=${v.id}"
                    >Переглянути</a
                  >
                  <button
                    type="button"
                    class="btn btn-secondary btn-sm js-copy-voting-url"
                    data-path="${pageContext.request.contextPath}/voting/detail?id=${v.id}"
                    aria-label="Копіювати посилання на це голосування"
                  >
                    Копіювати посилання
                  </button>
                  <c:if test="${v.ownerId == demoOwnerId}">
                    <c:if test="${v.draft}">
                      <form
                        method="post"
                        action="${pageContext.request.contextPath}/voting/host"
                      >
                        <input type="hidden" name="action" value="start" />
                        <input type="hidden" name="votingId" value="${v.id}" />
                        <button type="submit" class="btn btn-success btn-sm">
                          Запустити
                        </button>
                      </form>
                    </c:if>
                    <c:if test="${v.active}">
                      <form
                        method="post"
                        action="${pageContext.request.contextPath}/voting/host"
                      >
                        <input type="hidden" name="action" value="stop" />
                        <input type="hidden" name="votingId" value="${v.id}" />
                        <button type="submit" class="btn btn-danger btn-sm">
                          Зупинити
                        </button>
                      </form>
                    </c:if>
                  </c:if>
                  <c:if test="${v.ownerId != demoOwnerId}">
                    <span class="voting-detail-meta" style="margin: 0"
                      >Не ваш запис</span
                    >
                  </c:if>
                </div>
              </td>
            </tr>
          </c:forEach>
        </tbody>
      </table>

      <section class="form-create">
        <h2>Створити нове голосування</h2>
        <p class="voting-detail-meta">
          Претендентів перерахуйте через кому або з нового рядка (мінімум 2).
        </p>
        <form
          method="post"
          action="${pageContext.request.contextPath}/voting/host"
        >
          <input type="hidden" name="action" value="create" />
          <div>
            <label for="title">Назва</label><br />
            <input
              id="title"
              type="text"
              name="title"
              required
              maxlength="200"
            />
          </div>
          <div style="margin-top: 12px">
            <label for="description">Опис</label><br />
            <textarea
              id="description"
              name="description"
              rows="3"
              maxlength="2000"
            ></textarea>
          </div>
          <div style="margin-top: 12px">
            <label for="candidates">Претенденти</label><br />
            <textarea
              id="candidates"
              name="candidates"
              rows="4"
              required
              placeholder="Іван; Петро; Марія"
            ></textarea>
          </div>
          <button type="submit" class="btn btn-primary btn-submit">
            Створити
          </button>
        </form>
      </section>

      <p class="site-footer" style="margin-top: 48px">
        Публічне посилання для учасників: скопіюйте повний URL кнопкою
        «Копіювати посилання» в таблиці (потрібен запущений сервер і той самий
        хост/порт).
      </p>
    </div>
    <script>
      (function () {
        var label = "Копіювати посилання";
        document
          .querySelectorAll(".js-copy-voting-url")
          .forEach(function (btn) {
            btn.addEventListener("click", function () {
              var path = btn.getAttribute("data-path");
              if (!path) return;
              var full = new URL(path, window.location.origin).href;
              function restoreOk() {
                btn.textContent = "Скопійовано!";
                setTimeout(function () {
                  btn.textContent = label;
                }, 2000);
              }
              function restoreFail() {
                btn.textContent = "Не вдалося";
                setTimeout(function () {
                  btn.textContent = label;
                }, 2000);
              }
              if (navigator.clipboard && navigator.clipboard.writeText) {
                navigator.clipboard
                  .writeText(full)
                  .then(restoreOk)
                  .catch(restoreFail);
              } else {
                var ta = document.createElement("textarea");
                ta.value = full;
                ta.setAttribute("readonly", "");
                ta.style.position = "fixed";
                ta.style.left = "-9999px";
                document.body.appendChild(ta);
                ta.select();
                try {
                  if (document.execCommand("copy")) {
                    restoreOk();
                  } else {
                    restoreFail();
                  }
                } catch (e) {
                  restoreFail();
                }
                document.body.removeChild(ta);
              }
            });
          });
      })();
    </script>
  </body>
</html>
