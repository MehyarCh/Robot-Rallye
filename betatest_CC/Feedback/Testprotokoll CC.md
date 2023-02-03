# Installation und Ausführbarkeit
* Die Installation und Ausführbarkeit hat bei Mac Usern gar nicht funktioniert

* Bei Windows Laptops lief es problemlos

* Die Read Me ist sehr gut und ausführlich geschrieben, sodass man alles gut starten kann

* Man kann dem Spiel nicht mehr beitreten, wenn es schon gestartet hat

* Die Mindestanzahl an Spieler zu bestimmen ist von Vorteil zu haben.
# Ablauf des Spiels
## Korrektheit

* Dizzy Highway funktioniert nicht

* Man kann die Aktivierung der Elemente nicht gut sehen. Die Zeit in der sich jede Bewegung stattfindet ist unangenehm für den Nutzer, denn es bewegt sich viel zu schnell.

* Nachdem man von der Map rausgekickt wird, wird man geragt in welche Richtung man rebooten will. Leider wird die Richtung beim anderen Client falsch  geupdated.

* Wenn man auf einen Roboter oder Map einmal clickt, kann man die Auswahl nicht mehr ändern.
## Zuverlässigkeit

* Es gibt noch einige Fehlermeldungen, wo das Spiel crashed.

* Der Chat ist nicht zuverlässig, und crashed noch in vielen Fällen.

* Manchmal verschwindet einfach der Client.

* Probleme manchmal bei der Map Auswahl.

* Wenn man die Meinung ändert, ob man KI's will oder nicht funktioniert das Programm nicht mehr.

* ie Handkarten werden strategisch immer richtig angezeigt, zum Beispiel kein "Again" möglich im ersten Register, keine Karte wo man dann durch eine Wand muss. Außer im Fall wo der Timer
abläuft und man random Karten bekommt, kann die Again Karte ins erste Register gelegt werden.

## Benutzerfreundlichkeit

* Es wird alles gründlich erklärt mit vielen Info Buttons und extra Erklärungen, was sehr angenehm für den User ist.

* Es gibt eine Option für Dark Mode, was sehr benutzerfreundlich wäre, leider funktioniert sie noch nicht.

* Es gibt kein Ready Button nachdem man einen Roboter auswählt, was den User verwirren könnte.

* Die Starting Point Auswahl ist verwirrend, Starting Point wird über die Koordinaten gewählt. (Mausklick auf das Feld selbst wäre besser.)

* Fehlendes Feedback bei Buttonclicks und Buttonhovers (kleiner Effekt)

* Viele unnötige Fenster und Popups, bei vielen kleinen Aktionen die man auf das selbe Fenster bringen könnte (z.B. Timer, evtl. Shop)

* Wenn ein Spieler dran ist, steht bei ihm "It's your turn", was gut ist.
## Fehlermeldung
*Bei Fehlermeldungen crashed das ganze Spiel und nichts funktioniert.

# Sonstiges

* Die Option mit der Menu mit Musik, Dark Mode und Info ist sehr cool, hoffentlich werden diese Optionen noch funktionieren.

* Es gibt ein Undo button, dieser sollte ausgegraut werden, wenn er gerade keine Funktion hat.

* Wenn 2 KI's gegeneinander spielen, suchen sie immer Dizzy Highway aus, was nicht funktioniert.

* Die UI beim Server und Client kombinieren wäre gut, da man sonst sehr viele Fenster offen hat beim Spiel, vor allem da sich das Serverfenster nicht schließt nach Start.

# Protokolfragen
## Eröffnungsphase
* Wird die Wahl von Roboter (einzigartig) und Spielernamen (nicht zwangsweise einzigartig) korrekt behandelt?- Ja,Spielernamen kann man auch einzigartig
  auswählen, sogar den gleichen Namen wählen, beides klappt.
  Nachdem man sein Spielernamen auswählt kommt man zur Lobby, wo man dann sein Roboter auswählen kann.
  Leider haben wir bemerkt, dass die Roboter nicht ausgegraut werden und trotzdem „Clickable“ sind, auch wenn sie schon von einem anderen Client ausgewählt wurden.
  Aber nachdem man den gleichen Roboter auswählt, bekommt man eine Fehlermeldung. Wäre besser, wenn man den von Anfang an nicht clicken könnte.

* Ist eine Verbindung zu einem bereits laufenden Spiel unterbunden? Nein, die Möglichkeit besteht nicht.

* Ist Chatten in der Lobby möglich? Ja aber wenn man in der Lobby chatten will, öffnet sich ein extra Pop-up. Aber Zum Beispiel wenn ein Client im Chat ist und etwas schreibt
  und der andere Client, noch nicht drin ist und später reinkommt, crashed dann sein Server, wenn er den Chat betritt und er kann die Nachrichten nicht sehen.

* Funktioniert der Spielstart fehlerfrei? Nein, es gibt noch einige Fehler, wenn zum Beispiel man die Spamkarte als viertes spielt,
  dann kann man die 5. Karte nicht spielen/der Client wird disconnected, man kriegt eine IllegalArgumentException und mann kann für den nächsten Zug keine Karten mehr aussuchen bzw. die Karten werden nicht angezeigt.
  Beim Ablauf des Timers werden diese unsichtbaren Karten dann trotzdem gelegt.

## Normales Spiel
* Wird das Spielfeld richtig aufgebaut und angezeigt? Dizzy Highway hat gar nicht funktioniert. Sonst haben alle anderen Maps funktioniert. Bei Lost Bearings fehlt ein Conveyor Belt ganz
  am Ende. Die Conveyer Belts sehen bei KI's falsch aus.

* Wird der aktuelle Spieler eindeutig angezeigt? Ja.

* Ist es jederzeit offensichtlich, welche Optionen man hat? Ja, es gibt auch viele Information buttons, die man drücken kann, die erklären was man machen muss.

* Ist die Spieloberfläche, ohne unerwartete Nebeneffekte, in der Größe veränderbar? Nein, skalierbar ist nicht möglich.

* Werden Pit und Spielfeldrand korrekt behandelt? Es gab Probleme mit den Checkpoints. Wenn ein Roboter auf einem Checkpoint steht, dann verschwindet der Checkpoint,
  manchmal sogar bis zu dem Ende des Spiels. Bei Twister bewegt sich ja das Checkpoint und dieser dadurch auf einen Roboter bewegt wird, wird der Roboter überdeckt.

* Können sich Roboter gegenseitig und regelkonform verschieben? Nein.

* Werden Feldeffekte richtig behandelt? Nein, da es zum Beispiel Fehler bei Checkpoints gab. Die anderen Felder tun was sie sollen.

* Sind die Upgradekarten korrekt implementiert? Die Karten sind implementiert, jedoch gibt es beim Upgrade Shop Fehler. Gut wäre, wenn man die Karten, die man nicht kaufen kann
  gar nicht im Shop sieht. Außerdem sollte der Shop bei 2 Spielern die Größe 2 betragen, doch nach der ersten Activation Phase ist der Shop schon größer (man hat 3 Karten zur Auswahl).
  Außerdem werden die Karten fehlerhaft ausgegraut/angezeigt, was sehr verwirrend ist.
* Funktionieren Client und Server auch mit anderen Clients und Servern? Nein