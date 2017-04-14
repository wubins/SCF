package com.scf.core.context.spring.morphia;

import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.config.AbstractFactoryBean;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ReadPreference;
import com.mongodb.ServerAddress;
import com.mongodb.WriteConcern;

public class MongoFactoryBean extends AbstractFactoryBean<MongoClient> {
    // 表示服务器列表(主从复制或者分片)的字符串数组
    private String[] serverStrings;
    
    // 表示用户列表
    private String[] authStrings;
    
    // mongoDB配置对象
    
    // 是否主从分离(读取从库)，默认读写都在主库
    private boolean readSecondary = false;
    
    // 设定写策略(出错时是否抛异常)，默认采用SAFE模式(需要抛异常)
    private WriteConcern writeConcern = WriteConcern.SAFE;
    
    // 与目标数据库能够建立的最大connection数量,默认为50
    private int connectionsPerHost = 50;
    
    // 如果当前所有的connection都在使用中，则每个connection上可以有多少个线程排队等待，默认为50
    private int threadsAllowedToBlockForConnectionMultiplier = 50;
    
    /*
     * 一个线程访问数据库的时候，在成功获取到一个可用数据库连接之前的最长等待时间为5分钟 这里比较危险，如果超过maxWaitTime都没有获取到这个连接的话，该线程就会抛出Exception
     * 故这里设置的maxWaitTime应该足够大，以免由于排队线程过多造成的数据库访问失败
     */
    private int maxWaitTime = 1000 * 60 * 5;
    
    // 与数据库建立连接的timeout设置为1分钟
    private int connectTimeout = 1000 * 60 * 1;
    
    @Override
    public Class<?> getObjectType() {
        return MongoClient.class;
    }
    
    @Override
    protected MongoClient createInstance()
        throws Exception {
        MongoClient mongoClient = initMongoClient();
        
        // 设定主从分离
        if (readSecondary) {
            mongoClient.setReadPreference(ReadPreference.secondaryPreferred());
        }
        
        // 设定写策略
        mongoClient.setWriteConcern(writeConcern);
        return mongoClient;
    }
    
    /**
     * 初始化mongo实例
     * 
     * @return
     * @throws Exception
     */
    private MongoClient initMongoClient()
        throws Exception {
        // 根据条件创建Mongo实例
        MongoClient mongoClient = null;
        List<ServerAddress> serverList = getServerList();
        List<MongoCredential> credentialsList = getCredentialsList();
        
        MongoClientOptions.Builder build = new MongoClientOptions.Builder();
        build.connectionsPerHost(connectionsPerHost);
        build.threadsAllowedToBlockForConnectionMultiplier(threadsAllowedToBlockForConnectionMultiplier);
        build.maxWaitTime(maxWaitTime);
        build.connectTimeout(connectTimeout);
        
        // 读取服务器
        if (serverList.size() == 0) {
            mongoClient = new MongoClient();
        }
        else if (serverList.size() == 1) {
            mongoClient = new MongoClient(serverList.get(0), credentialsList, build.build());
        }
        else {
            mongoClient = new MongoClient(serverList, credentialsList, build.build());
        }
        
        // 读取用户
        
        return mongoClient;
    }
    
    /**
     * 根据用户字符串列表，解析出用户对象列表
     * <p>
     * 
     * @Title: getCredentialsList
     *         </p>
     * 
     * @return
     * @throws Exception
     */
    private List<MongoCredential> getCredentialsList()
        throws Exception {
        List<MongoCredential> credentialsList = new LinkedList<MongoCredential>();
        try {
            if (authStrings == null)
                return credentialsList;
            for (String authString : authStrings) {
                String[] temp = authString.split(":");
                String userName = temp[0];
                String password = temp[1];
                String db = temp[2];
                if (temp.length != 3) {
                    throw new IllegalArgumentException("Invalid credentials string: " + authString);
                }
                else {
                    credentialsList.add(MongoCredential.createCredential(userName, db, password.toCharArray()));
                }
            }
            return credentialsList;
        }
        catch (Exception e) {
            throw new Exception("Error while converting credentialsString to CredentialsList", e);
        }
    }
    
    /**
     * 根据服务器字符串列表，解析出服务器对象列表
     * <p>
     * 
     * @Title: getServerList
     *         </p>
     * 
     * @return
     * @throws Exception
     */
    private List<ServerAddress> getServerList()
        throws Exception {
        List<ServerAddress> serverList = new LinkedList<ServerAddress>();
        try {
            for (String serverString : serverStrings) {
                String[] temp = serverString.split(":");
                String host = temp[0];
                if (temp.length > 2) {
                    throw new IllegalArgumentException("Invalid server address string: " + serverString);
                }
                if (temp.length == 2) {
                    serverList.add(new ServerAddress(host, Integer.parseInt(temp[1])));
                }
                else {
                    serverList.add(new ServerAddress(host));
                }
            }
            return serverList;
        }
        catch (Exception e) {
            throw new Exception("Error while converting serverString to ServerAddressList", e);
        }
    }
    
    public String[] getServerStrings() {
        return serverStrings;
    }
    
    public void setServerStrings(String[] serverStrings) {
        this.serverStrings = serverStrings;
    }
    
    public boolean isReadSecondary() {
        return readSecondary;
    }
    
    public void setReadSecondary(boolean readSecondary) {
        this.readSecondary = readSecondary;
    }
    
    public WriteConcern getWriteConcern() {
        return writeConcern;
    }
    
    public void setWriteConcern(WriteConcern writeConcern) {
        this.writeConcern = writeConcern;
    }
    
    public String[] getAuthStrings() {
        return authStrings;
    }
    
    public void setAuthStrings(String[] authStrings) {
        this.authStrings = authStrings;
    }
    
    public int getConnectionsPerHost() {
        return connectionsPerHost;
    }
    
    public void setConnectionsPerHost(int connectionsPerHost) {
        this.connectionsPerHost = connectionsPerHost;
    }
    
    public int getThreadsAllowedToBlockForConnectionMultiplier() {
        return threadsAllowedToBlockForConnectionMultiplier;
    }
    
    public void setThreadsAllowedToBlockForConnectionMultiplier(int threadsAllowedToBlockForConnectionMultiplier) {
        this.threadsAllowedToBlockForConnectionMultiplier = threadsAllowedToBlockForConnectionMultiplier;
    }
    
    public int getMaxWaitTime() {
        return maxWaitTime;
    }
    
    public void setMaxWaitTime(int maxWaitTime) {
        this.maxWaitTime = maxWaitTime;
    }
    
    public int getConnectTimeout() {
        return connectTimeout;
    }
    
    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }
    
}
