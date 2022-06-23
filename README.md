# dbComparer
Lo scopo del software è quello di avere un tool per confrontare in modo veloce due database, in particolare con l’obiettivo di verificare eventuali differenze nei risultati di specifiche query select. Esso è stato creato per verificare il rispetto dei principi di normalizzazione del formato SIARD.

Il software, realizzato in java, si avvia da linea di comando ed accetta due parametri: il primo sarà il nome del file di input, che dovrà essere formattato in JSON e conterrà i parametri necessari alla connessione ai database da confrontare, le query da usare per i confronti e un’indicazione di quale sia il database di riferimento, mentre il secondo sarà il nome del file di output, in cui verranno scritti i risultati. Il confronto viene fatto a partire da un database di riferimento, indicato nel file di input, e tutte le differenze trovate negli altri database saranno relative a quest’ultimo. 
# Requisiti:
Parsing File di input:

Il software, aperto il file di input dovrà effettuarne il parsing per generare gli oggetti necessari per la creazione di tutte le connessioni e l’esecuzione delle query in esso specificate.
Connessione ai Database:
Il software deve connettersi correttamente a database di diverse tipologie, in particolare supportare MSSQL, MYSQL, PostgreSQL e Oracle. Per farlo vanno specificati nel file JSON di input i seguenti parametri: 
-	Tipo del database: Stringa che indica il DBMS che ospita il database, per poter caricare il driver JDBC corretto.
-	Host
-	Port
-	Username e password
-	Nome del database
-	Schema: è necessario specificare lo schema solo nel caso di PostgreSQL ed Oracle.

Confronto fra Database:

Il software dovrà quindi riconoscere le differenze nei risultati delle query elencate nel file di input. Tutte le query elencate sono eseguite su tutti i Database descritti nel file. In particolare, vengono rilevati:
-	Diverso numero di colonne: 
-	Diverso nome delle colonne
-	Diverso tipo delle colonne
-	Diverso numero di righe
-	Diverso contenuto nei record, specificando in particolare numero di riga e nome della colonna in cui avviene l’incongruenza.
Inoltre, in caso di diverso numero delle colonne non sarà possibile analizzarne il contenuto. Vengono analizzati i dati presenti all’interno delle colonne anche nel caso in cui queste siano di tipo diverso: non è raro che su DBMS differenti, tipi fra loro compatibili abbiano nome diverso, come ad esempio i tipi INT (in MSSQL) e NUMERIC (in Oracle). I confronti sulla base del nome delle colonne sono da effettuare in maniera case-insensitive, in modo da ridurre la dimensione del file di output evitando di mostrare informazioni irrilevanti.

Creazione file di output:

L’output del file sarà quindi presentato in forma testuale su un file specificato dall’utente, usando un formato personalizzato in cui si mostrano prima le eventuali differenze a livello di schema, quindi quelle riscontrate nel contenuto vero e proprio, indicando anche il numero di riga ed il nome di colonna in cui si verificano.
