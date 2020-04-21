package jacob.Coffee;

import javax.security.auth.login.LoginException;

import jacob.Coffee.commands.Clear;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;

public class Coffee {
	// Variables
	public static final String PREFIX = "c!";
	public static final String DEV_ID = "446127283384614914";
	public static final String WELCOME_ID = "702142273437040730";
	public static final String CUSTOMER_ROLE_ID = "692693343657787413";

	public static JDA jda;

	// Main
	public static void main(String[] args) throws LoginException {
		jda = JDABuilder.createDefault(TOKEN).build();
		jda.getPresence().setStatus(OnlineStatus.ONLINE);
		jda.getPresence().setActivity(Activity.watching("coffee brew."));

		// Listeners
		jda.addEventListener(new Clear());
	}
}
