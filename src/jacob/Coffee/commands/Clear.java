package jacob.Coffee.commands;

import java.util.List;
import java.util.Random;

import jacob.Coffee.Coffee;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class Clear extends ListenerAdapter {
	public static JDA jda = Coffee.jda;

	public static String[] joinMessages = { "Ding! [member] has joined!", "Ding! Welcome, [member]!" };

	@Override
	public void onGuildMemberJoin(GuildMemberJoinEvent event) {
		System.out.println("Join");
		Random rand = new Random();
		int index = rand.nextInt(joinMessages.length);
		String message = joinMessages[index].replace("[member]", event.getMember().getAsMention());
		event.getGuild().getTextChannelById(Coffee.WELCOME_ID).sendMessage(message).queue();

		event.getGuild().addRoleToMember(event.getMember(), event.getGuild().getRoleById(Coffee.CUSTOMER_ROLE_ID))
				.complete();

	}

	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		if (event.getAuthor().isBot())
			return;

		Message message = event.getMessage();
		String content = message.getContentRaw();

		if (!content.startsWith(Coffee.PREFIX)) {
			return;
		}

		String[] args = content.substring(Coffee.PREFIX.length()).split("\\s+");

		MessageChannel channel = event.getChannel();

		if (args[0].equalsIgnoreCase("clear")) {
			channel.sendTyping().queue();
			if (args.length < 2) {
				EmbedBuilder usage = new EmbedBuilder();
				usage.setTitle("Incorrect Usage!").setColor(0xff3923)
						.setDescription("Please specify the amount of messages you would like to delete.")
						.addField("Usage", "`" + Coffee.PREFIX + "clear [# of Messages]`", false);
				channel.sendMessage(usage.build()).queue();
				return;
			} else {
				try {
					List<Message> messages = channel.getHistory().retrievePast(Integer.parseInt(args[1])).complete();
					channel.purgeMessages(messages);
					EmbedBuilder success = new EmbedBuilder().setColor(0x22ff2a)
							.setTitle("Successfully deleted " + args[1] + " messages!");
					channel.sendMessage(success.build()).queue();

				} catch (IllegalArgumentException e) {
					EmbedBuilder error = new EmbedBuilder();
					if (e.toString().startsWith("java.lang.IllegalArgumentException: Message retrieval")) {
						error.setTitle("Too many messages!").setColor(0xff3923)
								.setDescription("I can only delete between 1 and 100 messages at a time!");
						channel.sendMessage(error.build()).queue();
					} else {
						error.setTitle("Old Messages!").setColor(0xff3923)
								.setDescription("I cannot delete a message older than 2 weeks.");
						channel.sendMessage(error.build()).queue();
					}
				}
			}
		}

	}

}
