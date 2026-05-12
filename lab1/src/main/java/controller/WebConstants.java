package controller;

public final class WebConstants {

    private WebConstants() {
    }

    public static final String ATTR_VOTING_SERVICE = "votingService";

    public static final String SESSION_VOTED_IDS = "votedVotingIds";

    public static final long DEMO_OWNER_ID = 101L;

    public static final String VIEW_LIST = "/WEB-INF/jsp/voting-list.jsp";
    public static final String VIEW_DETAIL = "/WEB-INF/jsp/voting-detail.jsp";
    public static final String VIEW_HOST = "/WEB-INF/jsp/voting-host.jsp";
}
