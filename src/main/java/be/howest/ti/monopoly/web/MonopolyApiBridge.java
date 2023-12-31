package be.howest.ti.monopoly.web;

import be.howest.ti.monopoly.logic.IService;
import be.howest.ti.monopoly.logic.exceptions.IllegalMonopolyActionException;
import be.howest.ti.monopoly.logic.exceptions.InsufficientFundsException;
import be.howest.ti.monopoly.logic.exceptions.MonopolyResourceNotFoundException;
import be.howest.ti.monopoly.logic.implementation.MonopolyService;
import be.howest.ti.monopoly.logic.implementation.tile.Tile;
import be.howest.ti.monopoly.web.exceptions.ForbiddenAccessException;
import be.howest.ti.monopoly.web.exceptions.InvalidRequestException;
import be.howest.ti.monopoly.web.exceptions.NotYetImplementedException;
import be.howest.ti.monopoly.web.tokens.MonopolyUser;
import be.howest.ti.monopoly.web.tokens.PlainTextTokens;
import be.howest.ti.monopoly.web.tokens.TokenManager;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BearerAuthHandler;
import io.vertx.ext.web.handler.CorsHandler;
import io.vertx.ext.web.openapi.RouterBuilder;

import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MonopolyApiBridge {

    private static final Logger LOGGER = Logger.getLogger(MonopolyApiBridge.class.getName());
    public static final String EMPTY = "empty";
    public static final String PROTECTED_ENDPOINT_MESSAGE = "This is a protected endpoint. Make sure the security" +
            "-token you passed along is a valid token for this game and is the token that gives this player access.";
    public static final String PROPERTY = "property";

    private final IService service;
    private final TokenManager tokenManager;

    public MonopolyApiBridge(IService service, TokenManager tokenManager) {
        this.service = service;
        this.tokenManager = tokenManager;
    }

    public MonopolyApiBridge() {
        this(
                new MonopolyService(),
                new PlainTextTokens()
        );
    }


    public Router buildRouter(RouterBuilder routerBuilder) {
        LOGGER.log(Level.INFO, "Installing CORS handlers");
        routerBuilder.rootHandler(createCorsHandler());

        LOGGER.log(Level.INFO, "Installing security handlers");
        routerBuilder.securityHandler("playerAuth", BearerAuthHandler.create(tokenManager));


        LOGGER.log(Level.INFO, "Installing Failure handlers");
        routerBuilder.operations().forEach(op -> op.failureHandler(this::onFailedRequest));

        LOGGER.log(Level.INFO, "Installing Actual handlers");

        // General Game and API Info
        routerBuilder.operation("getInfo").handler(this::getInfo);
        routerBuilder.operation("getTiles").handler(this::getTiles);
        routerBuilder.operation("getTile").handler(this::getTile);
        routerBuilder.operation("getChance").handler(this::getChance);
        routerBuilder.operation("getCommunityChest").handler(this::getCommunityChest);

        // Managing Games
        routerBuilder.operation("getGames").handler(this::getGames);
        routerBuilder.operation("createGame").handler(this::createGame);
        routerBuilder.operation("joinGame").handler(this::joinGame);

        // Game Info
        routerBuilder.operation("getGame").handler(this::getGame);
        routerBuilder.operation("getDummyGame").handler(this::getDummyGame);

        // Turn Management
        routerBuilder.operation("rollDice").handler(this::rollDice);
        routerBuilder.operation("declareBankruptcy").handler(this::declareBankruptcy);

        // Tax Management
        routerBuilder.operation("useEstimateTax").handler(this::useEstimateTax);
        routerBuilder.operation("useComputeTax").handler(this::useComputeTax);

        // Buying property
        routerBuilder.operation("buyProperty").handler(this::buyProperty);
        routerBuilder.operation("dontBuyProperty").handler(this::dontBuyProperty);

        // Improving property
        routerBuilder.operation("buyHouse").handler(this::buyHouse);
        routerBuilder.operation("sellHouse").handler(this::sellHouse);
        routerBuilder.operation("buyHotel").handler(this::buyHotel);
        routerBuilder.operation("sellHotel").handler(this::sellHotel);

        // Mortgage
        routerBuilder.operation("takeMortgage").handler(this::takeMortgage);
        routerBuilder.operation("settleMortgage").handler(this::settleMortgage);

        // Interaction with other player
        routerBuilder.operation("collectDebt").handler(this::collectDebt);
        routerBuilder.operation("trade").handler(this::trade);

        // Prison
        routerBuilder.operation("getOutOfJailFine").handler(this::getOutOfJailFine);
        routerBuilder.operation("getOutOfJailFree").handler(this::getOutOfJailFree);

        // Auctions
        routerBuilder.operation("getBankAuctions").handler(this::getBankAuctions);
        routerBuilder.operation("placeBidOnBankAuction").handler(this::placeBidOnBankAuction);
        routerBuilder.operation("getPlayerAuctions").handler(this::getPlayerAuctions);
        routerBuilder.operation("startPlayerAuction").handler(this::startPlayerAuction);
        routerBuilder.operation("placeBidOnPlayerAuction").handler(this::placeBidOnPlayerAuction);


        LOGGER.log(Level.INFO, "All handlers are installed");
        return routerBuilder.createRouter();
    }

    private void getInfo(RoutingContext ctx) {
        Response.sendJsonResponse(ctx, 200, new JsonObject()
                .put("name", "monopoly")
                .put("version", service.getVersion())
        );
    }

    private void getTiles(RoutingContext ctx) {
        Response.sendJsonResponse(ctx, 200, service.getTiles());
    }

    private void getTile(RoutingContext ctx) {
        Request request = Request.from(ctx);

        Tile tile;
        if (request.hasTilePosition()) {
            int position = request.getTilePosition();
            tile = service.getTile(position);
        } else {
            String name = request.getTileName();
            tile = service.getTile(name);
        }

        Response.sendJsonResponse(ctx, 200, tile);
    }

    private void getChance(RoutingContext ctx) {
        Response.sendJsonResponse(ctx, 200, service.getChance());
    }

    private void getCommunityChest(RoutingContext ctx) {
        Response.sendJsonResponse(ctx, 200, service.getCommunityChest());
    }

    private void createGame(RoutingContext ctx) {
        Request request = Request.from(ctx);

        if (ctx.getBodyAsJson().size() == 0) {
            Response.sendFailure(ctx, 400, "Empty body");
        }

        int numberOfPlayers = request.getNumberOfPlayersOfBody();
        String prefix = request.getPrefixOfBody();
        String gameName = request.getGameNameOfBody();

        if (!gameName.matches("\\w{0,15}")) {
            throw new InvalidRequestException("Game name not of correct format.");
        }

        Response.sendJsonResponse(ctx, 200, service.createGame(numberOfPlayers, prefix, gameName));
    }

    private void getGames(RoutingContext ctx) {
        Request request = Request.from(ctx);
        Integer numberOfPlayers = request.getNumberOfPlayersOfQuery();
        String prefix = request.getPrefixOfQuery();

        Boolean isStarted = null;
        if (request.hasQueryStartedParameter()) {
            isStarted = request.getGameStartedFromQuery();
        }

        Response.sendJsonResponse(ctx, 200, service.getGames(isStarted, numberOfPlayers, prefix));
    }

    private void joinGame(RoutingContext ctx) {
        Request request = Request.from(ctx);

        String playerName = request.getPlayerNameOfBody();
        String pawn = request.getPawnOfBody();
        String gameId = request.getGameId();

        try {
            if (pawn == null) {
                service.joinGame(gameId, playerName);
                String playerToken = tokenManager.createToken(new MonopolyUser(gameId, playerName));
                Response.sendJsonResponse(ctx, 200, new JsonObject().put("token", playerToken));
            } else {
                service.assignPawn(gameId, playerName, pawn);
                Response.sendOkResponse(ctx);
            }

        } catch (IllegalMonopolyActionException exception) {
            Response.sendFailure(ctx, 409, exception.getMessage());
        } catch (MonopolyResourceNotFoundException exception) {
            Response.sendFailure(ctx, 404, exception.getMessage());
        }
    }

    private void getGame(RoutingContext ctx) {
        Request request = Request.from(ctx);

        String gameId = request.getGameId();

        if (!request.isAuthorizedOnlyGame(gameId)) {
            throw new ForbiddenAccessException(PROTECTED_ENDPOINT_MESSAGE);
        }

        try {
            Response.sendJsonResponse(ctx, 200, service.getGame(gameId));
        } catch (MonopolyResourceNotFoundException exception) {
            Response.sendFailure(ctx, 404, exception.getMessage());
        }
    }

    private void getDummyGame(RoutingContext ctx) {
        throw new NotYetImplementedException("getDummyGame");
    }

    private void useEstimateTax(RoutingContext ctx) {
        Request request = Request.from(ctx);

        String gameId = request.getGameId();
        String playerName = request.getPlayerNameOfPath();

        if (!request.isAuthorized(gameId, playerName)) {
            throw new ForbiddenAccessException(PROTECTED_ENDPOINT_MESSAGE);
        }

        service.useEstimateTax(gameId, playerName);
        Response.sendJsonResponse(ctx, 200, new JsonObject().put(EMPTY, EMPTY));
    }

    private void useComputeTax(RoutingContext ctx) {
        Request request = Request.from(ctx);

        String gameId = request.getGameId();
        String playerName = request.getPlayerNameOfPath();

        if (!request.isAuthorized(gameId, playerName)) {
            throw new ForbiddenAccessException(PROTECTED_ENDPOINT_MESSAGE);
        }

        service.useComputeTax(gameId, playerName);
        Response.sendJsonResponse(ctx, 200, new JsonObject().put(EMPTY, EMPTY));
    }

    private void rollDice(RoutingContext ctx) {
        Request request = Request.from(ctx);

        String gameId = request.getGameId();
        String playerName = request.getPlayerNameOfPath();

        if (!request.isAuthorized(gameId, playerName)) {
            throw new ForbiddenAccessException(PROTECTED_ENDPOINT_MESSAGE);
        }

        try {
            Response.sendJsonResponse(ctx, 200, service.rollDice(gameId, playerName));
        } catch (MonopolyResourceNotFoundException exception) {
            Response.sendFailure(ctx, 404, exception.getMessage());
        } catch (IllegalMonopolyActionException exception) {
            Response.sendFailure(ctx, 409, exception.getMessage());
        }
    }

    private void declareBankruptcy(RoutingContext ctx) {
        Request request = Request.from(ctx);

        String gameId = request.getGameId();
        String playerName = request.getPlayerNameOfPath();

        if (!request.isAuthorized(gameId, playerName))
        {
            throw new ForbiddenAccessException(PROTECTED_ENDPOINT_MESSAGE);
        }

        try{
            Response.sendJsonResponse(ctx, 200, service.declareBankruptcy(gameId, playerName));
        } catch (MonopolyResourceNotFoundException exception) {
            Response.sendFailure(ctx, 404, exception.getMessage());
        }
    }

    private void buyProperty(RoutingContext ctx) {
        Request request = Request.from(ctx);

        String gameId = request.getGameId();
        String playerName = request.getPlayerNameOfPath();
        String propertyName = request.getPropertyName();

        if (!request.isAuthorized(gameId, playerName)) {
            throw new ForbiddenAccessException(PROTECTED_ENDPOINT_MESSAGE);
        }
        else {
            try{
                String property = service.buyProperty(gameId, playerName, propertyName);
                Response.sendJsonResponse(ctx, 200, new JsonObject().put(PROPERTY,
                        property).put("purchased", true));
            } catch (IllegalMonopolyActionException exception) {
                Response.sendFailure(ctx, 409, exception.getMessage());
            } catch (MonopolyResourceNotFoundException exception) {
                Response.sendFailure(ctx, 404, exception.getMessage());
            }
        }
    }

    private void dontBuyProperty(RoutingContext ctx) {
        Request request = Request.from(ctx);

        String gameId = request.getGameId();
        String playerName = request.getPlayerNameOfPath();
        String propertyName = request.getPropertyName();

        if (!request.isAuthorized(gameId, playerName)) {
            throw new ForbiddenAccessException(PROTECTED_ENDPOINT_MESSAGE);
        }

        try {
            String property = service.dontBuyProperty(gameId, playerName, propertyName);
            Response.sendJsonResponse(ctx, 200, new JsonObject().put(PROPERTY,
                    property).put("purchased", false));
        } catch (IllegalMonopolyActionException exception) {
            Response.sendFailure(ctx, 409, exception.getMessage());
        } catch (MonopolyResourceNotFoundException exception) {
            Response.sendFailure(ctx, 404, exception.getMessage());
        }
    }

    private void collectDebt(RoutingContext ctx) {
        Request request = Request.from(ctx);

        String gameId = request.getGameId();
        String playerName = request.getPlayerNameOfPath();
        String propertyName = request.getPropertyName();
        String debtorName = request.getDebtorName();

        if (!request.isAuthorized(gameId, playerName)) {
            throw new ForbiddenAccessException(PROTECTED_ENDPOINT_MESSAGE);
        }

        try {
            boolean changeDebt = service.collectDebt(gameId, playerName, propertyName, debtorName);
            Response.sendJsonResponse(ctx, 200, new JsonObject().put("true", changeDebt));
        } catch (IllegalMonopolyActionException exception) {
            Response.sendFailure(ctx, 409, exception.getMessage());
        } catch (MonopolyResourceNotFoundException exception) {
            Response.sendFailure(ctx, 404, exception.getMessage());
        }
    }

    private void takeMortgage(RoutingContext ctx) {
        throw new NotYetImplementedException("takeMortgage");
    }

    private void settleMortgage(RoutingContext ctx) {
        throw new NotYetImplementedException("settleMortgage");
    }

    private void buyHouse(RoutingContext ctx) {
        Request request = Request.from(ctx);

        String gameId = request.getGameId();
        String playerName = request.getPlayerNameOfPath();
        String propertyName = request.getPropertyName();

        if(!request.isAuthorized(gameId, playerName)){
            throw new ForbiddenAccessException(PROTECTED_ENDPOINT_MESSAGE);
        } else {
            try {
                int houseCount = service.buyHouse(gameId, playerName, propertyName);
                Response.sendJsonResponse(ctx, 200, new JsonObject().put(PROPERTY,
                        propertyName).put("houses", houseCount));
            }
            catch (IllegalMonopolyActionException exception) {
                Response.sendFailure(ctx, 409, exception.getMessage());
            }
            catch(MonopolyResourceNotFoundException exception){
                Response.sendFailure(ctx, 404, exception.getMessage());
            }
        }
    }

    private void sellHouse(RoutingContext ctx) {
        Request request = Request.from(ctx);

        String gameId = request.getGameId();
        String playerName = request.getPlayerNameOfPath();
        String propertyName = request.getPropertyName();

        if(!request.isAuthorized(gameId, playerName)){
            throw new ForbiddenAccessException(PROTECTED_ENDPOINT_MESSAGE);
        } else {
            try {
                int houseCount = service.sellHouse(gameId, playerName, propertyName);
                Response.sendJsonResponse(ctx, 200, new JsonObject().put(PROPERTY,
                        propertyName).put("houses", houseCount));
            }
            catch (IllegalMonopolyActionException exception) {
                Response.sendFailure(ctx, 409, exception.getMessage());
            }
            catch(MonopolyResourceNotFoundException exception){
                Response.sendFailure(ctx, 404, exception.getMessage());
            }
        }
    }

    private void buyHotel(RoutingContext ctx) {
        Request request = Request.from(ctx);

        String gameId = request.getGameId();
        String playerName = request.getPlayerNameOfPath();
        String propertyName = request.getPropertyName();

        if(!request.isAuthorized(gameId, playerName)){
            throw new ForbiddenAccessException(PROTECTED_ENDPOINT_MESSAGE);
        } else {
            try {
                int hotelCount = service.buyHotel(gameId, playerName, propertyName);
                Response.sendJsonResponse(ctx, 200, new JsonObject().put(PROPERTY,
                        propertyName).put("hotels", hotelCount));
            }
            catch (IllegalMonopolyActionException exception) {
                Response.sendFailure(ctx, 409, exception.getMessage());
            }
            catch(MonopolyResourceNotFoundException exception){
                Response.sendFailure(ctx, 404, exception.getMessage());
            }
        }
    }

    private void sellHotel(RoutingContext ctx) {
        Request request = Request.from(ctx);

        String gameId = request.getGameId();
        String playerName = request.getPlayerNameOfPath();
        String propertyName = request.getPropertyName();

        if(!request.isAuthorized(gameId, playerName)){
            throw new ForbiddenAccessException(PROTECTED_ENDPOINT_MESSAGE);
        } else {
            try {
                int hotelCount = service.sellHotel(gameId, playerName, propertyName);
                Response.sendJsonResponse(ctx, 200, new JsonObject().put(PROPERTY,
                        propertyName).put("hotels", hotelCount));
            }
            catch (IllegalMonopolyActionException exception) {
                Response.sendFailure(ctx, 409, exception.getMessage());
            }
            catch(MonopolyResourceNotFoundException exception){
                Response.sendFailure(ctx, 404, exception.getMessage());
            }
        }
    }

    private void getOutOfJailFine(RoutingContext ctx) {
        Request request = Request.from(ctx);

        String gameId = request.getGameId();
        String playerName = request.getPlayerNameOfPath();

        if(!request.isAuthorized(gameId, playerName)){
            throw new ForbiddenAccessException(PROTECTED_ENDPOINT_MESSAGE);
        } else {
            try {
                service.getOutOfJailFine(gameId, playerName);
                Response.sendJsonResponse(ctx, 200, new JsonObject().put(EMPTY, EMPTY));
            }
            catch (IllegalMonopolyActionException exception) {
                Response.sendFailure(ctx, 409, exception.getMessage());
            }
            catch(MonopolyResourceNotFoundException exception){
                Response.sendFailure(ctx, 404, exception.getMessage());
            }
        }
    }

    private void getOutOfJailFree(RoutingContext ctx) {
        Request request = Request.from(ctx);

        String gameId = request.getGameId();
        String playerName = request.getPlayerNameOfPath();

        if(!request.isAuthorized(gameId, playerName)){
            throw new ForbiddenAccessException(PROTECTED_ENDPOINT_MESSAGE);
        } else {
            try {
                service.getOutOfJailFree(gameId, playerName);
                Response.sendJsonResponse(ctx, 200, new JsonObject().put(EMPTY, EMPTY));
            }
            catch (IllegalMonopolyActionException exception) {
                Response.sendFailure(ctx, 409, exception.getMessage());
            }
            catch(MonopolyResourceNotFoundException exception){
                Response.sendFailure(ctx, 404, exception.getMessage());
            }
        }
    }

    private void getBankAuctions(RoutingContext ctx) {
        throw new NotYetImplementedException("getBankAuctions");
    }

    private void placeBidOnBankAuction(RoutingContext ctx) {
        throw new NotYetImplementedException("placeBidOnBankAuction");
    }

    private void getPlayerAuctions(RoutingContext ctx) {
        throw new NotYetImplementedException("getPlayerAuctions");
    }

    private void startPlayerAuction(RoutingContext ctx) {
        throw new NotYetImplementedException("startPlayerAuction");
    }

    private void placeBidOnPlayerAuction(RoutingContext ctx) {
        throw new NotYetImplementedException("placeBidOnPlayerAuction");
    }

    private void trade(RoutingContext ctx) {
        throw new NotYetImplementedException("trade");
    }

    private void onFailedRequest(RoutingContext ctx) {
        Throwable cause = ctx.failure();
        int code = ctx.statusCode();
        String quote = Objects.isNull(cause) ? "" + code : cause.getMessage();

        // Map custom runtime exceptions to a HTTP status code.
        LOGGER.log(Level.INFO, "Failed request", cause);
        if (cause instanceof InvalidRequestException) {
            code = 400;
        } else if (cause instanceof IllegalArgumentException) {
            code = 400;
        } else if (cause instanceof InsufficientFundsException) {
            code = 402;
        } else if (cause instanceof ForbiddenAccessException) {
            code = 403;
        } else if (cause instanceof MonopolyResourceNotFoundException) {
            code = 404;
        } else if (cause instanceof IllegalMonopolyActionException) {
            code = 409;
        } else if (cause instanceof NotYetImplementedException) {
            code = 501;
        } else {
            LOGGER.log(Level.WARNING, "Failed request", cause);
        }

        Response.sendFailure(ctx, code, quote);
    }

    private CorsHandler createCorsHandler() {
        return CorsHandler.create(".*.")
                .allowedHeader("x-requested-with")
                .allowedHeader("Access-Control-Allow-Origin")
                .allowedHeader("origin")
                .allowedHeader("Content-Type")
                .allowedHeader("accept")
                .allowedMethod(HttpMethod.GET)
                .allowedMethod(HttpMethod.POST)
                .allowedMethod(HttpMethod.OPTIONS)
                .allowedMethod(HttpMethod.PATCH)
                .allowedMethod(HttpMethod.DELETE)
                .allowedMethod(HttpMethod.PUT);
    }
}
