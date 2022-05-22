# Monopoly

## Token Scheme
Not implemented

## Bug list
| Bug behaviour  | How to reproduce  | Why it hasn't been fixed    |
|---|---|---|
|A game is too long in the list of joinable games.|Make a game for 2 players, join with both but don't choose pawns yet for 1 or both of them.|We fixed it by checking if the amount of players already joined matched the number of players of the game, but this created a bug because we stopped checking the started value. In the end we didn't have time left to fix this.|
|The game name is not automatically checked for length or illegal characters.|This bug cannot be reproduced.|To make it check automatically we would have to change the API specs. We didn't have the time for that anymore. We checked these things manually in the MonopolyAPIBridge.|
|In the assignPawn method, authorization is not checked with a token.|This bug cannot be reproduced.|To enable authorization we had to change the API spec. We didn't have enough time left to apply this.|