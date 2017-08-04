package com.legrand.iln;

public interface ICostantiIrc {


/**
 * Progetto ILN
 * Copyright (C) 2003 Monsieur Legrand
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the license, or any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA
 */

/**
 * Interfaccia contenente tutte le costanti simboliche utili 
 * per implementare il protocollo del servizio IRC.
 *
 * @author Monsieur Legrand
 * @version 1.4 rev 1
 * @date   10 marzo 2003
 */

    /*
      - The server sends Replies 001 to 004 to a user upon
        successful registration.
    */
    public static String RPL_WELCOME = "001";
              //RPL_WELCOME
              //"Welcome to the Internet Relay Network
               //<nick>!<user>@<host>"
    public static String RPL_YOURHOST = "002";
              //"Your host is <servername>, running version <ver>"
    public static String RPL_CREATED  = "003";
              //"This server was created <date>"
    public static String RPL_MYINFO = "004";
              //"<servername> <version> <available user modes>
              // <available channel modes>"

    /*
      - Sent by the server to a user to suggest an alternative
        server.  This is often used when the connection is
        refused because the server is already full.
    */
    public static String  RPL_BOUNCE = "005";
              //"Try server <server name>, port <port number>"

    /*
      - Reply format used by USERHOST to list replies to
        the query list.  The reply string is composed as
        follows:

        reply = nickname [ "*" ] "=" ( "+" / "-" ) hostname

        The '*' indicates whether the client has registered
        as an Operator.  The '-' or '+' characters represent
        whether the client has set an AWAY message or not
        respectively.
    */     

    public static String RPL_USERHOST = "302";
              //":*1<reply> *( " " <reply> )"

     /*
     - Reply format used by ISON to list replies to the
           query list.
     */    
     public static String RPL_ISON = "303";
              //":*1<nick> *( " " <nick> )"

         
     /*
       - These replies are used with the AWAY command (if
         allowed).  RPL_AWAY is sent to any client sending a
         PRIVMSG to a client which is away.  RPL_AWAY is only
         sent by the server to which the client is connected.
         Replies RPL_UNAWAY and RPL_NOWAWAY are sent when the
         client removes and sets an AWAY message.
     
     */
     public static String RPL_AWAY = "301";
              //"<nick> :<away message>"
     public static String RPL_UNAWAY = "305";
              //":You are no longer marked as being away"
     public static String RPL_NOWAWAY = "306";
              //":You have been marked as being away"

         
     /*
         - Replies 311 - 313, 317 - 319 are all replies
           generated in response to a WHOIS message.  Given that
           there are enough parameters present, the answering
           server MUST either formulate a reply out of the above
           numerics (if the query nick is found) or return an
           error reply.  The '*' in RPL_WHOISUSER is there as
           the literal character and not as a wild card.  For
           each reply set, only RPL_WHOISCHANNELS may appear
           more than once (for long lists of channel names).
           The '@' and '+' characters next to the channel name
           indicate whether a client is a channel operator or
           has been granted permission to speak on a moderated
           channel.  The RPL_ENDOFWHOIS reply is used to mark
           the end of processing a WHOIS message.
     
     */
     public static String RPL_WHOISUSER = "311";
              //"<nick> <user> <host> * :<real name>"
     public static String RPL_WHOISSERVER = "312";
              //"<nick> <server> :<server info>"
     public static String RPL_WHOISOPERATOR = "313";
              //"<nick> :is an IRC operator"

     public static String RPL_WHOISIDLE = "317";
              //"<nick> <integer> :seconds idle"
     public static String RPL_ENDOFWHOIS = "318";
              //"<nick> :End of WHOIS list"
     public static String RPL_WHOISCHANNELS = "319";
              //"<nick> :*( ( "@" / "+" ) <channel> " " )"

       
     /*
       - When replying to a WHOWAS message, a server MUST use
         the replies RPL_WHOWASUSER, RPL_WHOISSERVER or
         ERR_WASNOSUCHNICK for each nickname in the presented
         list.  At the end of all reply batches, there MUST
         be RPL_ENDOFWHOWAS (even if there was only one reply
         and it was an error).
     */
     public static String RPL_WHOWASUSER = "314";
              //"<nick> <user> <host> * :<real name>"
     public static String RPL_ENDOFWHOWAS = "369";
              //"<nick> :End of WHOWAS"

       
     /*
       - Replies RPL_LIST, RPL_LISTEND mark the actual replies
         with data and end of the server's response to a LIST
         command.  If there are no channels available to return,
         only the end reply MUST be sent.
     */
     public static String RPL_LISTSTART = "321";
              //Obsolete. Not used.

     public static String RPL_LIST = "322";
              //"<channel> <# visible> :<topic>"
     public static String RPL_LISTEND = "323";
              //":End of LIST"

         
     /*
       - When sending a TOPIC message to determine the
         channel topic, one of two replies is sent.  If
         the topic is set, RPL_TOPIC is sent back else
         RPL_NOTOPIC.
     */
     public static String RPL_UNIQOPIS = "325";
              //"<channel> <nickname>"

     public static String RPL_CHANNELMODEIS = "324";
              //"<channel> <mode> <mode params>"

     public static String RPL_NOTOPIC = "331";
              //"<channel> :No topic is set"
     public static String RPL_TOPIC  = "332";
              //"<channel> :<topic>"

         
     /*
       - Returned by the server to indicate that the
         attempted INVITE message was successful and is
         being passed onto the end client.
     */
     public static String RPL_INVITING = "341";
              //"<channel> <nick>"

         
     /*
       - Returned by a server answering a SUMMON message to
         indicate that it is summoning that user.
     */
     public static String RPL_SUMMONING = "342";
              //"<user> :Summoning user to IRC"

     /*
       - When listing the 'invitations masks' for a given channel,
         a server is required to send the list back using the
         RPL_INVITELIST and RPL_ENDOFINVITELIST messages.  A
         separate RPL_INVITELIST is sent for each active mask.
         After the masks have been listed (or if none present) a
         RPL_ENDOFINVITELIST MUST be sent.
     */
     public static String RPL_INVITELIST = "346";
              //"<channel> <invitemask>"
     public static String RPL_ENDOFINVITELIST = "347";
              //"<channel> :End of channel invite list"

     /*
       - When listing the 'exception masks' for a given channel,
         a server is required to send the list back using the
         RPL_EXCEPTLIST and RPL_ENDOFEXCEPTLIST messages.  A
         separate RPL_EXCEPTLIST is sent for each active mask.
         After the masks have been listed (or if none present)
         a RPL_ENDOFEXCEPTLIST MUST be sent.
     */
     public static String RPL_EXCEPTLIST = "348";
              //"<channel> <exceptionmask>"
     public static String RPL_ENDOFEXCEPTLIST = "349";
              //"<channel> :End of channel exception list"

     /*
       - Reply by the server showing its version details.
         The <version> is the version of the software being
         used (including any patchlevel revisions) and the
         <debuglevel> is used to indicate if the server is
         running in "debug mode".
         The "comments" field may contain any comments about
         the version or further version details.
     */
     public static String RPL_VERSION = "351";
              //"<version>.<debuglevel> <server> :<comments>"

     /*
       - The RPL_WHOREPLY and RPL_ENDOFWHO pair are used
         to answer a WHO message.  The RPL_WHOREPLY is only
         sent if there is an appropriate match to the WHO
         query.  If there is a list of parameters supplied
         with a WHO message, a RPL_ENDOFWHO MUST be sent
         after processing each list item with <name> being
         the item.
     */
     public static String RPL_WHOREPLY = "352";
              //"<channel> <user> <host> <server> <nick>
              //( "H" / "G" > ["*"] [ ( "@" / "+" ) ]
              //:<hopcount> <real name>"

     public static String RPL_ENDOFWHO = "315";
              //"<name> :End of WHO list"

     /*
       - To reply to a NAMES message, a reply pair consisting
         of RPL_NAMREPLY and RPL_ENDOFNAMES is sent by the
         server back to the client.  If there is no channel
         found as in the query, then only RPL_ENDOFNAMES is
         returned.  The exception to this is when a NAMES
         message is sent with no parameters and all visible
         channels and contents are sent back in a series of
         RPL_NAMEREPLY messages with a RPL_ENDOFNAMES to mark
         the end.
     */
     public static String RPL_NAMREPLY = "353";
              //"( "=" / "*" / "@" ) <channel>
              //:[ "@" / "+" ] <nick> *( " " [ "@" / "+" ] <nick> )
              //- "@" is used for secret channels, "*" for private
              //   channels, and "=" for others (public channels).

     public static String RPL_ENDOFNAMES = "366";
              //"<channel> :End of NAMES list"

     /*
       - In replying to the LINKS message, a server MUST send
         replies back using the RPL_LINKS numeric and mark the
         end of the list using an RPL_ENDOFLINKS reply.
     */
     public static String RPL_LINKS = "364";
              //"<mask> <server> :<hopcount> <server info>"
     public static String RPL_ENDOFLINKS = "365";
              //"<mask> :End of LINKS list"

     /*
       - When listing the active 'bans' for a given channel,
         a server is required to send the list back using the
         RPL_BANLIST and RPL_ENDOFBANLIST messages.  A separate
         RPL_BANLIST is sent for each active banmask.  After the
         banmasks have been listed (or if none present) a
         RPL_ENDOFBANLIST MUST be sent.
     */
     public static String RPL_BANLIST = "367";
              //"<channel> <banmask>"
     public static String RPL_ENDOFBANLIST = "368";
              //"<channel> :End of channel ban list"

     /*
       - A server responding to an INFO message is required to
         send all its 'info' in a series of RPL_INFO messages
         with a RPL_ENDOFINFO reply to indicate the end of the
         replies.
     */
     public static String RPL_INFO = "371";
              //":<string>"
     public static String RPL_ENDOFINFO = "374";
              //":End of INFO list"

     /*
       - When responding to the MOTD message and the MOTD file
         is found, the file is displayed line by line, with
         each line no longer than 80 characters, using
         RPL_MOTD format replies.  These MUST be surrounded
         by a RPL_MOTDSTART (before the RPL_MOTDs) and an
         RPL_ENDOFMOTD (after).
     */
     public static String RPL_MOTDSTART = "375";
              //":- <server> Message of the day - "
     public static String RPL_MOTD = "372";
              //":- <text>"
     public static String RPL_ENDOFMOTD = "376";
              //":End of MOTD command"

     /*
       - RPL_YOUREOPER is sent back to a client which has
         just successfully issued an OPER message and gained
         operator status.
     */

     public static String RPL_YOUREOPER = "381";
              //":You are now an IRC operator"

     /*
       - If the REHASH option is used and an operator sends
         a REHASH message, an RPL_REHASHING is sent back to
         the operator.
     */
     public static String RPL_REHASHING = "382";
              //"<config file> :Rehashing"

     /*
       - Sent by the server to a service upon successful
         registration.
     */

     public static String RPL_YOURESERVICE = "383";
              //"You are service <servicename>"

     /*
       - When replying to the TIME message, a server MUST send
         the reply using the RPL_TIME format above.  The string
         showing the time need only contain the correct day and
         time there.  There is no further requirement for the
         time string.
     */

     public static String RPL_TIME = "391";
              //"<server> :<string showing server's local time>"

     /*
       - If the USERS message is handled by a server, the
         replies RPL_USERSTART, RPL_USERS, RPL_ENDOFUSERS and
         RPL_NOUSERS are used.  RPL_USERSSTART MUST be sent
         first, following by either a sequence of RPL_USERS
         or a single RPL_NOUSER.  Following this is
         RPL_ENDOFUSERS.
     */

     public static String RPL_USERSSTART = "392";
              //":UserID   Terminal  Host"
     public static String RPL_USERS = "393";
              //":<username> <ttyline> <hostname>"
     public static String RPL_ENDOFUSERS = "394";
              //":End of users" 
     public static String RPL_NOUSERS = "395";
              //":Nobody logged in"

     /*
         - The RPL_TRACE* are all returned by the server in
           response to the TRACE message.  How many are
           returned is dependent on the TRACE message and
           whether it was sent by an operator or not.  There
           is no predefined order for which occurs first.
           Replies RPL_TRACEUNKNOWN, RPL_TRACECONNECTING and
           RPL_TRACEHANDSHAKE are all used for connections
           which have not been fully established and are either
           unknown, still attempting to connect or in the
           process of completing the 'server handshake'.
           RPL_TRACELINK is sent by any server which handles
           a TRACE message and has to pass it on to another
           server.  The list of RPL_TRACELINKs sent in
           response to a TRACE command traversing the IRC
           network should reflect the actual connectivity of
           the servers themselves along that path.
           RPL_TRACENEWTYPE is to be used for any connection
           which does not fit in the other categories but is
           being displayed anyway.
           RPL_TRACEEND is sent to indicate the end of the list.
     */

     public static String RPL_TRACELINK = "200";
              //"Link <version & debug level> <destination>
              // <next server> V<protocol version>
              // <link uptime in seconds> <backstream sendq>
              // <upstream sendq>"
     public static String RPL_TRACECONNECTING = "201";
              //"Try. <class> <server>"
     public static String RPL_TRACEHANDSHAKE = "202";
              //"H.S. <class> <server>"
     public static String RPL_TRACEUNKNOWN = "203";
              //"???? <class> [<client IP address in dot form>]"
     public static String RPL_TRACEOPERATOR = "204";
              //"Oper <class> <nick>"
     public static String RPL_TRACEUSER = "205";
              //"User <class> <nick>"
     public static String RPL_TRACESERVER = "206";
              //"Serv <class> <int>S <int>C <server>
              // <nick!user|*!*>@<host|server> V<protocol version>"
     public static String RPL_TRACESERVICE = "207";
              //"Service <class> <name> <type> <active type>"
     public static String RPL_TRACENEWTYPE = "208";
              //"<newtype> 0 <client name>"
     public static String RPL_TRACECLASS = "209";
              //"Class <class> <count>"
     public static String RPL_TRACERECONNECT = "210";
              //Unused.
     public static String RPL_TRACELOG = "261";
              //"File <logfile> <debug level>"
     public static String RPL_TRACEEND = "262";
              //"<server name> <version & debug level> :End of TRACE"

     /*
         - reports statistics on a connection.  <linkname>
           identifies the particular connection, <sendq> is
           the amount of data that is queued and waiting to be
           sent <sent messages> the number of messages sent,
           and <sent Kbytes> the amount of data sent, in
           Kbytes. <received messages> and <received Kbytes>
           are the equivalent of <sent messages> and <sent
           Kbytes> for received data, respectively.  <time
           open> indicates how long ago the connection was
           opened, in seconds.
     */
     public static String RPL_STATSLINKINFO = "211";
              //"<linkname> <sendq> <sent messages>
              // <sent Kbytes> <received messages>
              // <received Kbytes> <time open>"

     /*
       - reports statistics on commands usage.
     */
     public static String RPL_STATSCOMMANDS = "212";
              //"<command> <count> <byte count> <remote count>"

     public static String RPL_ENDOFSTATS = "219";
              //"<stats letter> :End of STATS report"

     /*
       - reports the server uptime.
     */
     public static String RPL_STATSUPTIME = "242";
              //":Server Up %d days %d:%02d:%02d"

     public static String RPL_STATSOLINE = "243";
              //"O <hostmask> * <name>"

     /*
       - To answer a query about a client's own mode,
         RPL_UMODEIS is sent back.
     */
     public static String RPL_UMODEIS = "221";
              //"<user mode string>"

         
     /*
         - When listing services in reply to a SERVLIST message,
           a server is required to send the list back using the
           RPL_SERVLIST and RPL_SERVLISTEND messages.  A separate
           RPL_SERVLIST is sent for each service.  After the
           services have been listed (or if none present) a
           RPL_SERVLISTEND MUST be sent.
     */
     public static String RPL_SERVLIST = "234";
              //"<name> <server> <mask> <type> <hopcount> <info>"

     public static String RPL_SERVLISTEND = "235";
              //"<mask> <type> :End of service listing"

     /*
         - In processing an LUSERS message, the server
           sends a set of replies from RPL_LUSERCLIENT,
           RPL_LUSEROP, RPL_USERUNKNOWN,
           RPL_LUSERCHANNELS and RPL_LUSERME.  When
           replying, a server MUST send back
           RPL_LUSERCLIENT and RPL_LUSERME.  The other
           replies are only sent back if a non-zero count
           is found for them.
     */    

     public static String RPL_LUSERCLIENT = "251";
              //":There are <integer> users and <integer>
              // services on <integer> servers"
     public static String RPL_LUSEROP = "252";
              //"<integer> :operator(s) online"
     public static String RPL_LUSERUNKNOWN = "253";
              //"<integer> :unknown connection(s)"
     public static String RPL_LUSERCHANNELS = "254";
              //"<integer> :channels formed"
     public static String RPL_LUSERME = "255";
              //":I have <integer> clients and <integer>
              //  servers"

       /*
         - When replying to an ADMIN message, a server
           is expected to use replies RPL_ADMINME
           through to RPL_ADMINEMAIL and provide a text
           message with each.  For RPL_ADMINLOC1 a
           description of what city, state and country
           the server is in is expected, followed by
           details of the institution (RPL_ADMINLOC2)
           and finally the administrative contact for the
           server (an email address here is REQUIRED)
           in RPL_ADMINEMAIL.
       */
       public static String RPL_ADMINME = "256";
              //"<server> :Administrative info"
       public static String RPL_ADMINLOC1 = "257";
              //":<admin info>"
       public static String RPL_ADMINLOC2 = "258";
              //":<admin info>"
       public static String RPL_ADMINEMAIL = "259";
              //":<admin info>"

       /*
         - When a server drops a command without processing it,
           it MUST use the reply RPL_TRYAGAIN to inform the
           originating client.
       */
       public static String RPL_TRYAGAIN = "263";
              //"<command> :Please wait a while and try again."

        
 /*
     5.2 Error Replies
         Error replies are found in the range from 400 to 599.
 */
       public static String ERR_NOSUCHNICK = "401";
              //"<nickname> :No such nick/channel"


       public static String ERR_NOSUCHSERVER = "402";
              //"<server name> :No such server"


       public static String ERR_NOSUCHCHANNEL = "403";
              //"<channel name> :No such channel"


       /*
         - Sent to a user who is either (a) not on a channel
           which is mode +n or (b) not a chanop (or mode +v) on
           a channel which has mode +m set or where the user is
           banned and is trying to send a PRIVMSG message to
           that channel.
       */
       public static String ERR_CANNOTSENDTOCHAN = "404";
              //"<channel name> :Cannot send to channel"


       public static String ERR_TOOMANYCHANNELS = "405";
              //"<channel name> :You have joined too many channels"

       /*
         - Returned by WHOWAS to indicate there is no history
           information for that nickname.
       */
       public static String ERR_WASNOSUCHNICK = "406";
              //"<nickname> :There was no such nickname"

       /*
         - Returned to a client which is attempting to send a
           PRIVMSG/NOTICE using the user@host destination format
           and for a user@host which has several occurrences.
         - Returned to a client which trying to send a
           PRIVMSG/NOTICE to too many recipients.
         - Returned to a client which is attempting to JOIN a safe
           channel using the shortname when there are more than one
           such channel.
       */
       public static String ERR_TOOMANYTARGETS = "407";
              //"<target> :<error code> recipients. <abort message>"

       public static String ERR_NOSUCHSERVICE = "408";
              //"<service name> :No such service"

       /*
         - PING or PONG message missing the originator parameter.
       */
       public static String ERR_NOORIGIN = "409";
              //":No origin specified"

       /*
         - 412 - 415 are returned by PRIVMSG to indicate that
           the message wasn't delivered for some reason.
           ERR_NOTOPLEVEL and ERR_WILDTOPLEVEL are errors that
           are returned when an invalid use of
           "PRIVMSG $<server>" or "PRIVMSG #<host>" is attempted.
       */
       public static String ERR_NORECIPIENT = "411";
              //":No recipient given (<command>)"
       public static String ERR_NOTEXTTOSEND = "412";
              //":No text to send"
       public static String ERR_NOTOPLEVEL = "413";
              //"<mask> :No toplevel domain specified"
       public static String ERR_WILDTOPLEVEL = "414";
              //"<mask> :Wildcard in toplevel domain"
       public static String ERR_BADMASK = "415";
              //"<mask> :Bad Server/host mask"

       public static String ERR_UNKNOWNCOMMAND = "421";
              //"<command> :Unknown command"

       public static String ERR_NOMOTD = "422";
              //":MOTD File is missing"

       public static String ERR_NOADMININFO = "423";
              //"<server> :No administrative info available"

       /*
         - Generic error message used to report a failed file
           operation during the processing of a message.
       */
       public static String ERR_FILEERROR = "424";
              //":File error doing <file op> on <file>"

       /*
         - Returned when a nickname parameter expected for a
           command and isn't found.
       */
       public static String ERR_NONICKNAMEGIVEN = "431";
              //":No nickname given"

       /*
         - Returned after receiving a NICK message which contains
           characters which do not fall in the defined set.  See
           section 2.3.1 for details on valid nicknames.
       */
       public static String ERR_ERRONEUSNICKNAME = "432";
              //"<nick> :Erroneous nickname"

       public static String ERR_NICKNAMEINUSE = "433";
              //"<nick> :Nickname is already in use"

       /*
         - Returned by a server to a client when it detects a
           nickname collision (registered of a NICK that
           already exists by another server).
       */
       public static String ERR_NICKCOLLISION = "436";
              //"<nick> :Nickname collision KILL from <user>@<host>"

       /*
         - Returned by a server to a user trying to join a channel
           currently blocked by the channel delay mechanism.

         - Returned by a server to a user trying to change nickname
           when the desired nickname is blocked by the nick delay
           mechanism.
       */
       public static String ERR_UNAVAILRESOURCE = "437";
              //"<nick/channel> :Nick/channel is temporarily unavailable"

       public static String ERR_USERNOTINCHANNEL = "441";
              //"<nick> <channel> :They aren't on that channel"

       public static String ERR_NOTONCHANNEL = "442";
              //"<channel> :You're not on that channel"

       public static String ERR_USERONCHANNEL = "443";
              //"<user> <channel> :is already on channel"

       /*
         - Returned by the summon after a SUMMON command for a
           user was unable to be performed since they were not
           logged in.
       */
       public static String ERR_NOLOGIN = "444";
              //"<user> :User not logged in"

       public static String ERR_SUMMONDISABLED = "445";
              //":SUMMON has been disabled"

       public static String ERR_USERSDISABLED = "446";
              //":USERS has been disabled"

       /*
         - Returned by the server to indicate that the client
           MUST be registered before the server will allow it
           to be parsed in detail.
       */
       public static String ERR_NOTREGISTERED = "451";
              //":You have not registered"

       public static String ERR_NEEDMOREPARAMS = "461";
              //"<command> :Not enough parameters"

       /*
         - Returned by the server to any link which tries to
           change part of the registered details (such as
           password or user details from second USER message).
       */
       public static String ERR_ALREADYREGISTRED = "462";
              //":Unauthorized command (already registered)"

       /*
         - Returned to a client which attempts to register with
           a server which does not been setup to allow
           connections from the host the attempted connection
           is tried.
       */
       public static String ERR_NOPERMFORHOST = "463";
              //":Your host isn't among the privileged"

       public static String ERR_PASSWDMISMATCH = "464";
              //":Password incorrect"

       public static String ERR_YOUREBANNEDCREEP = "465";
              //":You are banned from this server"

       public static String ERR_YOUWILLBEBANNED = "466";

       public static String ERR_KEYSET = "467";
              //"<channel> :Channel key already set"
       public static String ERR_CHANNELISFULL = "471";
              //"<channel> :Cannot join channel (+l)"
       public static String ERR_UNKNOWNMODE = "472";
              //"<char> :is unknown mode char to me for <channel>"
       public static String ERR_INVITEONLYCHAN = "473";
              //"<channel> :Cannot join channel (+i)"
       public static String ERR_BANNEDFROMCHAN = "474";
              //"<channel> :Cannot join channel (+b)"
       public static String ERR_BADCHANNELKEY = "475";
              //"<channel> :Cannot join channel (+k)"
       public static String ERR_BADCHANMASK = "476";
              //"<channel> :Bad Channel Mask"
       public static String ERR_NOCHANMODES = "477";
              //"<channel> :Channel doesn't support modes"
       public static String ERR_BANLISTFULL = "478";
              //"<channel> <char> :Channel list is full"

       public static String ERR_NOPRIVILEGES = "481";
              //":Permission Denied- You're not an IRC operator"

       /*
         - Any command requiring 'chanop' privileges (such as
           MODE messages) MUST return this error if the client
           making the attempt is not a chanop on the specified
           channel.
       */
       public static String ERR_CHANOPRIVSNEEDED = "482";
              //"<channel> :You're not channel operator"

       public static String ERR_CANTKILLSERVER = "483";
              //":You can't kill a server!"

       /*
         - Sent by the server to a user upon connection to indicate
           the restricted nature of the connection (user mode "+r").
       */
       public static String ERR_RESTRICTED = "484";
              //":Your connection is restricted!"

       /*
         - Any MODE requiring "channel creator" privileges MUST
           return this error if the client making the attempt is not
           a chanop on the specified channel.
       */
       public static String ERR_UNIQOPPRIVSNEEDED = "485";
              //":You're not the original channel operator"

       /*
         - If a client sends an OPER message and the server has
           not been configured to allow connections from the
           client's host as an operator, this error MUST be
           returned.
       */
       public static String ERR_NOOPERHOST = "491";
              //":No O-lines for your host"

       public static String ERR_UMODEUNKNOWNFLAG = "501";
              //":Unknown MODE flag"

       public static String ERR_USERSDONTMATCH = "502";
              //":Cannot change mode for other users"

   /*
     5.3 Reserved numerics

     These numerics are not described above since they fall into one of
     the following categories:

     3. in current use but are part of a non-generic 'feature' of
        the current IRC server.

            231    RPL_SERVICEINFO     232  RPL_ENDOFSERVICES
            233    RPL_SERVICE
            300    RPL_NONE            316  RPL_WHOISCHANOP
            361    RPL_KILLDONE        362  RPL_CLOSING
            363    RPL_CLOSEEND        373  RPL_INFOSTART
            384    RPL_MYPORTIS

            213    RPL_STATSCLINE      214  RPL_STATSNLINE
            215    RPL_STATSILINE      216  RPL_STATSKLINE
            217    RPL_STATSQLINE      218  RPL_STATSYLINE
            240    RPL_STATSVLINE      241  RPL_STATSLLINE
            244    RPL_STATSHLINE      244  RPL_STATSSLINE
            246    RPL_STATSPING       247  RPL_STATSBLINE
            250    RPL_STATSDLINE

            492    ERR_NOSERVICEHOST
   */
}
