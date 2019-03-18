package com.ai.aif.osp.jstorm.storm.spout;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import storm.kafka.KafkaSpout;
import storm.kafka.SpoutConfig;

/**
 * @Author :zuogy@asiainfo.com
 * Created on 2018/1/2.
 */
public class OspSpout extends KafkaSpout {

    private Logger LOG = LoggerFactory.getLogger(getClass());

    private static final long serialVersionUID = -4869928899962675500L;

    public OspSpout(SpoutConfig spoutConf) {
        super(spoutConf);
    }
}
