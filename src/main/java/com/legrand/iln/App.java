package com.legrand.iln;

import java.io.*;
import java.util.ArrayList;
import java.util.Properties;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.io.FileUtils;

/**
 * Hello world!
 *
 */
public class App {

	public static void main(String[] args) {
		String nick = null;
		String client = null;
		String debug = "0";
		String host = null;
		String port = null;
		String chan = null;
		String botmaster = null;
		String pass = null;
		String telegramToken = null;

		Options options = new Options();

		Option help = new Option("h", "print this message");
		help.setLongOpt("help");

		Option debugOption = new Option("d", "print debugging information");
		debugOption.setLongOpt("debug");
		
		Option disableLearningOption = Option.builder().desc("disable learning phase").longOpt("disable-learning").build();

		Option resourceOption = Option.builder().hasArg().desc("path of resources files (default: ./resources/)")
				.longOpt("resources").build();

		Option clientType = Option.builder("c").hasArg().desc("the type of client: Telegram, Irc, Test, HttpServer")
				.longOpt("client").required().build();

		Option nickOption = Option.builder("n").hasArg().desc("the bot's nick").longOpt("nick").required().build();

		Option hostOption = Option.builder().hasArg().desc("server host (for Irc client)").longOpt("host").build();

		Option portOption = Option.builder().hasArg().desc("server port (for Irc client)").longOpt("port").build();

		Option chanOption = Option.builder().hasArg().desc("chan name (for Irc client)").longOpt("chan").build();

		Option botmasterOption = Option.builder().hasArg().desc("botmaster nick (for Irc client)").longOpt("botmaster")
				.build();

		Option passOption = Option.builder().hasArg().desc("password (for Irc client)").longOpt("password").build();

		Option telegramTokenOption = Option.builder().hasArg().desc("bot token (for Telegram client)").longOpt("token")
				.build();
		
		Option httpServerPort = Option.builder().hasArg().desc("http server port (for HttpServer)").longOpt("server-port")
				.build();
		
		Option httpServerHost = Option.builder().hasArg().desc("http server host (for HttpServer)").longOpt("server-host")
				.build();

		options.addOption(help);
		options.addOption(debugOption);
		options.addOption(clientType);
		options.addOption(nickOption);
		options.addOption(hostOption);
		options.addOption(portOption);
		options.addOption(chanOption);
		options.addOption(botmasterOption);
		options.addOption(passOption);
		options.addOption(telegramTokenOption);
		options.addOption(resourceOption);
		options.addOption(httpServerHost);
		options.addOption(httpServerPort);
		options.addOption(disableLearningOption);

		HelpFormatter formatter = new HelpFormatter();

		// create the parser
		CommandLineParser parser = new DefaultParser();
		Properties prop = new Properties();
		InputStream input = null;
		
		try {
			input = new FileInputStream("config.properties");

			prop.load(input);
			// parse the command line arguments
			CommandLine line = parser.parse(options, args);

			if (line.hasOption("nick")) {
				nick = line.getOptionValue("n");
			}
			if (line.hasOption("client")) {
				client = line.getOptionValue("client");
			}
			if (line.hasOption("debug")) {
				debug = "1";
			} else {
				debug = "0";
			}
			if (line.hasOption("help")) {
				formatter.printHelp("java Iln -c <client-type> -n <nick> [options]", options);
				System.exit(0);
			}
			if (line.hasOption("resources")) {
				Configurations.RESOURCE_PATH = line.getOptionValue("resources");
				System.out.println(Configurations.RESOURCE_PATH);
				File confFile = new File(prop.getProperty("resource_path"));
				if (!confFile.isDirectory()) {
					System.out.println("invalid resources path");
					System.exit(1);
				}
			}
			if(line.hasOption("disable-learning")) {
				Configurations.LEARNING_ENABLES = false;
			}
			if (client == null) {
				formatter.printHelp("java Iln -c <client-type> -n <nick> [options]", options);
				System.exit(1);
			}

			checkForFiles(nick);

			switch (client) {
			case "Telegram":
				if (line.hasOption("token")) {
					telegramToken = line.getOptionValue("token");
					TelegramIlnBot.token = telegramToken;
				} else {
					formatter.printHelp("java Iln -c Telegram -n <nick> -t <token> [options]", options);
					System.exit(1);
				}
				String telegramArgs[] = { nick, debug };
				TelegramClient.main(telegramArgs);
				break;
			case "Irc":
				if (line.hasOption("host") && line.hasOption("port") && line.hasOption("chan")
						&& line.hasOption("botmaster")) {
					host = line.getOptionValue("host");
					port = line.getOptionValue("port");
					chan = line.getOptionValue("chan");
					botmaster = line.getOptionValue("botmaster");
				} else {
					formatter.printHelp("java Iln -c Irc -n <nick> -t <token> [options]", options);
					System.exit(1);
				}

				ArrayList<String> ar = new ArrayList<String>();
				ar.add(host);
				ar.add(port);
				ar.add(nick);
				ar.add(chan);
				ar.add(botmaster);
				ar.add(debug);

				if (line.hasOption("password")) {
					pass = line.getOptionValue("password");
					ar.add(pass);
				}
				String ircArgs[] = {};
				ar.toArray(ircArgs);
				IrcClient.main(ircArgs);
				break;
			case "HttpServer":
				if(!line.hasOption("server-port") || !line.hasOption("server-host")) {
					formatter.printHelp("java Iln -c HttpServer -n <nick> --server-port <port> --server-host <host> [options]", options);
					System.exit(1);
				}
				String httpServerArgs[] = { nick, debug , line.getOptionValue("server-port") , line.getOptionValue("server-host") };
				HttpServerClient.main(httpServerArgs);
				break;				
			case "Test":
			default:
				String innerArgs[] = { nick, debug };
				TestClient.main(innerArgs);
				break;

			}

		} catch (ParseException exp) {
			// oops, something went wrong
			formatter.printHelp("java Iln -c <client-type> -n <nick> [options]", options);
			System.exit(0);
                } catch (IOException ex) {
                        ex.printStackTrace();
                } finally {
                        if (input != null) {
                        try {
                                      input.close();
                        } catch (IOException e) {
                                 e.printStackTrace();
                        }
               }
          }


	}

	private static void checkForFiles(String nick) {
		Properties prop = new Properties();
		InputStream input = null;

		try {

			input = new FileInputStream("./config.properties");

			// load a properties file
			prop.load(input);

			File f_nome = new File(prop.getProperty("resource_path"), nick + ".txt");
			if (!f_nome.exists() || !f_nome.isFile()) {

				f_nome.createNewFile();

			}

			File f_nomi = new File(prop.getProperty("resource_path"), nick + "nomi.txt");
			if (!f_nomi.exists() || !f_nomi.isFile()) {

				f_nomi.createNewFile();

			}

			File f_motd = new File(prop.getProperty("resource_path"), "motd.txt");
			if (!f_motd.exists() || !f_motd.isFile()) {

				f_motd.createNewFile();
				File f_motd_org = new File(prop.getProperty("default_motd"));
				FileUtils.copyFile(f_motd_org, f_motd);

			}

			File f_base = new File(prop.getProperty("resource_path"), "base.txt");
			if (!f_base.exists() || !f_base.isFile()) {

				f_base.createNewFile();
				File f_base_org = new File(prop.getProperty("default_base"));
				FileUtils.copyFile(f_base_org, f_base);

			}

			File f_iastemma = new File(prop.getProperty("default_base"), "IastemmiaFile.txt");
			if (!f_iastemma.exists() || !f_iastemma.isFile()) {

				f_iastemma.createNewFile();
				File f_iasetmma_org = new File(prop.getProperty("default_iastemma"));
				FileUtils.copyFile(f_iasetmma_org, f_iastemma);

			}
	} catch (IOException ex) {
		ex.printStackTrace();
	} finally {
		if (input != null) {
			try {
				input.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
   }
}
