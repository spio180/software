/**
 * 
 */
package iKomunikator_server;

/**
 * @author pkos
 *
 */
public class Main {

	public static final Boolean debug = Boolean.TRUE;
	/**
	 * @param args
	 */
	public static void main(String[] args) {

		Configuration conf = new Configuration();
		conf.loadConfigFromFile("iKomunikator_server/server.cfg");
		System.out.printf("%s %d %d\n",conf.getServerIp(),conf.getPortNumber(),conf.getConnectionTimeout());
	}

}
