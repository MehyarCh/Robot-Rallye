# Roborally Spielanleitung

## 1. SCHRITT:  .jar-Dateien laden

### Server.jar-Datei laden

Man hat 2 Optionen, um den Server über JAR zu starten.
- Mit `java -jar Server.jar` ist die Mindestanzahl der Spieler 2, bis das Spiel gestartet werden kann.
- Andernfalls kann man die Mindestanzahl wie folgt anpassen: Dafür muss man den Server mit `java -jar Server.jar anzahl` starten. Möchte man bspw. mindestens 3 Spieler haben, dann muss man den Server mit `java -jar Server.jar 3` starten. Die maximale Anzahl an Spielern ist 6. 

### Client.jar-Datei bzw. AI.jar-Datei laden

Den Client startet man via `javar -jar Client.jar` oder einfach mit einem Doppelklick. Wenn man den Client mit der AI starten will kann man das mit `java -jar Client.jar ai`.

## 2. SCHRITT: "connect" zu dem Spiel und das Spielfeld wählen

#### Das "connect" Fenster

Standardmäßig will sich der Client auf `localhost:10080` verbinden. Dies kann man aber in den advanced-settings manuell anpassen.

#### "username" und "choose-robot" Fenster

Weiterhin sollte jeder Spieler einen Namen und einen Robot wählen. Mehrere Spieler können den gleichen Namen haben, aber nicht den gleichen Robot wählen.

#### Lobby Fenster

##### Chat

Private Nachrichten können mit `@UserName` bzw. `@UserID` als Präfix versendet werden.

##### Ready button und Spielfeld wählen

Wenn man das Spiel starten möchte, müssen alle Spieler ready sein. Nachdem alle ready sind, kann der erste Spieler, der den ready-Status gesetzt hat, eine Karte auswählen und diese mit "select" und dann "confirm" bestätigen.

## 3. SCHRITT: Spiel wird geladen

Zunächst wählt jeder Spieler seinen Startpunkt, der mit "START" markiert ist. Das Spiel beginnt, nur wenn alle Spieler ihren Startpunkt gewählt haben. 

### Shop für Upgrade Karten

Der Spieler kann Upgrade Karten vom Shop mit seinen Energiewürfeln kaufen, allerdings nur wenn dieser an der Reihe ist. Der Spieler kann maximal eine Karte kaufen oder auch den Vorgang mit dem "Skip"-Button überspringen.

### Programming-Karten

Die Programming-Karten vom Spieler werden unten in der Mitte dargestellt. Die Register werden von links nach rechts ausgeführt.

## 4. Spiel Endet

Wenn ein Spieler das Spiel beendet hat ist es für alle zu ende und eine Meldung erscheint ob man gewonnen oder verloren hat. 