package be.howest.ti.monopoly.web;

import be.howest.ti.monopoly.logic.ServiceAdapter;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.Test;


class OpenApiBuyingPropertyTests extends OpenApiTestsBase {

    @Test
    void buyProperty(final VertxTestContext testContext) {
        service.setDelegate(new ServiceAdapter() {
            @Override
            public String buyProperty(String gameId, String playerName, String propertyName) {
                return "";
            }
        });

        post(
                testContext,
                "/games/001/players/Alice/properties/some-property",
                "001-Alice",
                response -> assertOkResponse(response)
        );
    }

    @Test
    void buyPropertyUnauthorized(final VertxTestContext testContext) {
        post(
                testContext,
                "/games/game-id/players/Alice/properties/some-property",
                null,
                response -> assertErrorResponse(response, 401)
        );
    }

    @Test
    void dontBuyProperty(final VertxTestContext testContext) {
        delete(
                testContext,
                "/games/game-id/players/Alice/properties/some-property",
                "some-token",
                response -> assertNotYetImplemented(response, "dontBuyProperty")
        );
    }

    @Test
    void dontBuyPropertyUnauthorized(final VertxTestContext testContext) {
        delete(
                testContext,
                "/games/game-id/players/Alice/properties/some-property",
                null,
                response -> assertErrorResponse(response, 401)
        );
    }
}
