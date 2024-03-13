# Game of The Amazons - COSC 322 - Team 11
---
### How it works
The AI that we will program is a subclass of GamePlayer, which will use a Game Client to communicate with the server.

The GamePlayer communicates with the server to:
- Get info about the Game state to display on the GUI
- Communicate its move to the server

The method *handleGameMessage()* is the method in charge of communication with the Client (and therefore to the server).
Another method, *handleOpponentMove()* is what will contain the logic that our game player will use to determine its moves.
