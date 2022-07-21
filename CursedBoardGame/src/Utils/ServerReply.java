package Utils;

import java.io.Serializable;

/**
 * ServerReply Class
 * 
 * A ServerReply is sent by the Server to the Client.
 * A ServerReply has a ReplyCode that is used by the Client to tell what has to be done next.
 * A ServerReply may have a text that for the Client to print out.
 * A ServerReply may have an action for the Client run.
 * 
 * @author Alejandro Rodriguez Lopez
 *
 */
public class ServerReply implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private String reply;
	private ReplyCode code;
	
	/**
	 * Creates a new ServerReply
	 * 
	 * @param reply Reply text
	 */
	public ServerReply (String reply) {
		this(ReplyCode.NONE, reply);
	}

	/**
	 * Creates a new ServerReply
	 * 
	 * @param code Reply Code
	 */
	public ServerReply (ReplyCode code) {
		this(code, "");
	}

	/**
	 * Creates a new ServerReply
	 * @param reply Reply text
	 * @param code Reply Code
	 */
	public ServerReply (ReplyCode code, String reply) {
		this.reply = reply;
		this.code = code;
	}
	
	/**
	 * Returns the text Reply.
	 * 
	 * @return Text reply.
	 */
	public String getText () {
		return this.reply;
	}
	
	/**
	 * Returns the ReplyCode
	 * 
	 * @return ReplyCode
	 */
	public ReplyCode getCode () {
		return this.code;
	}

	/**
	 * Returns the result of the Action
	 * 
	 * @return Result of the Action
	 */
	public int run () {
		return this.code.run();
	}
}
