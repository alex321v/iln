# ILN - Interprete di Linguaggio Narurale
Progetto di automa a stati finiti dotato di riconoscimento automatico del linguaggio naturale umano e di generazione autonoma di risposte.
Maggiori informazioni: <http://iln.ppcnerds.org>
Documentazione storica e tecnica su questo e progetti simili: <http://iln.ppcnerds.org/iln.html>

### Dipendenze
Per l'accesso alle API Telegram sto usando la libreria jar [TelegramBots](https://github.com/rubenlagus/TelegramBots)
Per il supporto delle emoj occorre la libreria [emoji-java](https://github.com/vdurmont/emoji-java)

## Prerequisiti
L'esecuzione richiede la presenza di una Java Virtual Machine (Java Runtime) versione 1.7.1 o successiva.
Lo sviluppo richiede JDK Standard Edition versione 1.7.1 o successiva.
Sia il Java Runtime sia il JDK sono disponibili gratuitamente su http://java.sun.com
La versione per Telegram richiede anche la presenza di una connessione di rete.

## Compilazione
Nella directory src digitare:
```sh
$ javac *.java -d ../bin
```
Nella directory bin digitare:
```sh
$ touch <nomeILN>nomi.txt
$ touch motd.txt
$ touch <nomeILN>.txt
```
Si consiglia VIVAMENTE di non lasciare vuoti questi files:
in <nomeILN>nomi.txt inserire almeno il nick del botmaster (il tuo nick!)
in <nomeILN>.txt inserire almeno una coppia stimolo/risposta, ad esempio
```
ciao --- ciao
```
in motd.txt digitare il messaggio di benvenuto, un saluto che l'ILN emette appena acceso.

## Esecuzione
Per client su macchina locale:
```sh
$ java TestClient <nick> <debug>
```
dove debug = 1 - più messaggi in log. debug = 0 - meno messaggi

Per client Telegram:
```sh
java com.legrand.iln.TelegramClient <nick> <debug>
```
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
