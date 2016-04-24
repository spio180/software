package iKomunikator_server;

import org.apache.commons.configuration2.XMLConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.ex.ConfigurationException;

/**
 * Configuration object that holds server configuration options.
 *
 * String mServerIp: Server Ip Address Optin
 * int mPortNumber: Server Port Number option
 * int mConnectionTimeout: Connetion Timeout
 * String mConfigFilePath: Relative path to server config file Default value: "iKomunikator_server/server.cfg";
 *
 *
 *
 * Created by lukasz on 24.04.16.
 */
public class Configuration {

    private String mServerIp;
    private int mPortNumber;
    private int mConnectionTimeout;
    private String mConfigFilePath = "iKomunikator_server/server.cfg";

    /**
     * Load server configuration from xml file from given @path.
     */
    public void loadConfigFromFile(String path) {
        if(path != null){
            mConfigFilePath = path;
        }

        Parameters params = new Parameters();
        FileBasedConfigurationBuilder<XMLConfiguration> builder =
                new FileBasedConfigurationBuilder<XMLConfiguration>(XMLConfiguration.class)
                        .configure(params.xml()
                                .setFileName(mConfigFilePath));
        XMLConfiguration readConfig = null;
        try {
            readConfig = builder.getConfiguration();
            mServerIp = readConfig.getString("IP");
            mPortNumber = readConfig.getInt("ServerPort");
            mConnectionTimeout = readConfig.getInt("ConnectionsLimit");
        } catch (ConfigurationException e) {
            System.out.printf("Error reading the config file\n");
            if(Main.debug == true) e.printStackTrace();
        }
    }

    public String getServerIp() {
        return mServerIp;
    }

    public void setServerIp(String serverIp) {
        mServerIp = serverIp;
    }

    public int getPortNumber() {
        return mPortNumber;
    }

    public void setPortNumber(int portNumber) {
        mPortNumber = portNumber;
    }

    public int getConnectionTimeout() {
        return mConnectionTimeout;
    }

    public void setConnectionTimeout(int connectionTimeout) {
        mConnectionTimeout = connectionTimeout;
    }

    public String getConfigFilePath() {
        return mConfigFilePath;
    }

    public void setConfigFilePath(String configFilePath) {
        mConfigFilePath = configFilePath;
    }

}
