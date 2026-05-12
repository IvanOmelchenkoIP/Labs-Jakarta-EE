<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8" %> <%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="uk">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <title>
      <c:out value="${voting.title}" escapeXml="true"/> — голосування
    </title>
    <link
      rel="stylesheet"
      href="${pageContext.request.contextPath}/styles.css"
    />
  </head>
  <body>
    <div class="container">
      <nav class="voting-nav" aria-label="Навігація модуля">
        <a
          class="btn btn-secondary"
          href="${pageContext.request.contextPath}/voting"
          >&larr; До списку</a
        >
      </nav>

      <c:choose>
        <c:when test="${param.notice == 'ok'}">
          <div class="flash flash-ok" role="status">
            Голос прийнято. Дякуємо!
          </div>
        </c:when>
        <c:when test="${param.notice == 'already'}">
          <div class="flash flash-warn" role="status">
            У цьому голосуванні ви вже голосували (лише один голос за сесію
            браузера).
          </div>
        </c:when>
        <c:when test="${param.notice == 'inactive'}">
          <div class="flash flash-warn" role="status">
            Голосування неактивне.
          </div>
        </c:when>
        <c:when test="${param.notice == 'badcandidate'}">
          <div class="flash flash-err" role="alert">Некоректний кандидат.</div>
        </c:when>
        <c:when test="${param.notice == 'error'}">
          <div class="flash flash-err" role="alert">
            Не вдалося обробити голос.
          </div>
        </c:when>
      </c:choose>

      <h1><c:out value="${voting.title}" escapeXml="true" /></h1>

      <c:choose>
        <c:when test="${voting.active}">
          <p><span class="badge badge-active">ACTIVE</span></p>
        </c:when>
        <c:when test="${voting.draft}">
          <p><span class="badge badge-draft">DRAFT</span></p>
        </c:when>
        <c:otherwise>
          <p><span class="badge badge-closed">CLOSED</span></p>
        </c:otherwise>
      </c:choose>

      <p class="voting-detail-meta">
        <c:out value="${voting.description}" escapeXml="true" />
      </p>

      <h2>Претенденти та голоси</h2>
      <ul class="candidate-list">
        <c:forEach var="c" items="${voting.candidates}">
          <li>
            <span><c:out value="${c.name}" escapeXml="true" /></span>
            <strong><c:out value="${c.voteCount}" /></strong>
          </li>
        </c:forEach>
      </ul>

      <c:if test="${voting.active && !hasVoted}">
        <h2>Віддати голос</h2>
        <form
          class="vote-form"
          method="post"
          action="${pageContext.request.contextPath}/voting/vote"
        >
          <input type="hidden" name="votingId" value="${voting.id}" />
          <c:forEach var="c" items="${voting.candidates}">
            <label>
              <input type="radio" name="candidateId" value="${c.id}" required />
              <c:out value="${c.name}" escapeXml="true" />
            </label>
          </c:forEach>
          <button type="submit" class="btn btn-primary btn-submit">
            Проголосувати
          </button>
        </form>
      </c:if>

      <c:if test="${hasVoted}">
        <p class="flash flash-ok">
          Ви вже брали участь у цьому голосуванні в цій сесії.
        </p>
      </c:if>

      <c:if test="${!voting.active}">
        <p class="voting-detail-meta">
          Форма голосування доступна лише для голосувань у статусі ACTIVE.
        </p>
      </c:if>
    </div>
  </body>
</html>
