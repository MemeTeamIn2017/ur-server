# Protocol Protocol Specification

This protocol uses JSON as a data transfer means and it inherits
all the restrictions of the JSON standard. It is designed to be
easy to develop for and expand while being adequately fast to parse.

This protocol defines a concept of _PACKET_ as an _COMPLETE_ object being
sent or received. Every packet is a valid JSON object. It MUST contain
a field `{"id" : "PACKET_ID"}` which is the **PACKET_ID** of this packet, other
fields' contents are defined by the **PACKET_ID**. 
## Packets
### Client
##### AUTHENTICATE - "auth"

This is the first packet the server receives. If any other packet is received before this one, the server will automatically ban for 1 hour (subject to change).

Data fields:
* name:String - the name of the player.

Example:
```json
{
  "id": "auth",
  "name": "NoobSlay3r"
}
```


##### CHALLENGE PLAYER - "challenge_player"
Challenges another player in a game of Ur.
The other Player can respond with a Yes/No. The client should wait for this response. (RESPONSE_HERE)
```json
{
  "id": "challenge_player",
  "opponent": "Noob1337",
}
```
### Server
##### LOGIN_SUCCESSFUL - "login_success"
No additional fields.

##### LOGIN_FAILED - "login_failed"
Signals that the server has rejected the Auth request.
This could mean that the name is taken, or the server is under heavy load.
The reason for the fail is given in the `reason` field.

* reason: String - the reason for the Auth fail.

Possible reasons:
* name_taken
* TODO add other reason

Example:
```json
{
  "id":   "login_failed",
  "reason": "name_taken"
}
```
##### CHALLENGE RESPONSE - "challenge_response"
This packet is sent to a client when their opponent accepts or declines the request for a PvP game.
Fields:
* opponent:String - the name of the opponent.
* accepted:Boolean - `true` if the opponent accepted, `false` otherwise.

Example:
```json
{
  "id": "challenge_response",
  "opponent": "Noob1337",
  "accepted": true
}
```


# Authors
* [MemeTeam](mailto://memeteam1997@gmail.com), Memebers:
    * [Bojidar Borislavov Stoyanov (Божидар Бориславов Стоянов)](mailto://glav0r3zzz4@gmail.com)
    * [Kaloyan Ivanov Mitev (Калоян Иванов Митев)](http://todo.com)
    * [Yoanna Ivanova (Йоана Иванова)](http://todo.com)

