# Monopoly

## Token Scheme
Not implemented

## Bug list
| Bug behaviour  | How to reproduce  | Why it hasn't been fixed    |
|---|---|---|
|A game is too long in the list of joinable games|Make a game for 2 players, join with both but don't choose pawns yet for 1 or both of them|We fixed it by checking if the amount of players already joined matched the numberOfPlayers of the game, but this created a bug because we stopped checking the started value. In the end we didn't have time left to fix this|
