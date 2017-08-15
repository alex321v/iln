# ILN - Interprete di Linguaggio Narurale
Progetto di automa a stati finiti dotato di riconoscimento automatico del linguaggio naturale umano e di generazione autonoma di risposte.

Maggiori informazioni: <http://iln.ppcnerds.org>

Documentazione storica e tecnica su questo e progetti simili: <http://iln.ppcnerds.org/iln.html>

## Dipendenze
Per l'accesso alle API Telegram sto usando la libreria jar [TelegramBots](https://github.com/rubenlagus/TelegramBots).

Per il supporto delle emoj occorre la libreria [emoji-java](https://github.com/vdurmont/emoji-java).

Per il client Http è necessario installare [undertow](http://undertow.io/).

Tutte le dipendenze sono comunque riportate nel file _pom.xml_ 

## Prerequisiti
L'esecuzione richiede la presenza di una Java Virtual Machine (Java Runtime) versione 1.7.1 o successiva.

Lo sviluppo richiede JDK Standard Edition versione 1.7.1 o successiva.

Sia il Java Runtime sia il JDK sono disponibili gratuitamente su http://java.sun.com

La versione per Telegram richiede anche la presenza di una connessione di rete.

## Compilazione
Il metodo preferito per la compilazione è utilizzare [maven](https://maven.apache.org/) che si occuperà anche di scaricare tutte le dipendenze necessarie.

Nella directory root digitare:
```sh
$ mvn package
```
se tutto è andato bene dovrebbe essere stata creata una cartella *target* con all'interno il file **Iln.jar**

## Esecuzione
Prima di eseguire Iln è necessario predisporre alcuni file di testo:
 - [nickIln]nomi.txt contenente i nomi degli interlocutori conosciuti. si consiglia di inserire almeno il nick del botmaster (il tuo nick!)
 - motd.txt il messaggio di benvenuto, un saluto che l'ILN emette appena acceso.
 - [nickIln].txt il file di conoscenza di Iln. inserire almeno una coppia stimolo/risposta, ad esempio:
```
ciao --- ciao
```

se non indicato diversamente questi file vengono cercati in una cartella _resources_ posta nella cartella in cui viene eseguito Iln.

qualora questi file non vengano trovati vengono creati automaticamente a partire da quelli già presenti nella cartella _resources_ allegata.

Per eseguire Iln digitare:
```sh
$ java -jar target/Iln.jar
```

senza argomenti viene proposto l'help del software:
```
usage: java Iln -c <client-type> -n <nick> [options]
    --botmaster <arg>     botmaster nick (for Irc client)
 -c,--client <arg>        the type of client: Telegram, Irc, Test,
                          HttpServer
    --chan <arg>          chan name (for Irc client)
 -d,--debug               print debugging information
 -h,--help                print this message
    --host <arg>          server host (for Irc client)
 -n,--nick <arg>          the bot's nick
    --password <arg>      password (for Irc client)
    --port <arg>          server port (for Irc client)
    --resources <arg>     path of resources files (default: ./resources/)
    --server-host <arg>   http server host (for HttpServer)
    --server-port <arg>   http server port (for HttpServer)
    --token <arg>         bot token (for Telegram client)
```
a seconda del client scelto alcuni dei parametri sono obbligatori.

Per quanto riguarda la personalita', attualmente il codice viene rilasciato solo con il file di conoscenza di base. E' compito dell'utente aggiungere almeno una personalita' al progetto.
E' in fase di sviluppo il manuale per la realizzazione di personalita'.
Probabilmente, nei prossimi rilasci sara' inclusa una personalita' di prova.

## BUGS
Inviare dettagliati report di ogni bug a legrand@disadattati.org

## CREDITS
Per quanto riguarda la realizzazione del software, magari ce ne fossero.
Forse un giorno ce ne saranno ;-)

## CODERS:
Monsieur legrand <legrand@disadattati.org>
gx <gx@mortemale.org>
