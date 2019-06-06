package com.iplus.wechat;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

@ComponentScan("com.iplus.config")
@Import({MongoDbConfig.class})
public class BaseRootConfig {
}
