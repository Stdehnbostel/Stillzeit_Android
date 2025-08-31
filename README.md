todo:

- Screen mit Funktion zur Aufnahme neuer Stillmahlzeiten
1. Etappe Funktionierender Timer für einzelne Stillmahzeit (links/ rechts unterscheidung,
Zusammenfassung zu Mahlzeiten später)
- * Funktion createTimeString generiert Zeitsempel, wie sie für die Datenbank gebraucht werden.

Nächste Schritte:
- * Dauer berechnen
- * Für die Anzeige Zeitstempel der Form HH:mm generieren
- * Farbschema anpassen

- * Speicherung der Zeiten in Sqlite-Datenbank
- * Erstellung der Model-Klassen Feeding, BreastFeedingDetail, BreastFeeding

Benötigte Tables in SQL:
CREATE TABLE IF NOT EXISTS feeding (
                                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                                    uuid TEXT UNIQUE NOT NULL,
                                    volume INTEGER,
                                    type TEXT NOT NULL,
                                    profile_name TEXT,
                                    meal_id INTEGER,
                                    FOREIGN KEY(profile_name) REFERENCES profile(name)
                                    FOREIGN KEY (meal_id) REFERENCES meal(id)
                                );
        
                                CREATE TABLE IF NOT EXISTS breast_feeding (
                                    id INTEGER PRIMARY KEY,
                                    start TEXT,
                                    end TEXT,
                                    active INTEGER,
                                    side TEXT,
                                    FOREIGN KEY(id) REFERENCES feeding(id) ON DELETE CASCADE
                                );

Materialien zu Kotlin und zur Android-App-Entwicklung: https://developer.android.com/courses/android-basics-compose/course?hl=en&utm_source=codelabs.developers.google.com&utm_medium=referral#0

Die iOS-Variente sieht derzeit so aus. Hier müssen zwar auch noch ein paar Details überarbeitet werden.
Aber so in etwa sollte die App einmal aussehen: ![alt text](https://github.com/Stdehnbostel/Stillzeit_Android/blob/main/design/Simulator%20Screenshot%20-%20iPhone%2016e%20-%202025-08-30%20at%2012.48.01.png)
