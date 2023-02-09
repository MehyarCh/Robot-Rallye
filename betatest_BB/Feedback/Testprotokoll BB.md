# Installation und Ausführbarkeit
* Die Anleitung zum Starten des Programms ist direkt und klar, der Benutzer wird bis zum Spielstart gut unterstützt.
* Die Möglichkeit AIs nach Starten des Clients hinzuzufügen wäre praktischer als durch Arguments.
* Die Mindestanzahl an Spieler zu bestimmen, und das Port zu ändern ist von Vorteil zu haben.

# Ablauf des Spiels
## Korrektheit
* Die größte Mehrheit der Funktionalitäten ist integriert und läuft reibungslos.
* Die Wahl der Map bzw des Roboters ändern zu können ist von Vorteil.
* @ als Präfix für eine direkte Nachricht ist eine schlaue Idee.
* Man kann die Aktivierung der Elementen gut sehen. Die Zeit in der sich jede Bewegung statfindet ist angenehm für den Nutzer.
*
* Man kann leere Nachrichten schicken.
* Die Upgradephase läuft nicht korrekt.
* Conveyorbelts scheinen nicht korrekt zu funktionieren in diesem Szenario :
  => Map : Dizzy Highway, Spieler wählt (0,6) als StartPoint.\
  => Es werden zwei MoveTwo Karten gespielt, danach zufällige Move Karten.\
  => Nach dem zweiten Register scheinen die Conveyorbelts den Roboter in die falsche Richtung zu schieben (Rückwärts)\
* Wenn man relativ schnell seine Programmingphase endet, muss man trotzdem auf die AI warten. Es ist nicht praktisch, vor allem zum Testen.
* Nachdem ein Spieler den finalen Checkpoint erreicht, wird die Activationphase trotzdem fortgeführt. Was passiert wenn der Gewinner von einem anderen Roboter geschoben wird?

## Zuverlässigkeit
* Das Programm scheint insgesamt robust zu sein. Fehlermeldungen werden im größten Teil behandelt.
* Wenn die eine Spamkarte im 4. Register gespielt wird, stürzt das Programm ab, denn es geht weiter zur Programmingphase obwohl der eine Spieler noch die letzte Karte zu spielen hat.
* Dieser Spieler bekommt dann keine Karten in seine Hand, und wird ausgeloggt.

## Benutzerfreundlichkeit
* Insgesamt hat man eine ziemlich positive Erfahrung beim Spielen.
* Die Map Auswahl bietet eine gute UX.
* Seinen eigenen Roboter auf seinem Fenster zu sehen ist hilfreich, hilft auch beim Testen.
* Die leuchtenden StartPoints sind eine tolle Idee und machen eine zuverlässige Anleitung ohne Text.
* Die Gestaltung von den verschiedenen Fenstern ist attraktiv -insbesondere Roboterauswahl und Chat.
* Spiellogo im Fenster ist sehr cool.
* Dass die Upgradekarten geschoben werden wenn man darauf klickt ist eine schlaue Lösung.
* Der Nutzer hat wertvolle Informationen, die ihn orientieren bzw updaten über den Stand des Spiels.
* Die Skalierbarkeit ist relativ gut, weil alle Elemente des Fensters mit skaliert werden.
*
* Map Elemente sind nicht immer klar. Zb: Pits. Eine Art Beschreibung soll vorhanden sein.
* Man weiß nicht ob man Ready ist, oder ob man auf Ready clicken soll.
* Die Option in die Map reinzuzoomen ist schön zu haben, macht aber keine gute UX, weil sie unpraktisch und ablenkend ist. Außerdem ist die Scrollbar verwirrend.
* Die Karten sind unlesbar, die vom Gegner (Upgrade) unlesbar, und haben manchmal zu viel Text mit einer sehr unangenehmen Schrift.
* Register- bzw Handkarten Container können von einander getrennt werden.
* Die Energybar ist unpraktisch weil Energie im Spiel messbar ist. Man muss wieder hingehen um zu wissen wie viel Energie man hat. Lieber ein messbare Lösung anbieten, zB Kästchen.
* Die Lösung bei der man die (Upgrade)Karten von dem Gegner sieht schafft ihr Ziel gar nicht. Sie ist frustrierend und unattraktiv.
## Fehlermeldung
* Der Nutzer wird benachrichtigt wenn er etwas falsch macht.
* Reboot, und das Brett zu verlassen werden korrekt behandelt.

# Sonstiges
* Push panel schaut in die falsche Richtung bei DEATH TRAP.
* Antenne schaut falsch bei  DEATH TRAP.
* Es macht einen negativen Eindruck die Roboter an der Ecke auf der Map zu sehen, bevor man einen Starting Point aussucht.

# Protokolfragen
## Eröffnungsphase
* Wird die Wahl von Roboter (einzigartig) und Spielernamen (nicht zwangsweise einzigartig) korrekt behandelt?
  -- Ja
* Ist eine Verbindung zu einem bereits laufenden Spiel unterbunden? --Nein, die Möglichkeit besteht nicht.
* Ist Chatten in der Lobby möglich?
  -- Ja
* Funktioniert der Spielstart fehlerfrei? --Ja
## Normales Spiel
* Wird das Spielfeld richtig aufgebaut und angezeigt? --Im größten Teil ja, Push panels zB aber oft nicht.
* Wird der aktuelle Spieler eindeutig angezeigt? -- Ja
* Ist es jederzeit offensichtlich, welche Optionen man hat? --Ja, im Upgradeshop werden aber Karten highlighted die man nicht kaufen darf zB.
* Ist die Spieloberfläche, ohne unerwartete Nebeneffekte, in der Größe veränderbar? --Ja
* Werden Pit und Spielfeldrand korrekt behandelt? --Ja
* Können sich Roboter gegenseitig und regelkonform verschieben? --Ja
* Werden Feldeffekte richtig behandelt? --Ja
* Sind die Upgradekarten korrekt implementiert? --Ja
* Funktionieren Client und Server auch mit anderen Clients und Servern? --Nein, Clients funktionieren nicht weil das Port wahrscheinlich unterschiedlich ist. Aber der Server funktioniert mit anderen Clients.

### Hinweis:
Der Serverlog wurde überschrieben. Der beigefügte ist der Aktuellste (Test mit anderen Server/Client).