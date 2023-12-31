package be.howest.ti.monopoly.web;

import be.howest.ti.monopoly.web.tokens.MonopolyUser;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.validation.RequestParameter;
import io.vertx.ext.web.validation.RequestParameters;
import io.vertx.ext.web.validation.ValidationHandler;

import java.util.Objects;


/**
 * The Request class is responsible for translating information that is part of the
 * request into Java.
 * <p>
 * For every piece of information that you need from the request, you should provide a method here.
 * You can find information in:
 * - the request path: params.pathParameter("some-param-name")
 * - the query-string: params.queryParameter("some-param-name")
 * Both return a `RequestParameter`, which can contain a string or an integer in our case.
 * The actual data can be retrieved using `getInteger()` or `getString()`, respectively.
 * You can check if it is an integer (or not) using `isNumber()`.
 * <p>
 * Finally, some requests have a body. If present, the body will always be in the json format.
 * You can acces this body using: `params.body().getJsonObject()`.
 * <p>
 * **TIP:** Make sure that al your methods have a unique name. For instance, there is a request
 * that consists of more than one "player name". You cannot use the method `getPlayerName()` for both,
 * you will need a second one with a different name.
 */
public class Request {

    private final RoutingContext ctx;
    private final RequestParameters params;
    private final MonopolyUser user;

    private Request(RoutingContext ctx) {
        this.ctx = ctx;
        this.params = ctx.get(ValidationHandler.REQUEST_CONTEXT_KEY);
        this.user = (MonopolyUser) ctx.user();
    }

    public static Request from(RoutingContext ctx) {
        return new Request(ctx);
    }

    public RoutingContext getRoutingContext() {
        return ctx;
    }

    public RequestParameters getRequestParameters() {
        return params;
    }

    public boolean isAuthorized(String expectedGameId, String expectedPlayerName) {
        return Objects.equals(expectedGameId, user.getGameId()) &&
                Objects.equals(expectedPlayerName, user.getPlayerName());
    }

    public boolean isAuthorizedOnlyGame(String expectedGameId) {
        return Objects.equals(expectedGameId, user.getGameId());
    }

    public int getTilePosition() {
        return getTileIdFromPath().getInteger();
    }

    public boolean hasTilePosition() {
        return getTileIdFromPath().isNumber();
    }

    public String getTileName() {
        return getTileIdFromPath().getString();
    }

    private RequestParameter getTileIdFromPath() {
        return getIdFromPath("tileId");
    }

    public int getNumberOfPlayersOfBody() {
        return params.body().getJsonObject().getInteger("numberOfPlayers");
    }

    public Integer getNumberOfPlayersOfQuery(){
        RequestParameter parameter = getIdFromQuery("numberOfPlayers");
        if (parameter == null) {
            return null;
        }
        return parameter.getInteger();
    }

    public String getPrefixOfBody() {
        return params.body().getJsonObject().getString("prefix");
    }

    public String getPrefixOfQuery(){
        RequestParameter parameter = getIdFromQuery("prefix");
        if (parameter == null) {
            return null;
        }
        return parameter.getString();

    }

    public String getPlayerNameOfBody() {
        return params.body().getJsonObject().getString("playerName");
    }

    public String getPawnOfBody() {
        return params.body().getJsonObject().getString("pawn");
    }

    public String getGameNameOfBody() {
        return params.body().getJsonObject().getString("gameName");
    }

    public String getPlayerNameOfPath() {
        return getIdFromPath("playerName").getString();
    }

    public String getGameId() {
        return getIdFromPath("gameId").getString();
    }

    public boolean hasQueryStartedParameter() {
        RequestParameter parameter = getIdFromQuery("started");
        return parameter != null;
    }

    public Boolean getGameStartedFromQuery() {
        RequestParameter parameter = getIdFromQuery("started");
        return parameter.getBoolean();
    }

    public String getPropertyName() {
        return getIdFromPath("propertyName").getString();
    }

    public String getDebtorName() {
        return getIdFromPath("debtorName").getString();
    }

    private RequestParameter getIdFromPath(String id) {
        return params.pathParameter(id);
    }

    private RequestParameter getIdFromQuery(String id) {
        return params.queryParameter(id);
    }
}
