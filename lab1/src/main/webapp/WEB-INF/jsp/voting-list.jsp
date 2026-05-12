<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8" %> <%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="uk">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <title>Голосування — список</title>
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
          href="${pageContext.request.contextPath}/voting/host"
          >Керування голосуваннями</a
        >
      </nav>

      <h1>Онлайн-голосування</h1>
      <p class="voting-detail-meta">
        Контент згенеровано на сервері (JSP + EL + JSTL). База даних не
        використовується: дані тримаються лише в оперативній пам’яті й зникають
        після перезапуску сервера.
      </p>

      <table class="voting-table">
        <thead>
          <tr>
            <th>ID</th>
            <th>Назва</th>
            <th>Статус</th>
            <th></th>
          </tr>
        </thead>
        <tbody>
          <c:forEach var="v" items="${votings}">
            <tr>
              <td><c:out value="${v.id}" /></td>
              <td><c:out value="${v.title}" escapeXml="true" /></td>
              <td>
                <c:choose>
                  <c:when test="${v.active}">
                    <span class="badge badge-active">ACTIVE</span>
                  </c:when>
                  <c:when test="${v.draft}">
                    <span class="badge badge-draft">DRAFT</span>
                  </c:when>
                  <c:otherwise>
                    <span class="badge badge-closed">CLOSED</span>
                  </c:otherwise>
                </c:choose>
              </td>
              <td>
                <a
                  class="btn btn-primary btn-sm"
                  href="${pageContext.request.contextPath}/voting/detail?id=${v.id}"
                  >Відкрити</a
                >
              </td>
            </tr>
          </c:forEach>
        </tbody>
      </table>
    </div>
  </body>
</html>
