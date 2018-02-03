/*
	Copyright 2018 Andre Schepers

	Licensed under the Apache License, Version 2.0 (the "License");
	you may not use this file except in compliance with the License.
	You may obtain a copy of the License at

	http://www.apache.org/licenses/LICENSE-2.0

	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	See the License for the specific language governing permissions and
	limitations under the License.
*/
package eu.andreschepers.authservice.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.vibur.dbcp.ViburDBCPDataSource;

import javax.sql.DataSource;

@Configuration
@ComponentScan(basePackages = {"eu.andreschepers.authservice.data"})
@EntityScan(basePackages = {"eu.andreschepers.authservice.data"})
public class DataConfiguration {

    @Autowired
    private Environment env;

    @Bean
    public DataSource dataSource() {

        try {
            Class.forName(env.getProperty("db.driver"));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        ViburDBCPDataSource ds = new ViburDBCPDataSource();
        ds.setJdbcUrl(env.getProperty("db.jdbcUrl"));
        ds.setUsername(env.getProperty("db.username"));
        ds.setPassword(env.getProperty("db.password"));
        ds.setPoolInitialSize(env.getProperty("db.poolInitialSize", Integer.class));
        ds.setPoolMaxSize(env.getProperty("db.poolMaxSize", Integer.class));
        ds.setConnectionIdleLimitInSeconds(env.getProperty("db.connectionIdleLimitInSeconds", Integer.class));
        ds.setTestConnectionQuery(env.getProperty("db.testConnectionQuery"));
        ds.setLogQueryExecutionLongerThanMs(env.getProperty("db.logQueryExecutionLongerThanMs", Integer.class));
        ds.setLogStackTraceForLongQueryExecution(env.getProperty("db.logStackTraceForLongQueryExecution", Boolean.class));
        ds.setStatementCacheMaxSize(env.getProperty("db.statementCacheMaxSize", Integer.class));
        ds.start();
        return ds;
    }
}
