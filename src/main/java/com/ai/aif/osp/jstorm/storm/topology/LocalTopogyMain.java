package com.ai.aif.osp.jstorm.storm.topology;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.generated.AlreadyAliveException;
import backtype.storm.generated.InvalidTopologyException;
import backtype.storm.spout.SchemeAsMultiScheme;
import backtype.storm.topology.TopologyBuilder;
import com.ai.aif.osp.jstorm.storm.bolt.OspESBolt;
import com.ai.aif.osp.jstorm.storm.spout.OspSpout;
import storm.kafka.*;

import java.util.Arrays;

/**
 * @Author :zuogy@asiainfo.com
 * Created on 2018/3/27.
 */
public class LocalTopogyMain {
    private static String brokerZks = "10.11.20.81:2281";
    private static String topic = "LOG4X-MSG-TOPIC";
    private static String zkRoot = "/kafka";
    private static BrokerHosts brokerHosts = null;

    private static void setEnv(){
        //TODO 通过走配置获取
        brokerHosts = new ZkHosts(brokerZks,"/kafka/brokers");
    }

    public static void main(String[] args) throws AlreadyAliveException, InvalidTopologyException {
        Config conf = new Config();
        conf.setDebug(true);
        //初始化kafkaSpout
        setEnv();
        SpoutConfig spoutConfig = new SpoutConfig(brokerHosts,topic,zkRoot,"localTopologyConfig");
        spoutConfig.startOffsetTime = -1l;
        spoutConfig.scheme = new SchemeAsMultiScheme(new StringScheme());
        spoutConfig.zkPort = Integer.valueOf(2281);
        spoutConfig.zkServers = Arrays.asList(new String[]{"10.11.20.81"});
        spoutConfig.fetchSizeBytes = 1024 * 1024 * 2;
        spoutConfig.bufferSizeBytes = 1024 * 1024 * 2;
        spoutConfig.socketTimeoutMs = 5000;//减少一半
        spoutConfig.fetchMaxWait = 5000;
        KafkaSpout ospSpout = new OspSpout(spoutConfig);

        //定义自己的topo
        TopologyBuilder builder = new TopologyBuilder();
        builder.setSpout("osp-spout",ospSpout,1);
        builder.setBolt("osp-es-bolt",new OspESBolt(),2).setNumTasks(3).shuffleGrouping("osp-spout");

        conf.put(Config.TOPOLOGY_MAX_SPOUT_PENDING, 1);

        LocalCluster cluster = new LocalCluster();
        cluster.submitTopology("test", conf, builder.createTopology());

    }
}
